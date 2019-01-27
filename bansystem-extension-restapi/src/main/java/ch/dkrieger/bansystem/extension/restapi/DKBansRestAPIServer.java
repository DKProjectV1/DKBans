/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 01.01.19 13:25
 * @Website https://github.com/DevKrieger/DKBans
 *
 * The DKBans Project is under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package ch.dkrieger.bansystem.extension.restapi;

import ch.dkrieger.bansystem.extension.restapi.handler.RestApiHandler;
import ch.dkrieger.bansystem.extension.restapi.handler.defaults.*;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.utils.Document;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.util.ResourceLeakDetector;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DKBansRestAPIServer {

    static {
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);
    }
    private static DKBansRestAPIServer instance;
    private final EventLoopGroup parent, child;
    private final ServerBootstrap serverBootstrap;
    private final Map<String, RestApiHandler> handlers;
    private final DKBansRestApiConfig config;
    private final SslContext sslContext;

    public DKBansRestAPIServer(DKBansRestApiConfig config) {
        instance = this;
        this.config = config;
        this.handlers = new HashMap<>();
        this.parent = (Epoll.isAvailable()?new EpollEventLoopGroup():new NioEventLoopGroup());
        this.child = (Epoll.isAvailable()?new EpollEventLoopGroup():new NioEventLoopGroup());
        sslContext = createSslContext();

        this.serverBootstrap = new ServerBootstrap().group(this.parent,this.child)
                .channel(Epoll.isAvailable()?EpollServerSocketChannel.class:NioServerSocketChannel.class)
                .childOption(ChannelOption.AUTO_READ, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new DKBansChannelInitializer());

        //Register default handlers
        registerRestApiHandler(new FilterHandler());
        registerRestApiHandler(new BroadcastHandler());
        registerRestApiHandler(new PlayerHandler());
        registerRestApiHandler(new NetworkHandler());
        registerRestApiHandler(new ChatLogHandler());
    }
    public void registerRestApiHandler(RestApiHandler handler){
        this.handlers.put(handler.getPath(),handler);
    }
    public void startAsync(){
        new Thread(this::start).start();
    }
    public void start(){
        try{
            ChannelFuture future = this.serverBootstrap.bind(this.config.address).addListener((ChannelFutureListener) future1 -> {
                if(future1.isSuccess()) System.out.println(Messages.SYSTEM_PREFIX+"DKBansRestAPIServer is listening on "+config.address.getAddress().getHostAddress()+":"+config.address.getPort());
                else{
                    System.out.println(Messages.SYSTEM_PREFIX+"Failed to start DKBansRestAPIServer on "+config.address.getAddress().getHostAddress()+":"+config.address.getPort());
                    throw new RuntimeException(Messages.SYSTEM_PREFIX+"Failed to bind DKBansRestAPIServer");
                }
            }).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            future.syncUninterruptibly().channel().closeFuture().sync();
        }catch (Exception exception){
            System.out.println(Messages.SYSTEM_PREFIX+"Failed to start DKBansRestAPIServer on "+config.address.getAddress().getHostAddress()+":"+config.address.getPort());
            throw new RuntimeException(exception);
        }
    }
    public void shutdown(){
        if(this.parent != null) this.parent.shutdownGracefully();
        if(this.child  != null) this.child.shutdownGracefully();
    }
    private SslContext createSslContext(){
        try {
            if(config.sslEnabled){
                if(config.sslCustom){
                    File certificate = config.sslCertificate;
                    if(certificate.exists()){
                        File privateKey = config.sslPrivateKey;
                        if(privateKey.exists()) return SslContext.newServerContext(certificate,privateKey);
                        else System.out.println(Messages.SYSTEM_PREFIX+"PrivateKey file was not found (Ssl setup).");
                    }else System.out.println(Messages.SYSTEM_PREFIX+"Certificate file was not found (Ssl setup).");
                }
                SelfSignedCertificate ssc = new SelfSignedCertificate();
                return SslContext.newServerContext(ssc.certificate(),ssc.privateKey());
            }
        }catch (Exception exception){
            System.out.println(Messages.SYSTEM_PREFIX+"Could't not setup SSL encoding for RestAPIServer. ("+exception.getMessage()+")");
        }
        return null;
    }
    private class DKBansChannelInitializer extends ChannelInitializer<Channel> {
        @Override
        protected void initChannel(Channel channel) throws Exception {
            if(config.ipWhitelist.contains(channel.remoteAddress().toString().split(":")[0].replace("/",""))){
                if(sslContext != null) channel.pipeline().addLast(sslContext.newHandler(channel.alloc()));
                channel.pipeline().addLast("HttpServerCodec",new HttpServerCodec());
                channel.pipeline().addLast("HttpObjectAggregator",new HttpObjectAggregator(Integer.MAX_VALUE));
                channel.pipeline().addLast("HttpHandler",new DKBansHttpHandler());
            }else channel.close().syncUninterruptibly();
        }
    }
    private class DKBansHttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            if(ctx.channel() == null) return;
            if(!(ctx.channel().isOpen()) && !(ctx.channel().isActive()) && !(ctx.channel().isWritable())) ctx.channel().close().syncUninterruptibly();
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            if(!(cause instanceof IOException))cause.printStackTrace();
        }
        protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest httpRequest) throws Exception {
            if(ctx == null || httpRequest == null) return;
            FullHttpResponse httpResponse = new DefaultFullHttpResponse(httpRequest.protocolVersion(),HttpResponseStatus.NOT_FOUND);
            try{
                Document response = new Document();
                if(httpRequest.headers().contains("access-token") && config.accessTokens.contains(httpRequest.headers().get("access-token"))){
                    System.out.println(Messages.SYSTEM_PREFIX+"RestApi request from "+ctx.channel().remoteAddress().toString());
                    URI uri = new URI(httpRequest.uri());
                    String path = uri.getRawPath();
                    if(path.contains(".")) path = path.substring(0,path.indexOf("."));
                    RestApiHandler handler = handlers.get(path);
                    response.append("code",ResponseCode.OK).append("message","Success");
                    if(handler != null){
                        httpResponse.setStatus(HttpResponseStatus.OK);
                        handler.onRequest(getQuery(uri),response);
                    }else response.append("code",ResponseCode.NOT_FOUND).append("message","This api page was not found.");
                }else{
                    System.out.println(Messages.SYSTEM_PREFIX+"Unauthorized RestApi request from "+ctx.channel().remoteAddress().toString()+" (Canceled)");
                    httpResponse.setStatus(HttpResponseStatus.UNAUTHORIZED);
                    response.append("code",HttpResponseStatus.UNAUTHORIZED.code()).append("message","Wrong access token.");
                }
                httpResponse.content().writeBytes(response.toJson().getBytes());
            }catch (Exception exception) {
                exception.printStackTrace();
                httpResponse.setStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
            }
            ctx.writeAndFlush(httpResponse).addListener(ChannelFutureListener.CLOSE);
        }
    }
    public static DKBansRestAPIServer getInstance() {
        return instance;
    }

    private Document getQuery(URI uri){
        Map<String,String> values = new LinkedHashMap<>();
        try{
            String[] query = uri.getQuery().split("&");
            for(String subQuery : query){
                if(subQuery.length() >= 3 && subQuery.contains("=")){
                    String[] rawSubQuery = subQuery.split("=");
                    if(rawSubQuery.length >= 2) values.put(rawSubQuery[0],rawSubQuery[1]);
                }
            }
        }catch (Exception ignored){}
        return Document.loadData(values.get("query"));
    }
}

package ch.dkrieger.bansystem.lib.player;

public class OnlineSession {

    private String client,clientVersion, ip, country, lastServer, proxy, clientLanguage;
    private long connected, disconnected;

    public OnlineSession(String client, String clientVersion, String ip, String country, String lastServer, String proxy, String clientLanguage, long connected, long disconnected) {
        this.client = client;
        this.clientVersion = clientVersion;
        this.ip = ip;
        this.country = country;
        this.lastServer = lastServer;
        this.proxy = proxy;
        this.clientLanguage = clientLanguage;
        this.connected = connected;
        this.disconnected = disconnected;
    }

    public String getClient() {
        return client;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public String getIp() {
        return ip;
    }

    public String getCountry() {
        return country;
    }

    public String getLastServer() {
        return lastServer;
    }

    public String getProxy() {
        return proxy;
    }

    public String getClientLanguage() {
        return clientLanguage;
    }

    public long getConnected() {
        return connected;
    }

    public long getDisconnected() {
        return disconnected;
    }
    public long getDuration(){
        return this.connected-disconnected;
    }
}

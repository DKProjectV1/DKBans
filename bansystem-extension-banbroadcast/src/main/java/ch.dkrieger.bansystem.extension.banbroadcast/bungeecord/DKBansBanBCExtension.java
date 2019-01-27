/*
 * (C) Copyright 2018 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 30.12.18 14:39
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

package ch.dkrieger.bansystem.extension.banbroadcast.bungeecord;

import ch.dkrieger.bansystem.bungeecord.event.ProxiedNetworkPlayerBanEvent;
import ch.dkrieger.bansystem.bungeecord.event.ProxiedNetworkPlayerKickEvent;
import ch.dkrieger.bansystem.bungeecord.event.ProxiedNetworkPlayerUnbanEvent;
import ch.dkrieger.bansystem.bungeecord.event.ProxiedNetworkPlayerWarnEvent;
import ch.dkrieger.bansystem.extension.banbroadcast.BanBroadcastConfig;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.player.history.entry.Kick;
import ch.dkrieger.bansystem.lib.player.history.entry.Unban;
import ch.dkrieger.bansystem.lib.player.history.entry.Warn;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class DKBansBanBCExtension extends Plugin implements Listener {

    private BanBroadcastConfig config;

    @Override
    public void onEnable() {
        ProxyServer.getInstance().getPluginManager().registerListener(this,this);
        this.config = new BanBroadcastConfig();
    }
    @EventHandler
    public void onBan(ProxiedNetworkPlayerBanEvent event){
        Ban ban = event.getBan();
        String message = null;

        if(ban.getBanType() == BanType.NETWORK && config.NetworkBanMessageEnabled){
            if(ban.getTimeOut() < 1) message = config.NetworkBanMessagePermanently;
            else message = config.NetworkBanMessageTemporary;
        }else if(ban.getBanType() == BanType.CHAT && config.ChatBanMessageEnabled){
            if(ban.getTimeOut() < 1) message = config.ChatBanMessagePermanently;
            else message = config.ChatBanMessageTemporary;
        }
        if(message == null) return;
        TextComponent component = new TextComponent(message
                .replace("[player]",ban.getPlayer().getColoredName())
                .replace("[id]",""+ban.getID())
                .replace("[reason]",ban.getReason())
                .replace("[reasonID]",""+ban.getReasonID())
                .replace("[time]", BanSystem.getInstance().getConfig().dateFormat.format(ban.getTimeStamp()))
                .replace("[timeout]",BanSystem.getInstance().getConfig().dateFormat.format(ban.getTimeOut()))
                .replace("[message]",ban.getMessage())
                .replace("[type]",ban.getTypeName())
                .replace("[staff]",ban.getStaffName())
                .replace("[ip]",ban.getIp())
                .replace("[points]",""+ban.getPoints())
                .replace("[duration]", GeneralUtil.calculateDuration(ban.getDuration()))
                .replace("[remaining]",GeneralUtil.calculateRemaining(ban.getDuration(),false))
                .replace("[remaining-short]",GeneralUtil.calculateRemaining(ban.getDuration(),true))
                .replace("[prefix]",Messages.PREFIX_BAN));
        for(ProxiedPlayer player : ProxyServer.getInstance().getPlayers()){
            if(player.hasPermission("dkbans.banbroadcast.receive")) player.sendMessage(component);
        }
    }
    @EventHandler
    public void onKick(ProxiedNetworkPlayerKickEvent event){
        if(config.KickMessageEnabled){
            Kick kick = event.getKick();
            TextComponent component = new TextComponent(config.KickMessage
                    .replace("[prefix]", Messages.PREFIX_BAN)
                    .replace("[message]",kick.getMessage())
                    .replace("[staff]",kick.getStaffName())
                    .replace("[id]",""+kick.getID())
                    .replace("[ip]",kick.getIp())
                    .replace("[points]",""+kick.getPoints())
                    .replace("[player]",""+kick.getPlayer().getColoredName())
                    .replace("[server]",kick.getServer())
                    .replace("[reason]",kick.getReason()));
            for(ProxiedPlayer player : ProxyServer.getInstance().getPlayers()){
                if(player.hasPermission("dkbans.banbroadcast.receive")) player.sendMessage(component);
            }
        }
    }
    @EventHandler
    public void onUnban(ProxiedNetworkPlayerUnbanEvent event){
        if(config.UnbanMessageEnabled){
            Unban unban = event.getUnban();
            TextComponent component = new TextComponent(config.UnbanMessage
                    .replace("[prefix]", Messages.PREFIX_BAN)
                    .replace("[message]",unban.getMessage())
                    .replace("[staff]",unban.getStaffName())
                    .replace("[id]",""+unban.getID())
                    .replace("[ip]",unban.getIp())
                    .replace("[points]",""+unban.getPoints())
                    .replace("[player]",""+unban.getPlayer().getColoredName())
                    .replace("[reason]",unban.getReason()));
            for(ProxiedPlayer player : ProxyServer.getInstance().getPlayers()){
                if(player.hasPermission("dkbans.banbroadcast.receive")) player.sendMessage(component);
            }
        }
    }
    @EventHandler
    public void onWarn(ProxiedNetworkPlayerWarnEvent event){
        if(config.WarnMessageEnabled){
            Warn warn = event.getWarn();
            TextComponent component = new TextComponent(config.WarnMessage
                    .replace("[prefix]", Messages.PREFIX_BAN)
                    .replace("[message]",warn.getMessage())
                    .replace("[staff]",warn.getStaffName())
                    .replace("[id]",""+warn.getID())
                    .replace("[ip]",warn.getIp())
                    .replace("[points]",""+warn.getPoints())
                    .replace("[player]",""+warn.getPlayer().getColoredName())
                    .replace("[reason]",warn.getReason()));
            for(ProxiedPlayer player : ProxyServer.getInstance().getPlayers()){
                if(player.hasPermission("dkbans.banbroadcast.receive")) player.sendMessage(component);
            }
        }
    }
}

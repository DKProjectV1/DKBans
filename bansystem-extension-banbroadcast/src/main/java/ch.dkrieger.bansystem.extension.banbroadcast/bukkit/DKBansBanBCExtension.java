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

package ch.dkrieger.bansystem.extension.banbroadcast.bukkit;

import ch.dkrieger.bansystem.bukkit.event.BukkitNetworkPlayerBanEvent;
import ch.dkrieger.bansystem.bukkit.event.BukkitNetworkPlayerKickEvent;
import ch.dkrieger.bansystem.bukkit.event.BukkitNetworkPlayerUnbanEvent;
import ch.dkrieger.bansystem.bukkit.event.BukkitNetworkPlayerWarnEvent;
import ch.dkrieger.bansystem.extension.banbroadcast.BanBroadcastConfig;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.player.history.entry.Kick;
import ch.dkrieger.bansystem.lib.player.history.entry.Unban;
import ch.dkrieger.bansystem.lib.player.history.entry.Warn;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class DKBansBanBCExtension extends JavaPlugin implements Listener {

    private BanBroadcastConfig config;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this,this);
        this.config = new BanBroadcastConfig();
    }
    @EventHandler
    public void onBan(BukkitNetworkPlayerBanEvent event){
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
        Bukkit.broadcast(message
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
                .replace("[prefix]",Messages.PREFIX_BAN),"dkbans.banbroadcast.receive");
    }
    @EventHandler
    public void onKick(BukkitNetworkPlayerKickEvent event){
        if(config.KickMessageEnabled){
            Kick kick = event.getKick();
            Bukkit.broadcast(config.KickMessage
                    .replace("[prefix]", Messages.PREFIX_BAN)
                    .replace("[message]",kick.getMessage())
                    .replace("[staff]",kick.getStaffName())
                    .replace("[id]",""+kick.getID())
                    .replace("[ip]",kick.getIp())
                    .replace("[points]",""+kick.getPoints())
                    .replace("[player]",""+kick.getPlayer().getColoredName())
                    .replace("[server]",kick.getServer())
                    .replace("[reason]",kick.getReason()),"dkbans.banbroadcast.receive");
        }
    }
    @EventHandler
    public void onUnban(BukkitNetworkPlayerUnbanEvent event){
        if(config.UnbanMessageEnabled){
            Unban unban = event.getUnban();
            Bukkit.broadcast(config.UnbanMessage
                    .replace("[prefix]", Messages.PREFIX_BAN)
                    .replace("[message]",unban.getMessage())
                    .replace("[staff]",unban.getStaffName())
                    .replace("[id]",""+unban.getID())
                    .replace("[ip]",unban.getIp())
                    .replace("[points]",""+unban.getPoints())
                    .replace("[player]",""+unban.getPlayer().getColoredName())
                    .replace("[reason]",unban.getReason()),"dkbans.banbroadcast.receive");
        }
    }
    @EventHandler
    public void onWarn(BukkitNetworkPlayerWarnEvent event){
        if(config.WarnMessageEnabled){
            Warn warn = event.getWarn();
            Bukkit.broadcast(config.WarnMessage
                    .replace("[prefix]", Messages.PREFIX_BAN)
                    .replace("[message]",warn.getMessage())
                    .replace("[staff]",warn.getStaffName())
                    .replace("[id]",""+warn.getID())
                    .replace("[ip]",warn.getIp())
                    .replace("[points]",""+warn.getPoints())
                    .replace("[player]",""+warn.getPlayer().getColoredName())
                    .replace("[reason]",warn.getReason()),"dkbans.banbroadcast.receive");
        }
    }
}

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

package ch.dkrieger.bansystem.lib.player.history.entry;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.UUID;

public class Ban extends HistoryEntry {

    private long timeOut;
    private BanType banType;

    public Ban(UUID uuid, String ip, String reason, String message, long timeStamp, int id, int points, int reasonID, String staff, Document properties, long timeOut, BanType banType) {
        super(uuid, ip, reason, message, timeStamp, id, points, reasonID, staff, properties);
        this.timeOut = timeOut;
        this.banType = banType;
    }

    public long getTimeOut() {
        return timeOut;
    }
    public long getDuration(){
        return timeOut-System.currentTimeMillis();
    }
    public long getRemaining(){
        return timeOut-System.currentTimeMillis();
    }
    public BanType getBanType() {
        return banType;
    }

    @Override
    public String getTypeName() {
        return "Ban";
    }

    @Override
    public TextComponent getListMessage() {
        String message = Messages.HISTORY_LIST_BAN_NETWORK;
        if(banType == BanType.CHAT) message = Messages.HISTORY_LIST_BAN_CHAT;
        message = message.replace("[player]",getPlayer().getColoredName())
                .replace("[id]",""+getID())
                .replace("[reason]",getReason())
                .replace("[reasonID]",""+getReasonID())
                .replace("[time]",BanSystem.getInstance().getConfig().dateFormat.format(getTimeStamp()))
                .replace("[timeout]",BanSystem.getInstance().getConfig().dateFormat.format(getTimeOut()))
                .replace("[message]",getMessage())
                .replace("[type]",getTypeName())
                .replace("[staff]",getStaffName())
                .replace("[ip]",getIp())
                .replace("[points]",""+getPoints())
                .replace("[duration]",GeneralUtil.calculateDuration(getDuration()))
                .replace("[remaining]",GeneralUtil.calculateRemaining(getDuration(),false))
                .replace("[remaining-short]",GeneralUtil.calculateRemaining(getDuration(),true))
                .replace("[prefix]",Messages.PREFIX_BAN);
        return new TextComponent(message);
    }

    @Override
    public TextComponent getInfoMessage() {
        String message = Messages.HISTORY_INFO_BAN_NETWORK;
        if(banType == BanType.CHAT) message = Messages.HISTORY_INFO_BAN_CHAT;
        message = message
                .replace("[prefix]",Messages.PREFIX_BAN)
                .replace("[id]",""+getID())
                .replace("[reason]",getReason())
                .replace("[type]",getTypeName())
                .replace("[points]",""+getPoints())
                .replace("[reasonID]",""+getReasonID())
                .replace("[message]",getMessage())
                .replace("[time]",BanSystem.getInstance().getConfig().dateFormat.format(getTimeStamp()))
                .replace("[timeout]",BanSystem.getInstance().getConfig().dateFormat.format(getTimeOut()))
                .replace("[ip]",getIp())
                .replace("[staff]",getStaffName())
                .replace("[duration]",GeneralUtil.calculateDuration(getDuration()))
                .replace("[remaining]",GeneralUtil.calculateRemaining(getDuration(),false))
                .replace("[remaining-short]",GeneralUtil.calculateRemaining(getDuration(),true))
                .replace("[player]",getPlayer().getColoredName());
        return new TextComponent(message);
    }

    public TextComponent toMessage(){
        String message = Messages.BAN_MESSAGE_NETWORK_TEMPORARY;
        if(getBanType() == BanType.CHAT){
            message = Messages.BAN_MESSAGE_CHAT_TEMPORARY;
            if(getTimeOut() <= 0) message = Messages.BAN_MESSAGE_CHAT_PERMANENT;
        }else if(getTimeOut() <= 0) message = Messages.BAN_MESSAGE_NETWORK_PERMANENT;
        return new TextComponent(message
                .replace("[id]",getID()+"")
                .replace("[banid]",getID()+"")
                .replace("[staff]",getStaffName())
                .replace("[reason]",getReason())
                .replace("[message]",getMessage())
                .replace("[duration]",GeneralUtil.calculateDuration(getDuration()))
                .replace("[remaining]",GeneralUtil.calculateRemaining(getDuration(),false))
                .replace("[remaining-short]",GeneralUtil.calculateRemaining(getDuration(),true))
                .replace("[time]",BanSystem.getInstance().getConfig().dateFormat.format(getTimeStamp()))
                .replace("[timeOut]",BanSystem.getInstance().getConfig().dateFormat.format(getTimeOut()))
                .replace("[prefix]",Messages.PREFIX_NETWORK));
    }
}

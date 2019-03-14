/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 14.03.19 19:43
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
import ch.dkrieger.bansystem.lib.player.history.HistoryPoints;
import ch.dkrieger.bansystem.lib.utils.Document;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.UUID;

public class Warn extends HistoryEntry{

    private boolean kick;

    public Warn(UUID uuid, String ip, String reason, String message, long timeStamp, int id, HistoryPoints points, int reasonID, String staff, Document properties) {
        super(uuid, ip, reason, message, timeStamp, id, points, reasonID, staff, properties);
        this.kick = false;
    }

    public Warn(UUID uuid, String ip, String reason, String message, long timeStamp, int id, HistoryPoints points, int reasonID, String staff, Document properties, boolean kick) {
        super(uuid, ip, reason, message, timeStamp, id, points, reasonID, staff, properties);
        this.kick = kick;
    }

    public boolean isKick() {
        return kick;
    }

    public void setKick(boolean kick){
        this.kick = kick;
    }

    @Override
    public String getTypeName() {
        return "Warn";
    }

    @Override
    public TextComponent getListMessage() {
        return new TextComponent(replace(Messages.HISTORY_LIST_WARN));
    }
    @Override
    public TextComponent getInfoMessage() {
        return new TextComponent(replace(Messages.HISTORY_INFO_KICK));
    }
    public TextComponent toChatMessage(){
        return new TextComponent(replace(Messages.WARN_CHAT_MESSAGE));

    }
    public TextComponent toKickMessage(){
        return new TextComponent(replace(Messages.WARN_KICK_MESSAGE));

    }
    public String replace(String message){
        return message
                .replace("[prefix]",Messages.PREFIX_BAN)
                .replace("[id]",""+getID())
                .replace("[reason]",getReason())
                .replace("[type]",getTypeName())
                .replace("[points]",""+getPoints().getPoints())
                .replace("[pointsType]",getPoints().getHistoryType().getDisplay())
                .replace("[reasonID]",""+getReasonID())
                .replace("[message]",getMessage())
                .replace("[time]", BanSystem.getInstance().getConfig().dateFormat.format(getTimeStamp()))
                .replace("[ip]",getIp())
                .replace("[staff]",getStaffName())
                .replace("[player]",getPlayer().getColoredName());
    }
}

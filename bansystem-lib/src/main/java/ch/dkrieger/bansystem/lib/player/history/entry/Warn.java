/*
 * (C) Copyright 2018 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 30.12.18 19:42
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

    public Warn(UUID uuid, String ip, String reason, String message, long timeStamp, int id, HistoryPoints points, int reasonID, String staff, Document properties) {
        super(uuid, ip, reason, message, timeStamp, id, points, reasonID, staff, properties);
    }

    @Override
    public String getTypeName() {
        return "Warn";
    }

    @Override
    public TextComponent getListMessage() {
        return new TextComponent(Messages.HISTORY_LIST_WARN
                .replace("[player]",getPlayer().getColoredName())
                .replace("[id]",""+getID())
                .replace("[reason]",getReason())
                .replace("[reasonID]",""+getReasonID())
                .replace("[message]",getMessage())
                .replace("[type]",getTypeName())
                .replace("[staff]",getStaffName())
                .replace("[points]",""+getPoints())
                .replace("[time]", BanSystem.getInstance().getConfig().dateFormat.format(getTimeStamp()))
                .replace("[prefix]",Messages.PREFIX_BAN));
    }

    @Override
    public TextComponent getInfoMessage() {
        return new TextComponent(Messages.HISTORY_INFO_KICK
                .replace("[prefix]",Messages.PREFIX_BAN)
                .replace("[id]",""+getID())
                .replace("[reason]",getReason())
                .replace("[type]",getTypeName())
                .replace("[points]",""+getPoints())
                .replace("[reasonID]",""+getReasonID())
                .replace("[message]",getMessage())
                .replace("[time]", BanSystem.getInstance().getConfig().dateFormat.format(getTimeStamp()))
                .replace("[ip]",getIp())
                .replace("[staff]",getStaffName())
                .replace("[player]",getPlayer().getColoredName()));
    }
    public TextComponent toMessage(){
        return new TextComponent(Messages.WARN_MESSAGE
                .replace("[prefix]",Messages.PREFIX_BAN)
                .replace("[id]",""+getID())
                .replace("[reason]",getReason())
                .replace("[type]",getTypeName())
                .replace("[points]",""+getPoints())
                .replace("[reasonID]",""+getReasonID())
                .replace("[message]",getMessage())
                .replace("[time]", BanSystem.getInstance().getConfig().dateFormat.format(getTimeStamp()))
                .replace("[ip]",getIp())
                .replace("[staff]",getStaffName())
                .replace("[player]",getPlayer().getColoredName()));
    }
}

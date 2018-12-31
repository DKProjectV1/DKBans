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
import net.md_5.bungee.api.chat.TextComponent;

import java.util.UUID;


public class Unban extends HistoryEntry {

    private BanType banType;

    public Unban(UUID uuid, String ip, String reason, String message, long timeStamp, int id, int points, int reasonID, String staff, Document properties, BanType type) {
        super(uuid, ip, reason, message, timeStamp, id, points, reasonID, staff, properties);
        this.banType = type;
    }

    public BanType getBanType() {
        if(banType == null) return BanType.NETWORK;
        return banType;
    }

    @Override
    public TextComponent getListMessage() {
        System.out.println(getBanType());
        return new TextComponent(Messages.HISTORY_LIST_UNBAN
                .replace("[player]",getPlayer().getColoredName())
                .replace("[id]",""+getID())
                .replace("[reason]",getReason())
                .replace("[reasonID]",""+getReasonID())
                .replace("[time]", BanSystem.getInstance().getConfig().dateFormat.format(getTimeStamp()))
                .replace("[timeout]",BanSystem.getInstance().getConfig().dateFormat.format(getTimeStamp()))
                .replace("[message]",getMessage())
                .replace("[type]",getTypeName())
                .replace("[staff]",getStaffName())
                .replace("[banType]",getBanType().getDisplay())
                .replace("[points]",""+getPoints())
                .replace("[prefix]",Messages.PREFIX_BAN));
    }

    @Override
    public TextComponent getInfoMessage() {
        return new TextComponent(Messages.HISTORY_INFO_UNBAN
                .replace("[prefix]",Messages.PREFIX_BAN)
                .replace("[id]",""+getID())
                .replace("[reason]",getReason())
                .replace("[type]",getTypeName())
                .replace("[points]",""+getPoints())
                .replace("[reasonID]",""+getReasonID())
                .replace("[message]",getMessage())
                .replace("[time]",BanSystem.getInstance().getConfig().dateFormat.format(getTimeStamp()))
                .replace("[ip]",getIp())
                .replace("[banType]",getBanType().getDisplay())
                .replace("[staff]",getStaffName())
                .replace("[player]",getPlayer().getColoredName()));
    }

    @Override
    public String getTypeName() {
        return "Unban";
    }
}

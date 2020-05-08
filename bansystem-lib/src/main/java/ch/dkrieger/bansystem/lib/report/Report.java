/*
 * (C) Copyright 2020 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 08.05.20, 19:58
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

package ch.dkrieger.bansystem.lib.report;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.UUID;

public class Report {

    private UUID uuid, staff, reporter;
    private String reason, message, reportedServer;
    private long timeStamp;
    private int reasonID;

    public Report(UUID uuid, UUID staff, UUID reporter, String reason, String message, String reportedServer, long timeStamp, int reasonID) {
        this.uuid = uuid;
        this.staff = staff;
        this.reporter = reporter;
        this.reason = reason;
        this.message = message;
        this.reportedServer = reportedServer;
        this.timeStamp = timeStamp;
        this.reasonID = reasonID;
    }

    public UUID getUUID() {
        return uuid;
    }

    public UUID getStaff() {
        return staff;
    }

    public UUID getReporterUUID() {
        return reporter;
    }
    public String getReason() {
        return ChatColor.translateAlternateColorCodes('&',reason);
    }

    public String getRwaReason() {
        return reason;
    }

    public String getMessage() {
        return message;
    }

    public String getReportedServer() {
        return reportedServer;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public int getReasonID() {
        return reasonID;
    }

    public NetworkPlayer getPlayer(){
        return BanSystem.getInstance().getPlayerManager().getPlayer(this.uuid);
    }

    public NetworkPlayer getReporter(){
        return BanSystem.getInstance().getPlayerManager().getPlayer(this.reporter);
    }

    public void setStaff(UUID staff){
        this.staff = staff;
    }

    public TextComponent toMessage(){
        TextComponent component = new TextComponent(Messages.REPORT_MESSAGE_TEXT
                .replace("[prefix]",Messages.PREFIX_REPORT)
                .replace("[reason]",getReason())
                .replace("[server]",getReportedServer())
                .replace("[message]",getMessage())
                .replace("[time]",BanSystem.getInstance().getConfig().dateFormat.format(getTimeStamp()))
                .replace("[reporter]",getReporter().getColoredName())
                .replace("[player]",getPlayer().getColoredName()));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder(Messages.REPORT_MESSAGE_HOVER
                .replace("[prefix]",Messages.PREFIX_REPORT)
                .replace("[reason]",getReason())
                .replace("[server]",getReportedServer())
                .replace("[message]",getMessage())
                .replace("[time]",BanSystem.getInstance().getConfig().dateFormat.format(getTimeStamp()))
                .replace("[reporter]",getReporter().getColoredName())
                .replace("[player]",getPlayer().getColoredName())).create()));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/report take "+getUUID()));
        return component;
    }
}

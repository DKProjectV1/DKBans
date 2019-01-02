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

package ch.dkrieger.bansystem.lib.command.defaults;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.stats.NetworkStats;

import java.util.List;

public class NetworkStatsCommand extends NetworkCommand {

    public NetworkStatsCommand() {
        super("networkstats","","dkbans.networkstats");
        setPrefix(Messages.PREFIX_NETWORK);
    }

    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        NetworkStats stats = BanSystem.getInstance().getNetworkStats();
        NetworkStats syncStats = BanSystem.getInstance().getTempSyncStats();
        sender.sendMessage(Messages.NETWORK_STATS
                .replace("[registeredPlayers]",""+BanSystem.getInstance().getPlayerManager().getRegisteredCount())
                .replace("[onlinePlayers]",""+BanSystem.getInstance().getPlayerManager().getOnlineCount())
                .replace("[bans]",""+(stats.getBans()+syncStats.getBans()))
                .replace("[mutes]",""+(stats.getMutes()+syncStats.getMutes()))
                .replace("[unbans]",""+(stats.getUnbans()+syncStats.getUnbans()))
                .replace("[kicks]",""+(stats.getKicks()+syncStats.getKicks()))
                .replace("[reports]",""+(stats.getReports()+syncStats.getReports()))
                .replace("[reportsAccepted]",""+(stats.getReportsAccepted()+syncStats.getReportsAccepted()))
                .replace("[reportsDenied]",""+(stats.getReportsDenied()+syncStats.getReportsDenied()))
                .replace("[logins]",""+(stats.getLogins()+syncStats.getLogins()))
                .replace("[messages]",""+(stats.getMessages()+syncStats.getMessages()))
                .replace("[warns]",""+(stats.getWarns()+syncStats.getWarns()))
                .replace("[prefix]",getPrefix()));
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}

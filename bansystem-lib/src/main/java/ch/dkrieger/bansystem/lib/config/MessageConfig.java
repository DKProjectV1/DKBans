package ch.dkrieger.bansystem.lib.config;

import ch.dkrieger.bansystem.lib.DKBansPlatform;
import ch.dkrieger.bansystem.lib.Messages;

import java.io.File;

public class MessageConfig extends SimpleConfig{

    public MessageConfig(DKBansPlatform platform) {
        super(new File(platform.getFolder(),"messages.yml"));
    }
    @Override
    public void onLoad() {
        Messages.PREFIX_NETWORK = addAndGetMessageValue("prefix.network","&8» &4DKNetwork &8| &f");
        Messages.PREFIX_BAN = addAndGetMessageValue("prefix.ban","&8» &4DKBans &8| &f");
        Messages.PREFIX_REPORT = addAndGetMessageValue("prefix.report","&8» &6DKReport &8| &f");
        Messages.PREFIX_TEAMCHAT = addAndGetMessageValue("prefix.teamchat","&8» &eTeamChat &8| &f");
        Messages.PREFIX_CHAT = addAndGetMessageValue("prefix.chat","&8» &eChat &8| &f");
        Messages.PREFIX_CHATLOG = addAndGetMessageValue("prefix.chatlog","&8» &eChatLog &8| &f");

        Messages.ERROR = addAndGetMessageValue("error","[prefix]&cAn error occurred, please contact a network administrator.");
        Messages.NOPERMISSIONS = addAndGetMessageValue("nopermissions","[prefix]&cYou don't have permission for this command.");
        Messages.HELP = addAndGetMessageValue("help","[prefix]&6Information\n&8» &e/hub &8| &7Connect to the Hub-Server" +
                "\n&8» &e/report &8| &7Report a player\n&7&8\n&8» &eTeamSpeak&8: &bts.example.net\n&8» &eForum&8: &bforum.example.net\n&8» &eShop&8: &bshop.example.net");

        Messages.TIME_DAY_SINGULAR = addAndGetMessageValue("time.day.singular","Day");
        Messages.TIME_DAY_PLURAL = addAndGetMessageValue("time.day.plural","Days");
        Messages.TIME_DAY_SHORTCUT = addAndGetMessageValue("time.day.shortcut","D");

        Messages.TIME_HOUR_SINGULAR = addAndGetMessageValue("time.hour.singular","Hour");
        Messages.TIME_HOUR_PLURAL = addAndGetMessageValue("time.hour.plural","Hours");
        Messages.TIME_HOUR_SHORTCUT = addAndGetMessageValue("time.hour.shortcut","H");

        Messages.TIME_MINUTE_SINGULAR = addAndGetMessageValue("time.minute.singular","Minute");
        Messages.TIME_MINUTE_PLURAL = addAndGetMessageValue("time.minute.plural","Minutes");
        Messages.TIME_MINUTE_SHORTCUT = addAndGetMessageValue("time.minute.shortcuts","M");

        Messages.TIME_SECOND_SINGULAR = addAndGetMessageValue("time.second.singular","Second");
        Messages.TIME_SECOND_PLURAL = addAndGetMessageValue("time.second.plural","Seconds");
        Messages.TIME_SECOND_SHORTCUT = addAndGetMessageValue("time.second.shortcut","S");

        Messages.TIME_PERMANENTLY_NORMAL = addAndGetMessageValue("time.permanently.normal","Permanently");
        Messages.TIME_PERMANENTLY_SHORTCUT = addAndGetMessageValue("time.permanently.shortcut","Perma");

        Messages.TIME_FINISH = addAndGetMessageValue("time.finish","-");

        Messages.SERVER_NOT_FOUND = addAndGetMessageValue("server.notfound","[prefix]&cThe server &9[server] &cwas not found.");
        Messages.SERVER_ALREADY = addAndGetMessageValue("server.already","[prefix]&cYou are already connected to this server.");
        Messages.SERVER_CONNECTING = addAndGetMessageValue("server.connecting","[prefix]&7Connecting to server...");

        Messages.PLAYER_NOT_FOUND = addAndGetMessageValue("player.not.found","[prefix]&cThe player was not found.");
        Messages.PLAYER_NOT_ONLINE = addAndGetMessageValue("player.not.online","[prefix]&cThis player is not online.");
        Messages.PLAYER_NOT_BANNED = addAndGetMessageValue("player.not.banned","[prefix]&8[player] &cis not banned.");
        Messages.PLAYER_ALREADY_BANNED = addAndGetMessageValue("player.already.banned","[prefix]&8[player] &cis already banned.");
        Messages.PLAYER_ALREADY_MUTED = addAndGetMessageValue("player.already.muted","[prefix]&8[player] &cis already muted.");
        Messages.PLAYER_ALREADY_REPORTED = addAndGetMessageValue("player.already.reported","[prefix]&cYou have &8[player] &calready reported.");
        Messages.PLAYER_HAS_MOREBANS_HEADER = addAndGetMessageValue("player.has.morebans.header","[prefix]&8[player] &7has a chat and network ban:");
        Messages.PLAYER_HAS_MOREBANS_NETWORK = addAndGetMessageValue("player.morebans.network","&8» &cBan &8- &4[reason] &8| &7[remaining-short]");
        Messages.PLAYER_HAS_MOREBANS_CHAT = addAndGetMessageValue("player.has.morebans.chat","&8» &6Mute &8- &4[reason] &8| &7[remaining-short]");
        Messages.PLAYER_UNMUTED = addAndGetMessageValue("player.unmuted","[prefix]&7The player &8[player] &7was unmuted.");
        Messages.PLAYER_UNBANNED = addAndGetMessageValue("player.unbanned","[prefix]&7The player &8[player] &7was unbanned.");

        Messages.PLAYER_INFO_HELP = addAndGetMessageValue("player.info.help","[prefix]&cUsage&8: &7 /playerinfo <player>");
        Messages.PLAYER_INFO_ONLINE = addAndGetMessageValue("player.info.online","&7\n&8» &7Name&8: &c[player] &8(&aOnline&8)&7\n&8» &7UUID&8: &c[uuid]" +
                "\n&8» &7ID&8: &c[id]\n&8» &7FirstLogin&8: &c[firstLogin]\n&8» &7Server&8: &c[server]\n&8» &7onlineTime&8: &c[onlineTime-short]" +
                "\n&8» &7IP&8: &c[ip]\n&8» &7Country&8: &c[country]\n&8» &7Banned&8: &c[isBanned]\n&8» &7Muted&8: &c[isMuted]" +
                "\n&8» &7Bans&8: &c[bans]\n&8» &7Mutes&8: &c[mutes]\n&8» &7Logins&8: &c[logins]\n&8» &7Reports received&8: &c[reportsReceived]" +
                "\n&8» &7Reports send&8: &a[reportsAccepted]&8/&c[reportsSent]\n&5\n&8» [history] [sessions] [ips]\n&7");
        Messages.PLAYER_INFO_OFFLINE = addAndGetMessageValue("player.info.offline","&7\n&8» &7Name&8: &c[player] &8(&cOffline&8)&7\n&8» &7UUID&8: &c[uuid]" +
                "\n&8» &7ID&8: &c[id]\n&8» &7FirstLogin&8: &c[firstLogin]\n&8» &7LastLogin&8: &c[lastLogin]\n&8» &7onlineTime&8: &c[onlineTime]" +
                "\n&8» &7IP&8: &c[ip]\n&8» &7Country&8: &c[country]\n&8» &7Banned&8: &c[isBanned]\n&8» &7Muted&8: &c[isMuted]" +
                "\n&8» &7Bans&8: &c[bans]\n&8» &7Mutes&8: &c[mutes]\n&8» &7Logins&8: &c[logins]\n&8» &7Reports received&8: &c[reportsReceived]" +
                "\n&8» &7Reports send&8: &a[reportsAccepted]&8/&c[reportsSent]\n&5\n&8» [history] [sessions] [ips]\n&7");
        Messages.PLAYER_INFO_HISTORY = addAndGetMessageValue("player.info.history","&8[&bHistory&8]");
        Messages.PLAYER_INFO_SESSIONS = addAndGetMessageValue("player.info.sessions","&8[&aSessions&8]");
        Messages.PLAYER_INFO_IPS = addAndGetMessageValue("player.info.ips","&8[&eIps&8]");
        Messages.PLAYER_INFO_SESSIONS_HEADER = addAndGetMessageValue("player.info.session.header","[prefix]&7Online sessions from [player]");
        Messages.PLAYER_INFO_SESSIONS_LIST = addAndGetMessageValue("player.info.session.list","&8» &7[connected] &8- &7[disconnected] &8| &c[duration]");

        Messages.STAFF_STATUS_NOW = addAndGetMessageValue("staff.status.now","[prefix]&7You are [status]");
        Messages.STAFF_STATUS_ALREADY = addAndGetMessageValue("staff.status.already","[prefix]&7You are already [status]");
        Messages.STAFF_STATUS_CHANGE = addAndGetMessageValue("staff.status.changed","[prefix]&7You have [status]");
        Messages.STAFF_STATUS_NOT = addAndGetMessageValue("staff.status.not","[prefix]&7You are not [status]");
        Messages.STAFF_STATUS_LOGIN = addAndGetMessageValue("staff.status.login","&alogged in");
        Messages.STAFF_STATUS_LOGOUT = addAndGetMessageValue("staff.status.logout","&clogged out");

        Messages.REASON_NOT_FOUND = addAndGetMessageValue("reason.notfound","[prefix]&cThis reason was not found.");
        Messages.REASON_NO_PERMISSION = addAndGetMessageValue("prefix.network","[prefix]&cYou don't have permission for this reason.");

        Messages.BAN_HELP_HEADER = addAndGetMessageValue("ban.help.header","[prefix]&6Ban Administration");
        Messages.BAN_HELP_REASON = addAndGetMessageValue("ban.help.reason","&8- &c[id] &8| &c[reason] &8» &4&l[banType]");
        Messages.BAN_TYPE_NETWORK = addAndGetMessageValue("ban.type.network","Network");
        Messages.BAN_TYPE_CHAT= addAndGetMessageValue("ban.type.chat","Chat");
        Messages.BAN_HELP_HELP = addAndGetMessageValue("ban.help.help","&8» &cUsage&8: &7 /ban <player> <reason> {message}");
        Messages.BAN_SELF = addAndGetMessageValue("ban.self","[prefix]&cYou can not ban your self.");
        Messages.BAN_BYPASS = addAndGetMessageValue("ban.bypass","[prefix]&cYou can not ban &7[player]&c.");
        Messages.BAN_SUCCESS = addAndGetMessageValue("ban.success","[prefix]&8[player] &7was banned for [reason]&7.");
        Messages.BAN_OVERWRITE_INFO = addAndGetMessageValue("ban.overwrite.info","[prefix]&7Do you want override it? &8[&7Click&8]");
        Messages.BAN_NOTFOUND = addAndGetMessageValue("ban.notfound","[prefix]&cThis ban was not found.");

        Messages.BAN_OVERWRITE_NOTALLOWED = addAndGetMessageValue("ban.overwrite.notallowed","[prefix]&cYou are not allowed to overwrite this ban.");

        Messages.BAN_MESSAGE_NETWORK_TEMPORARY = addAndGetMessageValue("ban.message.network.temporary","&e&lexample.net\n&5\n&cYou are &e&l[duration] &cbanned from this network\n&3Reason&8: &4&l[reason]\n&3BanID&8: &7[banid]\n&e\n&3Remaining time&8: &e[remaining]\n&5\n&aYou can make a delivery request at &eforum.example.net");
        Messages.BAN_MESSAGE_NETWORK_PERMANENT = addAndGetMessageValue("ban.message.network.permanent","&e&lexample.net\n&5&6&7&8\n&cYou are &4&lPermanently banned &cfrom this network\n&3Reason&8: &4&l[reason]\n&3BanID&8: &7[banid]\n&5\n&aYou can make a delivery request at &eforum.example.net.");
        Messages.BAN_MESSAGE_CHAT_TEMPORARY = addAndGetMessageValue("ban.message.chat.temporary","&5\n[prefix]&cYou are &e&l[duration] &cbanned from the chat\n&8» &3Reason&8: &4&l[reason]\n&8» &3BanID&8: &7[banid]\n&e\n&8» &3Remaining time&8: &e[remaining]\n&7");
        Messages.BAN_MESSAGE_CHAT_PERMANENT = addAndGetMessageValue("ban.message.chat.permanent","&5\n[prefix]&cYou are &4&lPermanent &cbanned from the chat\n&8» &3Reason&8: &4&l[reason]\n&8» &3BanID&8: &7[banid]\n&5");

        Messages.KICK_HELP_HEADER = addAndGetMessageValue("kick.help.header","[prefix]&6Kick Administration");
        Messages.KICK_HELP_REASON = addAndGetMessageValue("kick.help.reason"," &8- &c[id] &8| &c[reason]");
        Messages.KICK_HELP_HELP = addAndGetMessageValue("kick.help.help","&8» &cUsage&8: &7 /kick <player> <reason> {message}");
        Messages.KICK_SELF = addAndGetMessageValue("kick.self","[prefix]&cYou can not kick your self.");
        Messages.KICK_BYPASS = addAndGetMessageValue("kick.bypass","[prefix]&cYou can not kick &8[player]&c.");
        Messages.KICK_SUCCESS = addAndGetMessageValue("kick.success","[prefix]&8[player] &7was kicked for &4[reason]&7.");
        Messages.KICK_MESSAGE= addAndGetMessageValue("kick.message","&e&lexample.net\n&5&6&7&8\n&cYou were kicked from the network\n&3Reason&8: &4&l[reason]\n");

        Messages.PING_SELF = addAndGetMessageValue("ping.self","[prefix]&7Your ping is &a[ping]&cms");
        Messages.PING_OTHER = addAndGetMessageValue("ping.other","[prefix]&7The ping from [player] &7is &a[ping]&cms");

        Messages.BROADCAST_HELP = addAndGetMessageValue("broadcast.help","[prefix]&7Broadcast help\n" +
                "&8» &e/bc reload &8| &7Reload all broadcast\n&8» &e/bc list &8| &7List all broadcasts" +
                "\n&8» &e/bc direct <message> &8| &7Send a direct message\n" +
                "&8» &e/bc create <message> &8| &7Create a broadcast\n&8» &e/bc <id> info &8| &7Show ths broadcast\n&8» &e/bc <id> send &8| &7Send this broadcast\n" +
                "&8» &e/bc <id> delete &8| &7Delete a broadcast\n&8» &e/bc <id> setHover <type> <message> &8| &7Set a hover message" +
                "\n&8» &e/bc <id> setClick <type> <message> &8| &7Set a click to a message\n" +
                "&8» &e/bc <id> setMessage <message> &8| &7Change the message\n&8» &e/bc <id> addMessage <message> &8| &7Add a message\n" +
                "&8» &e/bc <id> setPermission <permission> &8| &7Set a permission\n&8» &e/bc <id> setAuto <true/false> &8| &7Set a broadcast to auto");
        Messages.BROADCAST_RELOADED = addAndGetMessageValue("broadcast.reloaded","[prefix]&7Reloaded all broadcasts");
        Messages.BROADCAST_CREATED = addAndGetMessageValue("broadcast.created","[prefix]&7Create broadcast &9[id] &7with the message &9[message]");
        Messages.BROADCAST_DELETED = addAndGetMessageValue("broadcast.deleted","[prefix]&7Deleted broadcast ");
        Messages.BROADCAST_LIST_HEADER = addAndGetMessageValue("broadcast.list.header","[prefix]&7Available broadcasts");
        Messages.BROADCAST_LIST_LIST = addAndGetMessageValue("broadcast.list.list","&8» &7[id] &8| &7[message]");
        Messages.BROADCAST_INFO = addAndGetMessageValue("broadcast.info","[prefix]&7Broadcast &c[id]" +
                "\n&8» &7ID&8: &c[id]\n&8» &7Auto&8: &c[auto]\n&8» &7Message&8: &c[message]\n&8» &7Hover&8: &c[hover]\n&8» &7Click&8: &c[clickType] &8| &c[clickMessage]");
        Messages.BROADCAST_CHANGED_CLICK = addAndGetMessageValue("broadcast.changed.clicked","[prefix]&7Changed click from broadcast &c[id] &7to &c[clickType] &8| &c[clickMessage]");
        Messages.BROADCAST_CHANGED_HOVER = addAndGetMessageValue("broadcast.changed.hover","[prefix]&7Changed hover from broadcast &c[id] &7to &c[hover]");
        Messages.BROADCAST_CHANGED_MESSAGE = addAndGetMessageValue("broadcast.changed.message","[prefix]&7Changed message from broadcast &c[id] &7to &c[message]");
        Messages.BROADCAST_CHANGED_PERMISSION = addAndGetMessageValue("broadcast.changed.PERMISSION","[prefix]&7Changed permission from broadcast &c[id] &7to &c[permission]");
        Messages.BROADCAST_CHANGED_AUTO_DISABLED = addAndGetMessageValue("broadcast.changed.auto.disabled","[prefix]&7Disabled auto sending for broadcast &c[id]");
        Messages.BROADCAST_CHANGED_AUTO_ENABLED = addAndGetMessageValue("broadcast.changed.auto.enabled","[prefix]&7Enabled auto sending for broadcast &c[id]");
        Messages.BROADCAST_NOTFOUND_CLICKTYPE = addAndGetMessageValue("broadcast.notfound.clicktype","[prefix]&cThis click type was not found, use:" +
                "\n&8» &7Url\n&8» &7Command\n&8» &7OpenChat");
        Messages.BROADCAST_NOTFOUND_BROADCAST = addAndGetMessageValue("broadcast.notfound.broadcast","[prefix]&cThe broadcast with the with the id &c[id] &cwas not found.");
        Messages.BROADCAST_FORMAT_DIRECT = addAndGetMessageValue("broadcast.format.direct","&8\n[prefix]&7[message]\n&8&8&8");
        Messages.BROADCAST_FORMAT_SEND = addAndGetMessageValue("broadcast.format.send","&8\n[prefix][message]\n&8&8&8");

        Messages.JUMPTO_HELP = addAndGetMessageValue("jumpto.help","[prefix]&cUsage&8: &7 /jumpto <player>");

        Messages.JOINME_COOLDOWN = addAndGetMessageValue("joinme.cooldown","[prefix]&cWait a moment before sending the next joinme.");
        Messages.JOINME_NOTFOUND = addAndGetMessageValue("joinme.notfound","[prefix]&cThis Joinme was not found.");
        Messages.JOINME_NOTALLOWEDONSERVER = addAndGetMessageValue("joinme.notallowedonserver","[prefix]&cYou can not send a joinme on this server.");
        Messages.JOINME_LINE1 = addAndGetMessageValue("joinme.line.1","&6");
        Messages.JOINME_LINE2 = addAndGetMessageValue("joinme.line.2","&6");
        Messages.JOINME_LINE3 = addAndGetMessageValue("joinme.line.3","&6");
        Messages.JOINME_LINE4 = addAndGetMessageValue("joinme.line.4","  [player] &7is playing on &e[server]");
        Messages.JOINME_LINE5= addAndGetMessageValue("joinme.line.5","           &7Click to join");
        Messages.JOINME_LINE6 = addAndGetMessageValue("joinme.line.6","&6");
        Messages.JOINME_LINE7 = addAndGetMessageValue("joinme.line.7","&6");
        Messages.JOINME_LINE8 = addAndGetMessageValue("joinme.line.8","&6");

        Messages.TEAMCHAT_HELP = addAndGetMessageValue("teamchat.help","[prefix]&cUsage&8: &7 /teamchat <message>");
        Messages.TEAMCHAT_MESSAGE_COLOR = addAndGetMessageValue("teamchat.message.color","&7");
        Messages.TEAMCHAT_MESSAGE_FORMAT = addAndGetMessageValue("teamchat.message.format","[prefix]&8[player] &8» &7[message]");

        Messages.TEMPBAN_HELP = addAndGetMessageValue("tempban.help","[prefix]&cUsage&8: &7 /tempban <player> <reason> &7<time> &7{unit}");

        Messages.TEMPMUTE_HELP = addAndGetMessageValue("tempmute.help","[prefix]&cUsage&8: &7 /tempmute <player> <reason> &7<time> &7{unit}");

        Messages.HISTORY_HELP = addAndGetMessageValue("history.help","[prefix]&cUsage&8: &7 /history <player> {id}");
        Messages.HISTORY_NOTFOUND = addAndGetMessageValue("history.notfound","[prefix]&cThe history was not found.");
        Messages.HISTORY_LIST_HEADER = addAndGetMessageValue("history.list.header","[prefix]&7History from &8[player]");
        Messages.HISTORY_LIST_BAN_CHAT = addAndGetMessageValue("history.list.ban.chat","&8» &6Mute &8| &7[time] - &7[reason]");
        Messages.HISTORY_LIST_BAN_NETWORK = addAndGetMessageValue("history.list.ban.network","&8» &cBan &8| &7[time] - &7[reason]");
        Messages.HISTORY_LIST_KICK = addAndGetMessageValue("history.list.kick","&8» &eKick &8| &7[time] - &7[reason]");
        Messages.HISTORY_LIST_UNBAN = addAndGetMessageValue("history.list.unban","&8» &aUnban &8| &7[time] - &7[reason]");
        Messages.HISTORY_INFO_BAN_NETWORK = addAndGetMessageValue("history.info.ban.network","[prefix]&7Ban &c[id] &7von [player]" +
                "\n&8» &7ID&8: &c[id]\n&8» &7Reason&8: [reason] &8- &7[reasonID]\n&8» &7Message&8: &c[message]\n&8» &7Staff&8: &c[staff]" +
                "\n&8» &7Points&8: &c[points]\n&8» &7Duration&8: &c[duration]\n&8» &7Remaining&8: &c[remaining-short]\n&8» &7Time&8: &c[time]\n&8» &7TimeOut&8: &c[timeout]");
        Messages.HISTORY_INFO_BAN_CHAT = addAndGetMessageValue("history.info.ban.chat","[prefix]&7Mute &c[id] &7von [player]" +
                "\n&8» &7ID&8: &c[id]\n&8» &7Reason&8: [reason] &8- &7[reasonID]\n&8» &7Message&8: &c[message]\n&8» &7Staff&8: &c[staff]" +
                "\n&8» &7Points&8: &c[points]\n&8» &7Duration&8: &c[duration]\n&8» &7Remaining&8: &c[remaining-short]\n\n&8» &7Time&8: &c[time]\n&8» &7TimeOut&8: &c[timeout]");
        Messages.HISTORY_INFO_KICK = addAndGetMessageValue("history.info.kick","[prefix]&7Kick &c[id] &7von [player]" +
                "\n&8» &7ID&8: &c[id]\n&8» &7Reason&8: [reason] &8- &7[reasonID]\n&8» &7Message&8: &c[message]\n&8» &7Staff&8: &c[staff]" +
                "\n&8» &7Points&8: &c[points]\n&8» &7Time&8: &c[time]");
        Messages.HISTORY_INFO_UNBAN = addAndGetMessageValue("history.info.unban","[prefix]&7Unban &c[id] &7von [player]" +
                "\n&8» &7ID&8: &c[id]\n&8» &7For type&8: &c[banType]\n&8» &7Reason&8: [reason] &8- &7[reasonID]\n&8» &7Message&8: &c[message]\n&8» &7Staff&8: &c[staff]" +
                "\n&8» &7Points&8: &c[points]\n&8» &7Time&8: &c[time]");
        Messages.HISTORY_RESET_ALL = addAndGetMessageValue("history.reset.all","[prefix]&7The history from &8[player] &7was reset.");
        Messages.HISTORY_RESET_ONE = addAndGetMessageValue("history.reset.one","[prefix]&7The history entry &9[id] from [player] &7was reset.");

        Messages.ONLINE_TIME = addAndGetMessageValue("onlinetime","[prefix]&7Your online time is &c[time-short]");

        Messages.UNBAN_HELP_HEADER = addAndGetMessageValue("unban.help.header","[prefix]&6Unban Administration");
        Messages.UNBAN_HELP_HELP = addAndGetMessageValue("unban.help.help","[prefix]&cUsage&8: &7 /unban <player> <reason> ");
        Messages.UNBAN_HELP_REASON = addAndGetMessageValue("unban.help.reason"," &8- &c[id] &8| &c[reason]");
        Messages.UNBAN_NOTALLOWED = addAndGetMessageValue("unban.notallowed","[prefix]&cYou are not allowed to unban this player.");

        Messages.REPORT_HELP_HEADER = addAndGetMessageValue("report.help.header","[prefix]&6Report Administration");
        Messages.REPORT_HELP_REASON = addAndGetMessageValue("report.help.reason","&8- &c[id] &8| &c[reason]");
        Messages.REPORT_HELP_HELP = addAndGetMessageValue("report.help.help","[prefix]&cUsage&8: &7 /report <player> <reason> ");
        Messages.REPORT_SELF = addAndGetMessageValue("report.self","[prefix]&cYou can not report your self.");
        Messages.REPORT_SUCCESS = addAndGetMessageValue("report.success","[prefix]&7The player &8[player] &7was reported, thanks. ");
        Messages.REPORT_BYPASS = addAndGetMessageValue("report.bypass","[prefix]&cYou can't report his player.");
        Messages.REPORT_ACCEPTED = addAndGetMessageValue("report.accepted","[prefix]&aThe report for &8[player] &awas accepted, thanks &afor you cooperation.");
        Messages.REPORT_OTHERREASON = addAndGetMessageValue("report.other","[prefix]&7Click on a reason.");
        Messages.REPORT_DENIED_STAFF = addAndGetMessageValue("report.denied.staff","[prefix]&7You denied the report of &8[player]&7.");
        Messages.REPORT_DENIED_USER = addAndGetMessageValue("report.denied.user","[prefix]&cThe report of &8[player] &cwas denied.");
        Messages.REPORT_NOTFOUND = addAndGetMessageValue("report.notfound","[prefix]&cThe report was not found.");
        Messages.REPORT_PROCESS = addAndGetMessageValue("report.process.take","[prefix]&7You are now taking care of the player &8[player]\n&8» &7Reporter&8: &c[reporter]\n&8» &7Reason&8: &c[reason]\n&8» &7Message&8: &c[message]\n&8» &7Server&8: &c[server]\n&7");
        Messages.REPORT_LEAVED_STAFF = addAndGetMessageValue("report.leaved.staff","[prefix]&8[player] &cleaved the server.");
        Messages.REPORT_LEAVED_USER = addAndGetMessageValue("report.leaved.user","[prefix]&cThe report of [player] &cwas denied, because the &cplayer &cleaved the server.");
        Messages.REPORT_LIST_NO = addAndGetMessageValue("report.list.no","[prefix]&cNo reports found.");
        Messages.REPORT_LIST_HEADER = addAndGetMessageValue("report.list.header","[prefix]&7Open reports Page &c[page]&8/&c[maxPage]");
        Messages.REPORT_LIST_LIST = addAndGetMessageValue("report.list.list","&8» &8[player] &8&l>> &e[reason] &8[&7Click&8]");
        Messages.REPORT_INFO = addAndGetMessageValue("report.info","[prefix]&7At this moments are &9[size] &7Reports open.");
        Messages.REPORT_PROCESS_CONTROL_MESSAGE = addAndGetMessageValue("report.process.control.message","&8» [deny] [forReason] [otherReason]\n&7");//[deny] [forReason] [otherReason]
        Messages.REPORT_PROCESS_CONTROL_DENY = addAndGetMessageValue("report.process.control.deny","&8[&c&lDeny&8]");
        Messages.REPORT_PROCESS_CONTROL_FORREASON = addAndGetMessageValue("report.process.accept","&8[&a&lAccept&8]");
        Messages.REPORT_PROCESS_CONTROL_OTHERREASON = addAndGetMessageValue("report.process.otherreason","&8[&c&lOther Reason&8]");
        Messages.REPORT_MESSAGE_TEXT = addAndGetMessageValue("report.message.text","[prefix]&e[player] &8&l>> &e[reason] &8[&7Click&8]");
        Messages.REPORT_MESSAGE_HOVER = addAndGetMessageValue("report.message.hover","&8» &7Player&8: &c[player]\n&8» &7Reporter&8: &c[reporter]\n&8» &7Reason&8: &c[reason]\n&8» &7Message&8: &c[message]\n&8» &7Server&8: &c[server]");

        Messages.CHAT_PLUGIN = addAndGetMessageValue("chat.plugin","&fPlugins(1): &aDKBans");
        Messages.CHAT_FILTER_SPAM_REPEAT = addAndGetMessageValue("chat.filter.spam.repeat","[prefix]&cYou are repeating yourself.");
        Messages.CHAT_FILTER_SPAM_TOFAST = addAndGetMessageValue("chat.filter.spam.tofast","[prefix]&cYou are chatting to fast.");
        Messages.CHAT_FILTER_MESSAGE = addAndGetMessageValue("chat.filter.spam.message","[prefix]&cPlease pay attention to your choice of words.");
        Messages.CHAT_FILTER_PROMOTION = addAndGetMessageValue("chat.filter.spam.promotion","[prefix]&cPlease donn't advertise.");
        Messages.CHAT_FILTER_NICKNAME = addAndGetMessageValue("chat.filter.spam.nickname","&e&lexample.net\n&5&6&7&8\n&cYour nickname is not allowed on this server\n&7\n&aIf you change you name, you can connect :D");
        Messages.CHAT_FILTER_COMMAND = addAndGetMessageValue("chat.filter.spam.command","&fUnknown command. Type \"/help\" for help.");

        Messages.FILTER_HELP = addAndGetMessageValue("filter.help","[prefix] &6Filter Administration\n&8» &e/filter reload &8| &7Reload all filters" +
                "\n&8» &e/filter list {type} &8| &7List all filters\n&8» &e/filter create <type> <message> {operation} &8| &7Create a filter" +
                "\n&8» &e/filter delete <id> &8| &7Delete a filter");
        Messages.FILTER_RELOAD = addAndGetMessageValue("filter.reload","[prefix]&7Reloaded all filters.");
        Messages.FILTER_DELETE = addAndGetMessageValue("filter.delete","[prefix]&7The filter &c[id] &8- &c[word] &7for the type &c[type] &7was deleted.");
        Messages.FILTER_CREATE = addAndGetMessageValue("filter.create","[prefix]&7Created filter &c[id] &8- &c[word] &8(&c[operation]&8) &7for the type &c[type]&7.");
        Messages.FILTER_NOTFOUND = addAndGetMessageValue("filter.notfound","[prefix]&cThis filter was not found.");
        Messages.FILTER_TYPE_NOTFOUND = addAndGetMessageValue("filter.type.notfound","[prefix]&cThis filter type was not found, use:" +
                "\n&8» &7Message\n&8» &7Promotion\n&8» &7Nickname\n&8» &7Command\n&8» &7MuteCommand");
        Messages.FILTER_OPERATION_NOTFOUND = addAndGetMessageValue("filter.operator.notfound","[prefix]&cThis filter operation was not found, use:" +
                "\n&8» &7Contains\n&8» &7Equals\n&8» &7StartsWith\n&8» &7EndsWith");

        Messages.FILTER_LIST_HEADER = addAndGetMessageValue("filter.list.header","[prefix]&7Available filters");
        Messages.FILTER_LIST_LIST = addAndGetMessageValue("filter.list.list","&8» &7[type] &8| &7[id] &8- &7[word] &8(&7[operation]&8)");

        Messages.IPINFO_HELP = addAndGetMessageValue("ipinfo.header","[prefix]&cUsage&8: &7/ipinfo <player/ip>");
        Messages.IPINFO_IP_HEADER = addAndGetMessageValue("ipinfo.ip.header","[prefix]&7Ips from &c[player]");

        Messages.IPINFO_IP_LIST = addAndGetMessageValue("ipinfo.ip.list"," &8- &c[ip]");
        Messages.IPINFO_PLAYER_HEADER = addAndGetMessageValue("ipinfo.player.header","[prefix]&7Players with the ip &c[ip]");
        Messages.IPINFO_PLAYER_LIST = addAndGetMessageValue("ipinfo.player.list"," &8- &8[player] &8| &c[status]");
        Messages.IPINFO_PLAYER_ONLINE = addAndGetMessageValue("ipinfo.player.online","&aOnline");
        Messages.IPINFO_PLAYER_OFFLINE = addAndGetMessageValue("ipinfo.player.offline","&cOffline");
        Messages.IPINFO_PLAYER_MUTED = addAndGetMessageValue("ipinfo.player.muted","&6Muted");
        Messages.IPINFO_PLAYER_BANNED = addAndGetMessageValue("ipinfo.player.banned","&cBanned");

        Messages.CHATLOG_HELP = addAndGetMessageValue("chatlog.help","[prefix]&cUsage&8: &7/chatlog player/server <player/server>");
        Messages.CHATLOG_NOTFOUND = addAndGetMessageValue("chatlog.notfound","[prefix]&cThe chatlog was not found.");
        Messages.CHATLOG_PLAYER_HEADER = addAndGetMessageValue("chatlog.player.header","[prefix]&7Chatlog from &8[player]");
        Messages.CHATLOG_PLAYER_LIST_NORMAL = addAndGetMessageValue("chatlog.player.list.allowed","&8» &9[time] &8| &e[server]&8: &7[message]");
        Messages.CHATLOG_PLAYER_LIST_BLOCKED = addAndGetMessageValue("chatlog.player.list.blocked","&8» &9[time] &8| &e[server]&8: &c[message]");
        Messages.CHATLOG_SERVER_HEADER = addAndGetMessageValue("chatlog.server.header","[prefix]&7Chatlog from &9[server]");
        Messages.CHATLOG_SERVER_LIST_NORMAL = addAndGetMessageValue("chatlog.server.list.allowed","&8» &9[time] &8| &e[player]&8: &7[message]");
        Messages.CHATLOG_SERVER_LIST_BLOCKED = addAndGetMessageValue("chatlog.server.list.allowed","&8» &9[time] &8| &e[player]&8: &c[message]");

        Messages.NETWORK_STATS = addAndGetMessageValue("networkstats","[prefix]&6Network stats&7\n&8» &7Players&8: &a[onlinePlayers]&8/&c[registeredPlayers]" +
                "\n&8» &7Banned&8: &c[bans]\n&8» &7Muted&8: &c[mutes]\n&8» &7Kicked&8: &c[kicks]\n&8» &7Unbanned&8: &c[unbans]\n&8» &7Reported&8: &a[reportsAccepted]&8/&c[reports]" +
                "\n&8» &7Logins&8: &c[logins]\n&8» &7Messages&8: &c[messages]");
    }
}

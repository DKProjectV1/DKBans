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

        Messages.TIME_WEEK_SINGULAR = addAndGetMessageValue("time.week.singular","Week");
        Messages.TIME_WEEK_PLURAL = addAndGetMessageValue("time.week.plural","Weeks");
        Messages.TIME_WEEK_SHORTCUT = addAndGetMessageValue("time.week.shortcut","W");

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
        Messages.SERVER_CONNECTING = addAndGetMessageValue("server.connecting","[prefix]&c^7Connecting to server...");

        Messages.PLAYER_NOT_FOUND = addAndGetMessageValue("player.not.found","[prefix]&cThe player was not found.");
        Messages.PLAYER_NOT_ONLINE = addAndGetMessageValue("player.not.online","[prefix]&cThis player is not online.");
        Messages.PLAYER_NOT_BANNED = addAndGetMessageValue("player.not.banned","[prefix]&8[player] &cis not banned.");
        Messages.PLAYER_ALREADY_BANNED = addAndGetMessageValue("player.already.banned","[prefix]&8[player] &cis already banned.");
        Messages.PLAYER_ALREADY_MUTED = addAndGetMessageValue("player.already.muted","[prefix]&8[player] &cis already muted.");
        Messages.PLAYER_ALREADY_REPORTED = addAndGetMessageValue("player.already.reported","[prefix]&cYou have &8[player] &calready reported.");
        Messages.PLAYER_HAS_MOREBANS_HEADER = addAndGetMessageValue("player.has.morebans.header","[prefix]&8[player] &7has a chat and network ban:");
        Messages.PLAYER_HAS_MOREBANS_NETWORK = addAndGetMessageValue("player.morebans.network","&8» &cBan &8- &4[reason] &8| &7[duration]");
        Messages.PLAYER_HAS_MOREBANS_CHAT = addAndGetMessageValue("player.has.morebans.chat","&8» &6Mute &8- &4[reason] &8| &7[duration]");
        Messages.PLAYER_UNMUTED = addAndGetMessageValue("player.unmuted","[prefix]&7The player &8[player] &7was unmuted.");
        Messages.PLAYER_UNBANNED = addAndGetMessageValue("player.unbanned","[prefix]&7The player &8[player] &7was unbanned.");

        Messages.PLAYER_INFO_HELP = addAndGetMessageValue("player.info.help","[prefix]&cUsage&8: &7 /playerinfo <player>");
        Messages.PLAYER_INFO_ONLINE = addAndGetMessageValue("player.info.online","&8» &4DKNetwork &8| &f");
        Messages.PLAYER_INFO_OFFLINE = addAndGetMessageValue("player.info.offline","&8» &4DKNetwork &8| &f");
        Messages.PLAYER_INFO_SESSIONS_HEADER = addAndGetMessageValue("player.info.session.header","&8» &4DKNetwork &8| &f");
        Messages.PLAYER_INFO_SESSIONS_LIST = addAndGetMessageValue("player.info.session.list","&8» &4DKNetwork &8| &f");

        Messages.STAFF_STATUS_NOW = addAndGetMessageValue("staff.status.now","[prefix]&7You are [status]");
        Messages.STAFF_STATUS_ALREADY = addAndGetMessageValue("staff.status.already","[prefix]&7You are already [status]");
        Messages.STAFF_STATUS_CHANGE = addAndGetMessageValue("staff.status.changed","[prefix]&7You have [status]");
        Messages.STAFF_STATUS_NOT = addAndGetMessageValue("staff.status.not","[prefix]&7You are not [status]");
        Messages.STAFF_STATUS_LOGIN = addAndGetMessageValue("staff.status.login","&alogged in");
        Messages.STAFF_STATUS_LOGOUT = addAndGetMessageValue("staff.status.logout","&clogged out");

        Messages.REASON_NOT_FOUND = addAndGetMessageValue("reason.notfound","&8» &4DKNetwork &8| &f");
        Messages.REASON_NO_PERMISSION = addAndGetMessageValue("prefix.network","[prefix]&cYou don't have permission for this reason.");
        Messages.REASON_HELP = addAndGetMessageValue("reason.help","[prefix]&cThis reason was not found.");

        Messages.BAN_HELP_HEADER = addAndGetMessageValue("ban.help.header","[prefix]&6Ban Administration");
        Messages.BAN_HELP_REASON = addAndGetMessageValue("ban.help.reason","&8- &c[id] &8| &c[reason] &8» &4&l[banType]");
        Messages.BAN_TYPE_NETWORK = addAndGetMessageValue("ban.type.network","Network");
        Messages.BAN_TYPE_CHAT= addAndGetMessageValue("ban.type.chat","Chat");
        Messages.BAN_HELP_HELP = addAndGetMessageValue("ban.help.help","&8» &cUsage&8: &7 /ban <player> <reason> {message}");
        Messages.BAN_BYPASS = addAndGetMessageValue("ban.bypass","[prefix]&cYou can not ban &7[player]&c.");
        Messages.BAN_SUCCESS = addAndGetMessageValue("ban.success","[prefix]&8[player] &7was banned for [reason]&7.");
        Messages.BAN_OVERWRITE_INFO = addAndGetMessageValue("ban.overwrite.info","[prefix]&7Do you want override it? &8[&7Click&8]");

        Messages.BAN_OVERWRITE_NOTALLOWED = addAndGetMessageValue("ban.overwrite.notallowed","[prefix]&cYou are not allowed to overwrite this ban.");

        Messages.BAN_MESSAGE_NETWORK_TEMPORARY = addAndGetMessageValue("ban.message.network.temporary","&e&lexample.net\n&5\n&cYou are &e&l[duration] &cbanned from this network\n&3Reason&8: &4&l[reason]\n&3BanID&8: &7[banid]\n&e\n&3Remaining time&8: &e[remaining]\n&5\n&aYou can make a delivery request at &eforum.example.net");
        Messages.BAN_MESSAGE_NETWORK_PERMANENT = addAndGetMessageValue("ban.message.network.permanent","&e&lexample.net\n&5&6&7&8\n&cYou are &4&lPermanently banned &cfrom this network\n&3Reason&8: &4&l[reason]\n&3BanID&8: &7[banid]\n&5\n&aYou can make a delivery request at &eforum.example.net.");
        Messages.BAN_MESSAGE_CHAT_TEMPORARY = addAndGetMessageValue("ban.message.chat.temporary","&5\n[prefix]&cYou are &e&l[duration] &cbanned from the chat\n&8» &3Reason&8: &4&l[reason]\n&8» &3BanID&8: &7[banid]\n&e\n&8» &3Remaining time&8: &e[remaining]\n&7");
        Messages.BAN_MESSAGE_CHAT_PERMANENT = addAndGetMessageValue("ban.message.chat.permanent","&5\n[prefix]&cYou are &4&lPermanent &cbanned from the chat\n&8» &3Reason&8: &4&l[reason]\n&8» &3BanID&8: &7[banid]\n&5");

        Messages.KICK_HELP_HEADER = addAndGetMessageValue("kick.help.header","[prefix]&6Kick Administration");
        Messages.KICK_HELP_HELP = addAndGetMessageValue("kick.help.help","&8» &cUsage&8: &7 /kick <player> <reason> {message}");
        Messages.KICK_BYPASS = addAndGetMessageValue("kick.bypass","[prefix]&cYou can not kick &8[player]&c.");
        Messages.KICK_SUCCESS = addAndGetMessageValue("kick.success","[prefix]&8[player] &7was kicked for &4[reason]&7.");

        Messages.PING_SELF = addAndGetMessageValue("ping.self","[prefix]&7Your ping is &a[ping]&cms");
        Messages.PING_OTHER = addAndGetMessageValue("ping.other","[prefix]&7The ping from [player] &7is &a[ping]&cms");

        Messages.BROADCAST_HELP = addAndGetMessageValue("broadcast.help","[prefix] &7Broadcast help\n" +
                "&8» &e/bc reload &8| &7Reload all broadcast\n&8» &e/bc direct <message> &8| &7Send a direct message\n" +
                "&8» &e/bc create <message> &8| &7Create a broadcast\n&8» &e/bc <id> send &8| &7Send this broadcast\n" +
                "&8» &e/bc <id> delete &8| &7Delete a message\n&8» &e/bc <id> setClick <type> <message> &8| &7Set a click to a message\n" +
                "&8» &e/bc <id> setMessage <message> &8| &7Change the message\n&8» &e/bc <id> addMessage <message> &8| &7Add a message\n" +
                "&8» &e/bc <id> setAuto <true/false> &8| &7Set a broadcast to auto");
        Messages.BROADCAST_CREATED = addAndGetMessageValue("broadcast.created","[prefix] &7Create broadcast &9[id] &7with the message &9[message]");
        Messages.BROADCAST_DELETED = addAndGetMessageValue("broadcast.deleted","[prefix] &7Deleted broadcast ");
        Messages.BROADCAST_CHANGED_CLICK = addAndGetMessageValue("broadcast.changed.clicked","[prefix] &7Changed click from broadcast &9[id] &7to &9[type] &8| &9[message]");
        Messages.BROADCAST_CHANGED_HOVER = addAndGetMessageValue("broadcast.changed.hover","[prefix] &7Changed hover from broadcast &9[id] to &9[message]");
        Messages.BROADCAST_CHANGED_MESSAGE = addAndGetMessageValue("broadcast.changed.message","[prefix] &7Changed message from 11broadcast &9[id] &7to &9[message]");
        Messages.BROADCAST_CHANGED_AUTO_DISABLED = addAndGetMessageValue("broadcast.changed.auto.disabled","[prefix] &7Disabled auto sending for broadcast &9[id]");
        Messages.BROADCAST_CHANGED_AUTO_ENABLED = addAndGetMessageValue("broadcast.changed.auto.enabled","[prefix] &7Enabled auto sending for broadcast &9[id]");
        Messages.BROADCAST_NOTFOUND_CLICKTYPE = addAndGetMessageValue("broadcast.notfound.clicktype","&8» &4DKNetwork &8| &f");
        Messages.BROADCAST_NOTFOUND_BROADCAST = addAndGetMessageValue("broadcast.notfound.broadcast","[prefix] &cThe broadcast with the with the id &9[id] &cwas not found.");
        Messages.BROADCAST_FORMAT_DIRECT = addAndGetMessageValue("broadcast.format.direct","&8\n[prefix]&7[message]\n&8");
        Messages.BROADCAST_FORMAT_SEND = addAndGetMessageValue("broadcast.format.direct","&8\n[prefix]&7[message]\n&8");
        Messages.BROADCAST_FORMAT_AUTO = addAndGetMessageValue("broadcast.format.direct","&8\n[prefix]&7[message]\n&8");

        /*
        /bc reload
        /bc direct <message>
        /bc create <message>
        /bc <id> send
        /bc <id> delete
        /bc <id> setClick <type> <message>
        /bc <id> setHover <message>
        /bc <id> setMessage <message>
        /bc <id> addMessage <message>
        /bc <id> setAuto <true/false>
         */

        Messages.JUMPTO_HELP = addAndGetMessageValue("jumpto.help","[prefix]&cUsage&8: &7 /jumpto <player>");

        Messages.JOINME_COOLDOWN = addAndGetMessageValue("joinme.cooldown","[prefix]&cWait a moment before sending the next joinme.");
        Messages.JOINME_NOTFOUND = addAndGetMessageValue("joinme.notfound","[prefix]&cThis Joinme was not found.");

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
        Messages.HISTORY_INFO_BAN_NETWORK = addAndGetMessageValue("history.info.ban.chat","[prefix]&7Ban &c[id] &7von [player]" +
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
        Messages.HISTORY_RESET_ALL = addAndGetMessageValue("history.reset.all","[prefix]&7The history from &8[player] &7was reseted.");
        Messages.HISTORY_RESET_ONE = addAndGetMessageValue("history.reset.one","[prefix]&7The history entry &9[id] from [player] &7was reseted.");

        Messages.ONLINE_TIME = addAndGetMessageValue("onlinetime","[prefix]&7Your online time is &c[time]");

        Messages.UNBAN_HELP_HEADER = addAndGetMessageValue("unban.help.header","[prefix]&6Unban Administration");
        Messages.UNBAN_HELP_HELP = addAndGetMessageValue("unban.help.help","[prefix]&cUsage&8: &7 /unban <player> <reason> ");

        Messages.REPORT_HELP_HEADER = addAndGetMessageValue("report.help.header","[prefix]&6Report Administration");
        Messages.REPORT_HELP_REASON = addAndGetMessageValue("report.help.reason","&8- &c[id] &8| &c[reason]");
        Messages.REPORT_HELP_HELP = addAndGetMessageValue("report.help.help","[prefix]&cUsage&8: &7 /report <player> <reason> ");
        Messages.REPORT_SUCCESS = addAndGetMessageValue("report.success","[prefix]&7The player &8[player] &7was reported, thanks. ");
        Messages.REPORT_BYPASS = addAndGetMessageValue("report.bypass","[prefix]&cYou can't report his player.");
        Messages.REPORT_ACCEPTED = addAndGetMessageValue("report.accepted","[prefix]&aThe report for &8[player] &awas accepted, thanks &afor you cooperation.");
        Messages.REPORT_DENIED_STAFF = addAndGetMessageValue("report.denied.staff","[prefix]&7You denied the report of &8[player]&7.");
        Messages.REPORT_DENIED_USER = addAndGetMessageValue("report.denied.user","[prefix]&cThe report of &8[player] &cwas denied.");
        Messages.REPORT_NOTFOUND = addAndGetMessageValue("report.notfound","[prefix]&cThe report was not found.");
        Messages.REPORT_PROCESS = addAndGetMessageValue("report.process.take","[prefix]&7You are now taking care of the player &8[player]\n&8» &7Reporter&8: &c[reporter]\n&8» &7Reason&8: &c[reason]\n&8» &7Message&8: &c[message]\n&8» &7Server&8: &c[server]\n");
        Messages.REPORT_LEAVED_STAFF = addAndGetMessageValue("report.leaved.staff","[prefix]&8[player] &cleaved the server.");
        Messages.REPORT_LEAVED_USER = addAndGetMessageValue("report.leaved.user","[prefix]&cThe report of [player] &cwas denied, because the &cplayer &cleaved the server.");
        Messages.REPORT_LIST_NO = addAndGetMessageValue("report.list.no","[prefix]&cNo reports found.");
        Messages.REPORT_LIST_HEADER = addAndGetMessageValue("report.list.header","[prefix]&7Open reports");
        Messages.REPORT_LIST_LIST = addAndGetMessageValue("report.list.list","&8» &8&l>> &e[reason] &8[&7Click&8]");
        Messages.REPORT_INFO = addAndGetMessageValue("report.info","[prefix]&7At this moments are &9[size] &7Reports open.");
        Messages.REPORT_PROCESS_CONTROL_MESSAGE = addAndGetMessageValue("report.process.control.message","&8» [deny] [forReason] [otherReason]\n&7"); //[deny] [forReason] [otherReason]
        Messages.REPORT_PROCESS_CONTROL_DENY = addAndGetMessageValue("report.process.control.deny","&8[&c&lDeny&8]");
        Messages.REPORT_PROCESS_CONTROL_FORREASON = addAndGetMessageValue("report.process.accept","&8[&a&lAccept&8]");
        Messages.REPORT_PROCESS_CONTROL_OTHERREASON = addAndGetMessageValue("report.process.otherreason","&8[&c&lOther Reason&8]");
        Messages.REPORT_MESSAGE_TEXT = addAndGetMessageValue("report.message.text","[prefix]&e[player] &8&l>> &e[reason] &8[&7Click&8]");
        Messages.REPORT_MESSAGE_HOVER = addAndGetMessageValue("report.message.hover","&8» &7Player&8: &c[player]\n&8» &7Reporter&8: &c[reporter]\n&8» &7Reason&8: &c[reason]\n&8» &7Message&8: &c[message]\n&8» &7Server&8: &c[server]");

        Messages.CHAT_PLUGIN = addAndGetMessageValue("chat.plugin","&fPlugins(1): &aDKBans");
        Messages.CHAT_FILTER_SPAM_REPEAT = addAndGetMessageValue("chat.filter.spam.repeat","[prefix]&cYou are repeating yourself.");
        Messages.CHAT_FILTER_SPAM_TOFAST = addAndGetMessageValue("chat.filter.spam.tofast","[prefix]&cYou are chatting to fast.");
        Messages.CHAT_FILTER_MESSAGE = addAndGetMessageValue("chat.filter.spam.message","[prefix]&cPlease pay attention to your choice of words.");
        Messages.CHAT_FILTER_PROMOTION = addAndGetMessageValue("chat.filter.spam.promotion","[prefix]&cPlease don't advertise.");
        Messages.CHAT_FILTER_NICKNAME = addAndGetMessageValue("chat.filter.spam.nickname","&e&lexample.net\n&5&6&7&8\n&cYour nickname is not allowed on this server\n&7\n&aIf you change you name, you can connect .)");
        Messages.CHAT_FILTER_COMMAND = addAndGetMessageValue("chat.filter.spam.command","&fUnknown command. Type \"/help\" for help.");

        Messages.FILTER_HELP = addAndGetMessageValue("filter.help","");
        Messages.FILTER_RELOAD = addAndGetMessageValue("filter.reload","[prefix]&7Reloaded all filters.");
        Messages.FILTER_ADD = addAndGetMessageValue("filter.add","&8» &4DKNetwork &8| &f");
        Messages.FILTER_DELETE = addAndGetMessageValue("filter.delete","&8» &4DKNetwork &8| &f");
        Messages.FILTER_CREATE = addAndGetMessageValue("filter.create","&8» &4DKNetwork &8| &f");
        Messages.FILTER_NOTFOUND = addAndGetMessageValue("filter.notfound","&8» &4DKNetwork &8| &f");
        Messages.FILTER_TYPE_NOTFOUND = addAndGetMessageValue("filter.type.notfound","&8» &4DKNetwork &8| &f");
        Messages.FILTER_OPERATION_NOTFOUND = addAndGetMessageValue("filter.operator.notfound","&8» &4DKNetwork &8| &f");
        Messages.FILTER_LIST_HEADER = addAndGetMessageValue("filter.list.header","&8» &4DKNetwork &8| &f");
        Messages.FILTER_LIST_LIST = addAndGetMessageValue("filter.list.list","&8» &4DKNetwork &8| &f");

        Messages.IPINFO_HEADER = addAndGetMessageValue("prefix.network","&8» &4DKNetwork &8| &f");
        Messages.IPINFO_IP_HEADER = addAndGetMessageValue("prefix.network","&8» &4DKNetwork &8| &f");
        Messages.IPINFO_IP_LIST = addAndGetMessageValue("prefix.network","&8» &4DKNetwork &8| &f");
        Messages.IPINFO_PLAYER_HEADER = addAndGetMessageValue("prefix.network","&8» &4DKNetwork &8| &f");
        Messages.IPINFO_PLAYER_LIST = addAndGetMessageValue("prefix.network","&8» &4DKNetwork &8| &f");
        Messages.IPINFO_PLAYER_ONLINE = addAndGetMessageValue("prefix.network","&8» &4DKNetwork &8| &f");
        Messages.IPINFO_PLAYER_OFFLINE = addAndGetMessageValue("prefix.network","&8» &4DKNetwork &8| &f");
        Messages.IPINFO_PLAYER_MUTED = addAndGetMessageValue("prefix.network","&8» &4DKNetwork &8| &f");
        Messages.IPINFO_PLAYER_BANNED = addAndGetMessageValue("prefix.network","&8» &4DKNetwork &8| &f");

        Messages.CHATLOG_HELP = addAndGetMessageValue("chatlog.help","[prefix]&cUsage&8: &7/chatlog player/server <player/server>");
        Messages.CHATLOG_NOTFOUND = addAndGetMessageValue("chatlog.notfound","[prefix]&cThe chatlog was not found.");
        Messages.CHATLOG_PLAYER_HEADER = addAndGetMessageValue("chatlog.player.header","[prefix]&7Chatlog from &8[player]");
        Messages.CHATLOG_PLAYER_LIST_NORMAL = addAndGetMessageValue("chatlog.player.list.allowed","&8» &9[time] &8| &7[message]");
        Messages.CHATLOG_PLAYER_LIST_BLOCKED = addAndGetMessageValue("chatlog.player.list.blocked","&8» &9[time] &8| &c[message]");
        Messages.CHATLOG_SERVER_HEADER = addAndGetMessageValue("chatlog.server.header","[prefix]&7Chatlog from &9[server]");
        Messages.CHATLOG_SERVER_LIST_NORMAL = addAndGetMessageValue("chatlog.server.list.allowed","&8» &9[time] &8| &e[player]&8: &7[message]");
        Messages.CHATLOG_SERVER_LIST_BLOCKED = addAndGetMessageValue("chatlog.server.list.allowed","&8» &9[time] &8| &e[player]&8: &c[message]");

        Messages.NETWORK_STATS = addAndGetMessageValue("networkstats","[prefix]&6Network stats");
    }
}

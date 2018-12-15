package ch.dkrieger.bansystem.lib;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 16.11.18 19:26
 *
 */

public class Messages {

    public Messages(String systemname) {
        SYSTEM_NAME = systemname;
        SYSTEM_PREFIX = "["+systemname+"] ";
    }
    public static String SYSTEM_NAME;
    public static String SYSTEM_PREFIX;

    public static String PREFIX_NETWORK;
    public static String PREFIX_BAN;
    public static String PREFIX_REPORT;
    public static String PREFIX_TEAMCHAT;
    public static String PREFIX_CHAT;

    public static String ERROR;
    public static String NOPERMISSIONS;
    public static String HELP;

    public static String TIME_WEEK_SINGLUAR;
    public static String TIME_WEEK_PLURAL;
    public static String TIME_WEEK_SHORTCUT;

    public static String TIME_DAY_SINGLUAR;
    public static String TIME_DAY_PLURAL;
    public static String TIME_DAY_SHORTCUT;

    public static String TIME_HOUR_SINGLUAR;
    public static String TIME_HOUR_PLURAL;
    public static String TIME_HOUR_SHORTCUT;

    public static String TIME_MINUTE_SINGLUAR;
    public static String TIME_MINUTE_PLURAL;
    public static String TIME_MINUTE_SHORTCUT;

    public static String TIME_SECOND_SINGLUAR;
    public static String TIME_SECOND_PLURAL;
    public static String TIME_SECOND_SHORTCUT;

    public static String SERVER_NOT_FOUND;
    public static String SERVER_ALREADY;
    public static String SERVER_CONNECTING;

    public static String PLAYER_NOT_FOUND;
    public static String PLAYER_NOT_ONLINE;
    public static String PLAYER_NOT_BANNED;
    public static String PLAYER_ALREADY_BANNED;
    public static String PLAYER_ALREADY_MUTED;
    public static String PLAYER_ALREADY_REPORTED;
    public static String PLAYER_HAS_MOREBANS_HEADER;
    public static String PLAYER_HAS_MOREBANS_NETWORK;
    public static String PLAYER_HAS_MOREBANS_CHAT;
    public static String PLAYER_UNMUTED;
    public static String PLAYER_UNBANNED;

    public static String PLAYER_INFO_HELP;
    public static String PLAYER_INFO_ONLINE;
    public static String PLAYER_INFO_OFFLINE;
    public static String PLAYER_INFO_SESSIONS_HEADER;
    public static String PLAYER_INFO_SESSIONS_LIST;

    public static String STAFF_STATUS_NOW;
    public static String STAFF_STATUS_ALREADY;
    public static String STAFF_STATUS_CHANGE;
    public static String STAFF_STATUS_NOT;
    public static String STAFF_STATUS_LOGIN;
    public static String STAFF_STATUS_LOGOUT;

    public static String REASON_NOT_FOUND;
    public static String REASON_NO_PERMISSION;
    public static String REASON_HELP;

    public static String BAN_HELP_HEADER;
    public static String BAN_HELP_HELP;
    public static String BAN_BYPASS;
    public static String BAN_SUCCESS;
    public static String BAN_OVERWRITE_INFO;
    public static String BAN_OVERWRITE_NOTALLOWED;

    public static String BAN_MESSAGE_NETWORK_PERMANENT;
    public static String BAN_MESSAGE_NETWORK_TEMPORARY;
    public static String BAN_MESSAGE_CHAT_PERMANENT;
    public static String BAN_MESSAGE_CHAT_TEMPORARY;

    public static String KICK_HELP_HEADER;
    public static String KICK_HELP_HELP;
    public static String KICK_BYPASS;
    public static String KICK_SUCCESS;

    public static String PING_SELF;
    public static String PING_OTHER;

    public static String BROADCAST_HELP;
    public static String BROADCAST_CREATED;
    public static String BROADCAST_DELETED;
    public static String BROADCAST_CHANGED_CLICK;
    public static String BROADCAST_CHANGED_HOVER;
    public static String BROADCAST_CHANGED_MESSAGE;
    public static String BROADCAST_CHANGED_AUTO;
    public static String BROADCAST_NOTFOUND_CLICKTYPE;
    public static String BROADCAST_NOTFOUND_BROADCAST;
    public static String BROADCAST_FORMAT_DIRECT;
    public static String BROADCAST_FORMAT_SEND;
    public static String BROADCAST_FORMAT_AUTO;

    public static String JUMPTO_HELP;

    public static String JOINME_COOLDOWN;
    public static String JOINME_NOTFOUND;

    public static String TEAMCHAT_HELP;
    public static String TEAMCHAT_MESSAGE_COLOR;
    public static String TEAMCHAT_MESSAGE_FORMAT;

    public static String TEMPBAN_HELP;

    public static String TEMPMUTE_HELP;

    public static String HISTORY_HELP;
    public static String HISTORY_NOTFOUND;
    public static String HISTORY_LIST_HEADER;
    public static String HISTORY_LIST_LIST;
    public static String HISTORY_INFO_BAN;
    public static String HISTORY_INFO_KICK;
    public static String HISTORY_INFO_UNBAN;
    public static String HISTORY_INFO_OTHER;
    public static String HISTORY_RESET_ALL;
    public static String HISTORY_RESET_ONE;

    public static String ONLINE_TIME;

    public static String UNBAN_HELP_HEADER;
    public static String UNBAN_HELP_HELP;

    public static String REPORT_SUCCESS;
    public static String REPORT_BYPASS;
    public static String REPORT_ACCEPTED;
    public static String REPORT_DENIED;
    public static String REPORT_NOTFOUND;
    public static String REPORT_PROCESS;
    public static String REPORT_LIST_NO;
    public static String REPORT_LIST_HEADER;
    public static String REPORT_LIST_LIST;
    public static String REPORT_INFO;
    public static String REPORT_PROCESS_CONTROL_MESSAGE; //[deny] [forReason] [otherReason]
    public static String REPORT_PROCESS_CONTROL_DENY;
    public static String REPORT_PROCESS_CONTROL_FORREASON;
    public static String REPORT_PROCESS_CONTROL_OTHERREASON;
    public static String REPORT_MESSAGE_TEXT;
    public static String REPORT_MESSAGE_HOVER;

    public static String CHAT_PLUGIN;
    public static String CHAT_FILTER_SPAM_REPEAT;
    public static String CHAT_FILTER_SPAM_TOFAST;
    public static String CHAT_FILTER_MESSAGE;
    public static String CHAT_FILTER_PROMOTION;
    public static String CHAT_FILTER_NICKNAME;
    public static String CHAT_FILTER_COMMAND;

    public static String FILTER_HELP;
    public static String FILTER_RELOAD;
    public static String FILTER_ADD;
    public static String FILTER_DELETE;
    public static String FILTER_CREATE;
    public static String FILTER_NOTFOUND;
    public static String FILTER_TYPE_NOTFOUND;
    public static String FILTER_OPERATION_NOTFOUND;
    public static String FILTER_LIST_HEADER;
    public static String FILTER_LIST_LIST;

    public static String IPINFO_HEADER;
    public static String IPINFO_IP_HEADER;
    public static String IPINFO_IP_LIST;
    public static String IPINFO_PLAYER_HEADER;
    public static String IPINFO_PLAYER_LIST;
    public static String IPINFO_PLAYER_ONLINE;
    public static String IPINFO_PLAYER_OFFLINE;
    public static String IPINFO_PLAYER_MUTED;
    public static String IPINFO_PLAYER_BANNED;

    public static String CHATLOG_HELP;
    public static String CHATLOG_NOTFOUND;
    public static String CHATLOG_PLAYER_HEADER;
    public static String CHATLOG_PLAYER_LIST_NORMAL;
    public static String CHATLOG_PLAYER_LIST_BLOCKED;
    public static String CHATLOG_SERVER_HEADER;
    public static String CHATLOG_SERVER_LIST_NORMAL;
    public static String CHATLOG_SERVER_LIST_BLOCKED;

    public static String BROADCAST;

    public static String NETWORK_STATS;



}

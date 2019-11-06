/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 06.09.19, 22:57
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

package ch.dkrieger.bansystem.lib;

public class Messages {

    public static String SYSTEM_NAME;
    public static String SYSTEM_PREFIX;

    public static String PREFIX_NETWORK;
    public static String PREFIX_BAN;
    public static String PREFIX_REPORT;
    public static String PREFIX_TEAMCHAT;
    public static String PREFIX_CHAT;
    public static String PREFIX_CHATLOG;

    public static String ERROR;
    public static String NOPERMISSIONS;
    public static String HELP;
    public static String UNKNOWN;
    public static String TRUE;
    public static String FALSE;

    public static String TIME_DAY_SINGULAR;
    public static String TIME_DAY_PLURAL;
    public static String TIME_DAY_SHORTCUT;

    public static String TIME_HOUR_SINGULAR;
    public static String TIME_HOUR_PLURAL;
    public static String TIME_HOUR_SHORTCUT;

    public static String TIME_MINUTE_SINGULAR;
    public static String TIME_MINUTE_PLURAL;
    public static String TIME_MINUTE_SHORTCUT;

    public static String TIME_SECOND_SINGULAR;
    public static String TIME_SECOND_PLURAL;
    public static String TIME_SECOND_SHORTCUT;

    public static String TIME_PERMANENTLY_NORMAL;
    public static String TIME_PERMANENTLY_SHORTCUT;

    public static String TIME_FINISH;

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
    public static String PLAYER_INFO_HISTORY;
    public static String PLAYER_INFO_SESSIONS;
    public static String PLAYER_INFO_IPS;
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

    public static String BAN_HELP_HEADER;
    public static String BAN_HELP_REASON;
    public static String BAN_HELP_HELP;
    public static String BAN_TYPE_NETWORK;
    public static String BAN_TYPE_CHAT;
    public static String BAN_SELF;
    public static String BAN_BYPASS;
    public static String BAN_CHAT_SUCCESS;
    public static String BAN_NETWORK_SUCCESS;
    public static String BAN_OVERWRITE_INFO;
    public static String BAN_OVERWRITE_NOTALLOWED;
    public static String BAN_NOTFOUND;

    public static String BAN_MESSAGE_NETWORK_PERMANENT;
    public static String BAN_MESSAGE_NETWORK_TEMPORARY;
    public static String BAN_MESSAGE_CHAT_PERMANENT;
    public static String BAN_MESSAGE_CHAT_TEMPORARY;

    public static String KICK_HELP_HEADER;
    public static String KICK_HELP_HELP;
    public static String KICK_HELP_REASON;
    public static String KICK_SELF;
    public static String KICK_BYPASS;
    public static String KICK_SUCCESS;
    public static String KICK_MESSAGE;

    public static String WARN_HELP_HEADER;
    public static String WARN_HELP_REASON;
    public static String WARN_HELP_HELP;
    public static String WARN_SELF;
    public static String WARN_BYPASS;
    public static String WARN_SUCCESS;
    public static String WARN_CHAT_MESSAGE;
    public static String WARN_KICK_MESSAGE;

    public static String UNWARN_HELP;
    public static String UNWARN_SUCCESS_ALL;
    public static String UNWARN_SUCCESS_DEFINED;

    public static String PING_SELF;
    public static String PING_OTHER;

    public static String BROADCAST_HELP;
    public static String BROADCAST_RELOADED;
    public static String BROADCAST_CREATED;
    public static String BROADCAST_DELETED;
    public static String BROADCAST_LIST_HEADER;
    public static String BROADCAST_LIST_LIST;
    public static String BROADCAST_INFO;
    public static String BROADCAST_CHANGED_CLICK;
    public static String BROADCAST_CHANGED_HOVER;
    public static String BROADCAST_CHANGED_MESSAGE;
    public static String BROADCAST_CHANGED_PERMISSION;
    public static String BROADCAST_CHANGED_AUTO_DISABLED;
    public static String BROADCAST_CHANGED_AUTO_ENABLED;
    public static String BROADCAST_NOTFOUND_CLICKTYPE;
    public static String BROADCAST_NOTFOUND_BROADCAST;
    public static String BROADCAST_FORMAT_DIRECT;
    public static String BROADCAST_FORMAT_SEND;

    public static String JUMPTO_HELP;

    public static String TEAMCHAT_HELP;
    public static String TEAMCHAT_MESSAGE_COLOR;
    public static String TEAMCHAT_MESSAGE_FORMAT;

    public static String TEMPBAN_HELP;

    public static String TEMPMUTE_HELP;

    public static String HISTORY_HELP;
    public static String HISTORY_NOTFOUND;
    public static String HISTORY_LIST_HEADER;
    public static String HISTORY_LIST_BAN_CHAT;
    public static String HISTORY_LIST_BAN_NETWORK;
    public static String HISTORY_LIST_KICK;
    public static String HISTORY_LIST_UNBAN;
    public static String HISTORY_LIST_WARN;
    public static String HISTORY_INFO_BAN_CHAT;
    public static String HISTORY_INFO_BAN_NETWORK;
    public static String HISTORY_INFO_BAN_CHANGES;
    public static String HISTORY_INFO_BAN_VERSION_LIST_HEADER;
    public static String HISTORY_INFO_BAN_VERSION_LIST_FIRST;
    public static String HISTORY_INFO_BAN_VERSION_LIST_REASON;
    public static String HISTORY_INFO_BAN_VERSION_LIST_MESSAGE;
    public static String HISTORY_INFO_BAN_VERSION_LIST_POINTS;
    public static String HISTORY_INFO_BAN_VERSION_LIST_TIMEOUT;
    public static String HISTORY_INFO_BAN_VERSION_INFO_REASON;
    public static String HISTORY_INFO_BAN_VERSION_INFO_MESSAGE;
    public static String HISTORY_INFO_BAN_VERSION_INFO_POINTS;
    public static String HISTORY_INFO_BAN_VERSION_INFO_TIMEOUT;
    public static String HISTORY_INFO_KICK;
    public static String HISTORY_INFO_UNBAN;
    public static String HISTORY_INFO_WARN;
    public static String HISTORY_RESET_HELP;
    public static String HISTORY_RESET_ALL;
    public static String HISTORY_RESET_ONE;

    public static String ONLINE_TIME;

    public static String UNBAN_HELP_HEADER;
    public static String UNBAN_HELP_REASON;
    public static String UNBAN_HELP_HELP;
    public static String UNBAN_NOTALLOWED;
    public static String UNBAN_NOTFOTHISTYPE;
    public static String UNBAN_TOMANYPOINTS;

    public static String EDITBAN_HELP;
    public static String EDITBAN_CHANGED;
    public static String EDITBAN_NOTALLOWED;

    public static String REPORT_HELP_HEADER;
    public static String REPORT_HELP_REASON;
    public static String REPORT_HELP_HELP;
    public static String REPORT_SELF;
    public static String REPORT_SUCCESS;
    public static String REPORT_BYPASS;
    public static String REPORT_ACCEPTED;
    public static String REPORT_OTHERREASON;
    public static String REPORT_DENIED_STAFF;
    public static String REPORT_DENIED_USER;
    public static String REPORT_LEAVED_STAFF;
    public static String REPORT_LEAVED_USER;
    public static String REPORT_NOTFOUND;
    public static String REPORT_PROCESS;
    public static String REPORT_LIST_NO;
    public static String REPORT_LIST_HEADER;
    public static String REPORT_LIST_LIST;
    public static String REPORT_INFO;
    public static String REPORT_PROCESS_ALREADY;
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
    public static String CHAT_FIRST_JOIN_DELAY_CANCELLED;

    public static String FILTER_HELP;
    public static String FILTER_RELOAD;
    public static String FILTER_DELETE;
    public static String FILTER_CREATE;
    public static String FILTER_NOTFOUND;
    public static String FILTER_TYPE_NOTFOUND;
    public static String FILTER_OPERATION_NOTFOUND;
    public static String FILTER_LIST_HEADER;
    public static String FILTER_LIST_LIST;

    public static String IPINFO_HELP;
    public static String IPINFO_IP_HEADER;
    public static String IPINFO_IP_LIST;
    public static String IPINFO_PLAYER_HEADER;
    public static String IPINFO_PLAYER_LIST;
    public static String IPINFO_PLAYER_ONLINE;
    public static String IPINFO_PLAYER_OFFLINE;
    public static String IPINFO_PLAYER_MUTED;
    public static String IPINFO_PLAYER_BANNED;

    public static String IPBAN_HELP;
    public static String IPBAN_SUCCESS;
    public static String IPBAN_INFO;
    public static String IPBAN_NOT_BANNED;
    public static String IPBAN_ALREADY_BANNED;

    public static String IPUNBAN_HELP;
    public static String IPUNBAN_SUCCESS;

    public static String CHATLOG_HELP;
    public static String CHATLOG_NOTFOUND;
    public static String CHATLOG_PLAYER_HEADER;
    public static String CHATLOG_PLAYER_LIST_NORMAL;
    public static String CHATLOG_PLAYER_LIST_BLOCKED;
    public static String CHATLOG_SERVER_HEADER;
    public static String CHATLOG_SERVER_LIST_NORMAL;
    public static String CHATLOG_SERVER_LIST_BLOCKED;

    public static String NETWORK_STATS;

    public static String MYHISTORYPOINTS;

    public static String STAFFSTATS_HELP;
    public static String STAFFSTATS_INFO;

    public static boolean JOINME_HEAD;
    public static String JOINME_COOLDOWN;
    public static String JOINME_NOTFOUND;
    public static String JOINME_NOTALLOWEDONSERVER;
    public static String JOINME_LINE1;
    public static String JOINME_LINE2;
    public static String JOINME_LINE3;
    public static String JOINME_LINE4;
    public static String JOINME_LINE5;
    public static String JOINME_LINE6;
    public static String JOINME_LINE7;
    public static String JOINME_LINE8;
    public static String JOINME_LINE9;
    public static String JOINME_LINE10;

    public Messages(String systemname) {
        SYSTEM_NAME = systemname;
        SYSTEM_PREFIX = "["+systemname+"] ";
    }

}

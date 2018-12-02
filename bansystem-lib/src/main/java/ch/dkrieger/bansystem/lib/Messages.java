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
    public static String PLAYER_ALREADY_BANNED;
    public static String PLAYER_ALREADY_MUTED;

    public static String PLAYER_INFO_HELP;
    public static String PLAYER_INFO_ONLINE;
    public static String PLAYER_INFO_OFFLINE;

    public static String REASON_NOT_FOUND;
    public static String REASON_NO_PERMISSION;
    public static String REASON_HELP;

    public static String BAN_HELP_HEADER;
    public static String BAN_HELP_HELP;
    public static String BAN_BYPASS;
    public static String BAN_SUCCESS;
    public static String BAN_OVERWRITE_INFO;
    public static String BAN_OVERWRITE_NOTALLOWED;

    public static String KICK_HELP_HEADER;
    public static String KICK_HELP_HELP;
    public static String KICK_BYPASS;
    public static String KICK_SUCCESS;

    public static String PING_SELF;
    public static String PING_OTHER;

    public static String BROADCAST_HELP;

    public static String JUMPTO_HELP;

}

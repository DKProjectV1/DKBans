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

    public static String PREFIX_FRIEND;
    public static String PREFIX_PARTY;

    public static String ERROR;
    public static String NOPERMISSIONS;
}

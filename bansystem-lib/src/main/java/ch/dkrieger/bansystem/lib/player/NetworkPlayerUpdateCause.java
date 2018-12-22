package ch.dkrieger.bansystem.lib.player;

public enum NetworkPlayerUpdateCause {

    NOTSET(),
    LOGIN(),
    LOGOUT(),
    BAN(),
    KICK(),
    UNBAN(),
    WARN(),
    HISTORYUPDATE(),
    STAFFSETTINGS(),
    REPORTSEND(),
    REPORTDENY(),
    REPORTDELETE(),
    REPORTPROCESS();
}

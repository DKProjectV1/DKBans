package ch.dkrieger.bansystem.lib.storage.sql;

/*
 *
 *  * Copyright (c) 2018 Philipp Elvin Friedhoff on 04.12.1018 21:10
 *
 */

import java.sql.Connection;

public abstract class SQL {

    private boolean ignoreCase, supportNoCase, optionsOnEnd;

    public SQL() {
        this.ignoreCase = true;
        this.supportNoCase = true;
        this.optionsOnEnd = false;
    }

    public SQL(boolean ignoreCase, boolean supportNoCase, boolean optionsOnEnd) {
        this.ignoreCase = ignoreCase;
        this.supportNoCase = supportNoCase;
        this.optionsOnEnd = optionsOnEnd;
    }

    public boolean isIgnoreCase() {
        return this.ignoreCase;
    }

    public boolean isOptionsOnEnd() {
        return optionsOnEnd;
    }

    public boolean supportNoCase() {
        return supportNoCase;
    }


    public SQL setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
        return this;
    }

    public SQL setSupportNoCase(boolean supportNoCase) {
        this.supportNoCase = supportNoCase;
        return this;
    }

    public SQL setOptionsOnEnd(boolean optionsOnEnd) {
        this.optionsOnEnd = optionsOnEnd;
        return this;
    }

    public abstract Connection getConnection();
    public abstract void loadDriver();
    public abstract boolean connect();
    public abstract void disconnect();
    public abstract boolean isConnected();
}

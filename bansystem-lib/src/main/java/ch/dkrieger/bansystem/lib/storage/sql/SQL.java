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

package ch.dkrieger.bansystem.lib.storage.sql;

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

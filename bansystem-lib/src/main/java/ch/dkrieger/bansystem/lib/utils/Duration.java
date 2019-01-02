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

package ch.dkrieger.bansystem.lib.utils;

import java.util.concurrent.TimeUnit;

public class Duration {

    private long time;
    private TimeUnit unit;

    public Duration(long time, TimeUnit unit) {
        this.time = time;
        this.unit = unit;
    }

    public long getTime() {
        return time;
    }
    public TimeUnit getUnit() {
        return unit;
    }
    public String getFormattedTime(boolean shortCut){
        return GeneralUtil.calculateDuration(time);
    }
    public long getMillisTime(){
        return this.unit.toMillis(this.time);
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setUnit(TimeUnit unit) {
        this.unit = unit;
    }
}

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

package ch.dkrieger.bansystem.lib.reason;

import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.utils.Duration;

import java.util.concurrent.TimeUnit;

public class BanReasonEntry {

    private BanType type;
    private Duration duration;

    public BanReasonEntry(BanType type, Duration duration) {
        this.type = type;
        this.duration = duration;
    }
    public BanReasonEntry(BanType type, long time, TimeUnit unit){
        this.type = type;
        this.duration = new Duration(time,unit);
    }

    public BanType getType() {
        return type;
    }

    public Duration getDuration() {
        return duration;
    }
}

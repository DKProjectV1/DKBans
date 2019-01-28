/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 13.01.19 11:39
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

package ch.dkrieger.bansystem.extension.maintenance;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Maintenance {

    private boolean enabled;
    private long timeOut;
    private String reason;

    private List<UUID> whitelist;

    public Maintenance(boolean enabled, long timeOut, String reason) {
        this.enabled = enabled;
        this.timeOut = timeOut;
        this.reason = reason;
        this.whitelist = new LinkedList<>();
    }

    public boolean isEnabled() {
        return enabled && getRemaining() > 0;
    }
    public boolean isRawEnabled() {
        return enabled;
    }
    public long getTimeOut() {
        return timeOut;
    }

    public List<UUID> getWhitelist() {
        if(this.whitelist == null) this.whitelist = new LinkedList<>();
        return whitelist;
    }

    public String getReason() {
        return reason;
    }
    public long getRemaining(){
        return timeOut-System.currentTimeMillis();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    public String replace(String message){
        return message.replace("[reason]",reason).replace("[timeOut]",BanSystem.getInstance().getConfig().dateFormat.format(timeOut))
                .replace("[remaining]",formatRemaining(false)).replace("[remaining-short]",formatRemaining(true));
    }
    public String formatRemaining(boolean shortCut){
        long remaining = getRemaining()/1000;
        if(remaining >= TimeUnit.HOURS.toSeconds(24)){
            remaining = Math.round(TimeUnit.SECONDS.toDays(remaining));
            return remaining+" "+(shortCut?Messages.TIME_DAY_SHORTCUT:(remaining>1?Messages.TIME_DAY_PLURAL:Messages.TIME_DAY_SINGULAR));
        }else if(remaining >= 3600){
            remaining = Math.round(TimeUnit.SECONDS.toHours(remaining));
            return remaining+" "+(shortCut?Messages.TIME_HOUR_SHORTCUT:(remaining>1?Messages.TIME_HOUR_PLURAL:Messages.TIME_HOUR_SINGULAR));
        }else {
            remaining = Math.round(TimeUnit.SECONDS.toMinutes(remaining));
            if(remaining < 1) remaining = 1;
            return remaining+" "+(shortCut?Messages.TIME_MINUTE_SHORTCUT:(remaining>1?Messages.TIME_MINUTE_PLURAL:Messages.TIME_MINUTE_SINGULAR));
        }
    }
}

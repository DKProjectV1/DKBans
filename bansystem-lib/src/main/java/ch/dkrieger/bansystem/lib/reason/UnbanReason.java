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

import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.entry.Unban;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.lib.utils.Duration;

import java.util.List;

public class UnbanReason extends KickReason{

    private int maxPoints;
    private boolean removeAllPoints;
    private List<Integer> notForBanID;

    private Duration maxDuration;
    private Duration removeDuration;
    private double durationDivider;

    public UnbanReason(int id, int points, String name, String display, String permission, boolean hidden, List<String> aliases, int maxPoints, boolean removeAllPoints, List<Integer> notForBanID, Duration maxDuration, Duration removeDuration, double durationDivider) {
        super(id, points, name, display, permission, hidden, aliases);
        this.maxPoints = maxPoints;
        this.removeAllPoints = removeAllPoints;
        this.notForBanID = notForBanID;
        this.maxDuration = maxDuration;
        this.removeDuration = removeDuration;
        this.durationDivider = durationDivider;
    }

    public Duration getMaxDuration() {
        return maxDuration;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public List<Integer> getNotForBanID() {
        return notForBanID;
    }
    public Duration getRemoveDuration() {
        return removeDuration;
    }
    public double getDurationDivider() {
        return durationDivider;
    }

    public boolean isRemoveAllPoints() {
        return removeAllPoints;
    }

    public Unban toUnban(BanType type, NetworkPlayer player, String message, String staff){
        if(removeDuration == null || durationDivider == 0) return new Unban(player.getUUID(),player.getIP(),getDisplay(),message,System.currentTimeMillis(),-1,getPoints(),getID(),staff,new Document(),type);
        else{
            return new Unban(player.getUUID(),player.getIP(),getRawDisplay(),message,System.currentTimeMillis(),-1,getPoints(),getID(),staff,new Document(),type);
        }
    }
}

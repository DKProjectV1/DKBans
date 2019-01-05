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
import ch.dkrieger.bansystem.lib.player.history.HistoryPoints;
import ch.dkrieger.bansystem.lib.player.history.entry.Unban;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.lib.utils.Duration;

import java.util.List;

public class UnbanReason extends KickReason{

    private int maxPoints;
    private boolean removeAllPoints;
    private List<Integer> notForBanID;
    private BanType banType;

    private Duration maxDuration;
    private Duration removeDuration;
    private double durationDivider, pointsDivider;

    public UnbanReason(int id, HistoryPoints points, String name, String display, String permission, boolean hidden, List<String> aliases
            , Document properties, int maxPoints, boolean removeAllPoints, List<Integer> notForBanID, Duration maxDuration
            , Duration removeDuration, double durationDivider, double pointsDivider,BanType banType) {
        super(id, points, name, display, permission, hidden, aliases,properties);
        this.maxPoints = maxPoints;
        this.removeAllPoints = removeAllPoints;
        this.notForBanID = notForBanID;
        this.maxDuration = maxDuration;
        this.removeDuration = removeDuration;
        this.durationDivider = durationDivider;
        this.pointsDivider = pointsDivider;
        this.banType = banType;
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

    public double getPointsDivider() {
        return pointsDivider;
    }

    public BanType getBanType() {
        return banType;
    }

    public boolean isRemoveAllPoints() {
        return removeAllPoints;
    }

    public Unban toUnban(BanType type, NetworkPlayer player, String message, String staff, HistoryPoints lastBanPoints){
        int points;
        if(isRemoveAllPoints()) points = lastBanPoints.getPoints();
        else{
            points = lastBanPoints.getPoints()-getPoints().getPoints();
            if(points > 0 && getPointsDivider() > 0) points = (int) (points/getPointsDivider());
        }
        points = points-(points*2);
        return new Unban(player.getUUID(),player.getIP(),getRawDisplay(),message,System.currentTimeMillis(),-1
                ,new HistoryPoints(points,lastBanPoints.getHistoryType()),getID(),staff,new Document(),type);
    }
}

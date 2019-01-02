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

package ch.dkrieger.bansystem.lib.stats;

public class NetworkStats {

    private long logins, reports, reportsAccepted, messages, bans,mutes, unbans, kicks, warns;

    public NetworkStats(){
        this(0,0,0,0,0,0,0,0,0);
    }

    public NetworkStats(long logins, long reports, long reportsAccepted, long messages, long bans, long mutes, long unbans, long kicks, long warns) {
        this.logins = logins;
        this.reports = reports;
        this.reportsAccepted = reportsAccepted;
        this.messages = messages;
        this.bans = bans;
        this.mutes = mutes;
        this.unbans = unbans;
        this.kicks = kicks;
        this.warns = warns;
    }

    public long getLogins() {
        return logins;
    }

    public long getReports() {
        return reports;
    }

    public long getReportsAccepted() {
        return reportsAccepted;
    }
    public long getReportsDenied() {
        return reports-reportsAccepted;
    }

    public long getMessages() {
        return messages;
    }

    public long getBans() {
        return bans;
    }

    public long getMutes() {
        return mutes;
    }

    public long getUnbans() {
        return unbans;
    }

    public long getKicks() {
        return kicks;
    }

    public long getWarns() {
        return warns;
    }

    public void addLogins(){
        logins++;
    }
    public void addReports(){
        reports++;
    }
    public void addReportsAccepted(){
        reportsAccepted++;
    }
    public void addMessages(){
        messages++;
    }

    public void setLogins(long logins) {
        this.logins = logins;
    }

    public void setReports(long reports) {
        this.reports = reports;
    }

    public void setReportsAccepted(long reportsAccepted) {
        this.reportsAccepted = reportsAccepted;
    }

    public void setMessages(long messages) {
        this.messages = messages;
    }

    public void addBans(){
        bans++;
    }
    public void addMutes(){
        mutes++;
    }
    public void addUnbans(){
        unbans++;
    }
    public void addKicks(){
        kicks++;
    }

    public void addWarns(){
        warns++;
    }
}

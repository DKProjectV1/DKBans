/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 08.11.19, 22:06
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

package ch.dkrieger.bansystem.lib.player.history;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.player.history.entry.HistoryEntry;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.util.List;
import java.util.UUID;

public class HistoryManager {

    private List<Ban> activeBans;

    public NetworkPlayer getPlayerByHistoryEntry(int id){
        HistoryEntry entry = getHistoryEntry(id);
        if(entry != null) return entry.getPlayer();
        return null;
    }

    public NetworkPlayer getPlayerByBan(int id){
        HistoryEntry entry = getHistoryEntry(id);
        if(entry instanceof Ban) return entry.getPlayer();
        return null;
    }

    public HistoryEntry getHistoryEntry(int id){
        return BanSystem.getInstance().getStorage().getHistoryEntry(id);
    }

    @SuppressWarnings("This methode is dangerous, it (can) return many datas and have a long delay.")
    public List<Ban> getActiveBans(BanType type){
        if(this.activeBans == null) activeBans = BanSystem.getInstance().getStorage().getNotTimeOutedBans();
        GeneralUtil.iterateAndRemove(activeBans, object -> {
            History history = object.getPlayer().getHistory();
            return !history.getBan(type).equals(object);
        });
        return this.activeBans;
    }

    @SuppressWarnings("This methode is dangerous, it (can) return many datas and have a long delay.")
    public List<Ban> getActiveBans(BanType type, String reason){
        return GeneralUtil.iterateAcceptedReturn(getActiveBans(type), object -> object.getReason().equalsIgnoreCase(reason));
    }

    @SuppressWarnings("This methode is dangerous, it (can) return many datas and have a long delay.")
    public List<Ban> getActiveBans(BanType type,int reasonID){
        return GeneralUtil.iterateAcceptedReturn(getActiveBans(type), object -> object.getID() == reasonID);
    }

    @SuppressWarnings("This methode is dangerous, it (can) return many datas and have a long delay.")
    public List<Ban> getActiveBans(BanType type,UUID staff){
        return GeneralUtil.iterateAcceptedReturn(getActiveBans(type), object -> object.getStaff().equals(staff.toString()));
    }
    public void clearCache(){
        this.activeBans = null;
    }
}

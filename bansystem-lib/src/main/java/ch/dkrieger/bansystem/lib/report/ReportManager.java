/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 08.11.19, 22:07
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

package ch.dkrieger.bansystem.lib.report;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.util.ArrayList;
import java.util.List;

public class ReportManager {

    private List<Report> cachedReports;

    public ReportManager() {
        this.cachedReports = new ArrayList<>();
    }

    public List<Report> getReports(){
        if(this.cachedReports == null) loadReports();
        return this.cachedReports;
    }

    public List<Report> getOpenReports(){
        if(this.cachedReports == null) loadReports();
        return GeneralUtil.iterateAcceptedReturn(this.cachedReports, object -> object.getStaff() == null);
    }

    public void loadReports(){
        this.cachedReports = BanSystem.getInstance().getStorage().getReports();
    }

    public void deleteCachedReports(List<Report> reports){
        reports.forEach(report -> GeneralUtil.iterateAndRemove(cachedReports, object -> object.equals(report)));
    }

    public void clearCachedReports(){
        this.cachedReports = null;
    }
}

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

package ch.dkrieger.bansystem.extension.reportreward;

import ch.dkrieger.bansystem.lib.BanSystem;

public class ReportRewardConfig {


    public int reportRewardCoins;
    public String reportRewardMessage;

    public ReportRewardConfig() {
        reportRewardCoins = BanSystem.getInstance().getConfig().addAndGetIntValue("extension.reportreward.coins",80);
        BanSystem.getInstance().getConfig().save();

        reportRewardMessage = BanSystem.getInstance().getMessageConfig().addAndGetMessageValue("extension.reportreward","[coin-prefix]&7+&6[coins] &7Coins");
        BanSystem.getInstance().getMessageConfig().save();
    }
}

package ch.dkrieger.bansystem.lib.utils;

/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 08.11.19, 19:01
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

import ch.dkrieger.bansystem.lib.BanSystem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class UpdateChecker {

    private final int resourceId;
    private final URL resourceURL;
    private final String currentVersionString;
    private String latestVersionString;
    private UpdateCheckResult updateCheckResult;

    public UpdateChecker(int resourceId) throws MalformedURLException {
        this.resourceId = resourceId;
        this.resourceURL = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId);
        this.currentVersionString = BanSystem.getInstance().getVersion();
    }

    public boolean hasNewVersion() {
        this.latestVersionString = getLatestVersion();

        if (this.latestVersionString == null) {
            updateCheckResult = UpdateCheckResult.NO_RESULT;
            return false;
        }

        int currentVersion = Integer.parseInt(currentVersionString.replace("v", "").replace(".", ""));
        int latestVersion = Integer.parseInt(getLatestVersion().replace("v", "").replace(".", ""));

        if (currentVersion < latestVersion) updateCheckResult = UpdateCheckResult.OUT_DATED;
        else if (currentVersion == latestVersion) updateCheckResult = UpdateCheckResult.UP_TO_DATE;
        else updateCheckResult = UpdateCheckResult.UNRELEASED;
        return this.updateCheckResult == UpdateCheckResult.OUT_DATED;
    }

    public int getResourceId() {
        return resourceId;
    }

    public String getResourceURL() {
        return "https://www.spigotmc.org/resources/" + resourceId;
    }

    public String getCurrentVersionString() {
        return currentVersionString;
    }

    public String getLatestVersionString() {
        return latestVersionString;
    }

    public UpdateCheckResult getUpdateCheckResult() {
        return updateCheckResult;
    }

    public String getLatestVersion() {
        try {
            URLConnection urlConnection = resourceURL.openConnection();
            return new BufferedReader(new InputStreamReader(urlConnection.getInputStream())).readLine();
        } catch (Exception exception) {
            return null;
        }
    }

    public enum UpdateCheckResult {
        NO_RESULT, OUT_DATED, UP_TO_DATE, UNRELEASED,
    }
}
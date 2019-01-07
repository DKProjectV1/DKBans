package de.fridious.bansystem.extension.gui.guis;

/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 07.01.19 16:55
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

import org.bukkit.event.Event;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GuiData {

    private final boolean enabled;
    private final String title;
    private final Map<String, Object> settings;
    private final List<Class<? extends Event>> updateEvents;

    public GuiData(boolean enabled, String title, Map<String, Object> settings, List<Class<? extends Event>> updateEvents) {
        this.enabled = enabled;
        this.title = title;
        this.settings = settings;
        this.updateEvents = updateEvents;
    }

    public GuiData(boolean enabled, String title) {
        this.enabled = enabled;
        this.title = title;
        this.settings = new ConcurrentHashMap<>();
        this.updateEvents = new LinkedList<>();
    }

    public GuiData(boolean enabled, String title, Map<String, Object> settings) {
        this.enabled = enabled;
        this.title = title;
        this.settings = settings;
        this.updateEvents = new LinkedList<>();
    }

    public GuiData(boolean enabled, String title, List<Class<? extends Event>> updateEvents) {
        this.enabled = enabled;
        this.title = title;
        this.updateEvents = updateEvents;
        this.settings = new ConcurrentHashMap<>();
    }

    public GuiData(boolean enabled, String title, Class<? extends Event>... updateEvents) {
        this.enabled = enabled;
        this.title = title;
        this.updateEvents = new LinkedList<>(Arrays.asList(updateEvents));
        this.settings = new ConcurrentHashMap<>();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getTitle() {
        return title;
    }

    public Map<String, Object> getSettings() {
        return settings;
    }

    public List<Class<? extends Event>> getUpdateEvents() {
        return updateEvents;
    }
}
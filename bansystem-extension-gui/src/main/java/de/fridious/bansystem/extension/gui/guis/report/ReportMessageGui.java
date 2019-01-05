package de.fridious.bansystem.extension.gui.guis.report;

import de.fridious.bansystem.extension.gui.api.inventory.gui.AnvilInputGui;
import de.fridious.bansystem.extension.gui.api.inventory.gui.PrivateGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 05.01.19 00:07
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

public class ReportMessageGui extends AnvilInputGui {

    private ReportTemplateGui reportTemplateGui;

    public ReportMessageGui(ReportTemplateGui reportTemplateGui) {
        super(reportTemplateGui.getOwner());
        this.reportTemplateGui = reportTemplateGui;
    }

    @Override
    public String getMessage() {
        return reportTemplateGui.getMessage();
    }

    @Override
    public void setMessage(String message) {
        reportTemplateGui.setMessage(message);
    }

    @Override
    public void updatePage() {
        reportTemplateGui.updatePage(null);
    }

    @Override
    public void open() {
        reportTemplateGui.open();
    }
}
package de.fridious.bansystem.extension.gui.guis.ban;

/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 04.01.19 00:53
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

import de.fridious.bansystem.extension.gui.api.inventory.gui.AnvilSlot;
import de.fridious.bansystem.extension.gui.api.inventory.item.ItemBuilder;
import de.fridious.bansystem.extension.gui.api.inventory.item.ItemStorage;
import de.fridious.bansystem.extension.gui.api.inventory.gui.AnvilInputGui;

public class BanMessageGui extends AnvilInputGui {

    private BanTemplateGui banTemplateGui;

    public BanMessageGui(BanTemplateGui banTemplateGui) {
        super(banTemplateGui.getOwner());
        this.banTemplateGui = banTemplateGui;
        setItem(AnvilSlot.INPUT_LEFT, new ItemBuilder(ItemStorage.get("custom_message")).setDisplayName(banTemplateGui.getMessage()));
    }

    @Override
    public String getMessage() {
        return banTemplateGui.getMessage();
    }

    @Override
    public void setMessage(String message) {
        banTemplateGui.setMessage(message);
    }

    @Override
    public void updatePage() {
        banTemplateGui.updatePage(null);
    }

    @Override
    public void open() {
        banTemplateGui.open();
    }
}
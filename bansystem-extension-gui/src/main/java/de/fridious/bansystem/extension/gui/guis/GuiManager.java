package de.fridious.bansystem.extension.gui.guis;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import de.fridious.bansystem.extension.gui.guis.PlayerInfoGui;
import de.fridious.bansystem.extension.gui.guis.TemplateBanGui;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/*
 * (C) Copyright 2018 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 31.12.18 17:34
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

public class GuiManager {

    private LoadingCache<UUID, OpenedInventories> cachedInventories;

    public GuiManager() {
        this.cachedInventories = CacheBuilder.newBuilder().build(new CacheLoader<UUID, OpenedInventories>() {
            @Override
            public OpenedInventories load(UUID key) throws Exception {
                return new OpenedInventories(key);
            }
        });
    }

    public LoadingCache<UUID, OpenedInventories> getCachedInventories() {
        return cachedInventories;
    }

    public OpenedInventories getCachedInventories(UUID uuid) {
        try {
            return getCachedInventories().get(uuid);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public OpenedInventories getCachedInventories(Player player) {
        return getCachedInventories(player.getUniqueId());
    }

    public class OpenedInventories {

        private UUID uuid;
        private PlayerInfoGui playerInfoGui;
        private TemplateBanGui templateBanGui;

        public OpenedInventories(UUID uuid) {
            this.uuid = uuid;
        }

        public PlayerInfoGui getPlayerInfoGui(UUID target) {
            if(this.playerInfoGui == null) this.playerInfoGui = new PlayerInfoGui(target);
            return playerInfoGui;
        }

        public TemplateBanGui getTemplateBanGui(UUID target) {
            if(this.templateBanGui == null) this.templateBanGui = new TemplateBanGui(this.uuid, target);
            return templateBanGui;
        }

        public void setPlayerInfoGui(PlayerInfoGui playerInfoGui) {
            this.playerInfoGui = playerInfoGui;
        }

        public void setTemplateBanGui(TemplateBanGui templateBanGui) {
            this.templateBanGui = templateBanGui;
        }
    }
}
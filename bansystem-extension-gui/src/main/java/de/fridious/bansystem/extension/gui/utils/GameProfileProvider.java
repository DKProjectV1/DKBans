package de.fridious.bansystem.extension.gui.utils;

/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 01.01.19 18:26
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
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.storage.sql.table.Table;
import com.google.gson.*;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.util.UUIDTypeAdapter;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class GameProfileProvider {

    private static Gson GSON = new GsonBuilder().disableHtmlEscaping().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).registerTypeAdapter(GameProfile.class, new GameProfileSerializer()).registerTypeAdapter(PropertyMap.class, new PropertyMap.Serializer()).create();
    private Map<String, GameProfile> PLAYER_PROFILES = new LinkedHashMap<>();

    public GameProfileProvider() {

    }

    public GameProfile getGameProfileByPlayer(String player) {
        if(PLAYER_PROFILES.containsKey(player)) PLAYER_PROFILES.get(player);
        NetworkPlayer networkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(player);
        GameProfile gameProfile = getGameProfileByJsonString(networkPlayer.getProperties().getString("gameprofile"));
        PLAYER_PROFILES.put(player, gameProfile);
        return gameProfile;
    }

    public GameProfile getGameProfileByJsonString(String value) {
        return GSON.fromJson(value, GameProfile.class);
    }

    public String getJsonStringByGameProfile(GameProfile gameProfile) {
        return GSON.toJson(gameProfile);
    }

    private static class GameProfileSerializer implements JsonSerializer<GameProfile>, JsonDeserializer<GameProfile> {

        public GameProfile deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = (JsonObject) json;
            UUID id = object.has("id") ? (UUID) context.deserialize(object.get("id"), UUID.class) : null;
            String name = object.has("name") ? object.getAsJsonPrimitive("name").getAsString() : null;
            GameProfile profile = new GameProfile(id, name);

            if(object.has("properties")){
                for(Map.Entry<String, Property> prop : ((PropertyMap) context.deserialize(object.get("properties"), PropertyMap.class)).entries()) {
                    profile.getProperties().put(prop.getKey(), prop.getValue());
                }
            }
            return profile;
        }

        public JsonElement serialize(GameProfile profile, Type type, JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            if(profile.getId() != null) result.add("id", context.serialize(profile.getId()));
            if(profile.getName() != null)
                result.addProperty("name", profile.getName());
            if(!profile.getProperties().isEmpty()) result.add("properties", context.serialize(profile.getProperties()));
            return result;
        }
    }
}
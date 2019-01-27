/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 19.01.19 16:12
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

package ch.dkrieger.bansystem.extension.offlinepermissionhook;

import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Tristate;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.PermissionData;

public class LuckPermsHook implements SimplePermissionHook{

    @Override
    public String getName() {
        return "LuckPerms";
    }

    @Override
    public boolean hasPermission(NetworkPlayer player, String permission) {
        User user = LuckPerms.getApi().getUser(player.getUUID());
        if(user == null) return false;
        PermissionData data = user.getCachedData().getPermissionData(LuckPerms.getApi().getContextManager().getApplicableContexts(user));
        Tristate result = data.getPermissionValue(permission);
        return result.asBoolean();
    }
}

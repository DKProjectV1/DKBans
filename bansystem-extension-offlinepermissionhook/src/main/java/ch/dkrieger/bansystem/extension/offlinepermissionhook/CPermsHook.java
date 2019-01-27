/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 19.01.19 16:03
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
import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.bridge.CloudProxy;
import de.dytanic.cloudnet.lib.player.OfflinePlayer;

public class CPermsHook implements SimplePermissionHook{

    @Override
    public String getName() {
        return "CPerms | CloudNetV2";
    }

    @Override
    public boolean hasPermission(NetworkPlayer player, String permission) {
        OfflinePlayer offlinePlayer = CloudProxy.getInstance().getCachedPlayer(player.getUUID());
        if(offlinePlayer == null) offlinePlayer = CloudAPI.getInstance().getOfflinePlayer(player.getUUID());
        return offlinePlayer != null && offlinePlayer.getPermissionEntity().hasPermission(CloudAPI.getInstance().getPermissionPool(), permission, CloudAPI.getInstance().getGroup());
    }
}

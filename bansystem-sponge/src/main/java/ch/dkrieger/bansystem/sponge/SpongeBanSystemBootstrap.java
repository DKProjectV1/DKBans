/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 10.08.19, 21:12
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

package ch.dkrieger.bansystem.sponge;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.DKBansPlatform;
import ch.dkrieger.bansystem.lib.NetworkTaskManager;
import ch.dkrieger.bansystem.lib.command.NetworkCommandManager;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.sponge.network.SpongeNetwork;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameLoadCompleteEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;

import java.io.File;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Plugin(id = "dkbans", name = "DKBans", version = "1.0.0", authors="Davide Wietlisbach")
public class SpongeBanSystemBootstrap implements DKBansPlatform, NetworkTaskManager {

    private static SpongeBanSystemBootstrap instance;

    private NetworkCommandManager commandManager;

    @Listener
    public void onLoad(GameLoadCompleteEvent event){

        instance = this;
        commandManager = new SpongeCommandManager();

        new BanSystem(this,new SpongeNetwork(),null);
    }

    @Override
    public String getPlatformName() {
        return "Sponge";
    }

    @Override
    public String getServerVersion() {
        return "Unknown";
    }

    @Override
    public File getFolder() {
        return new File("plugins/DKBans/");
    }

    @Override
    public NetworkCommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public NetworkTaskManager getTaskManager() {
        return this;
    }

    @Override
    public String getColor(NetworkPlayer player) {
        return null;
    }

    @Override
    public boolean checkPermissionInternally(NetworkPlayer networkPlayer, String permission) {
        Optional<Player> player = Sponge.getServer().getPlayer(networkPlayer.getUUID());
        return player.map(value -> value.hasPermission(permission)).orElse(false);
    }

    @Override
    public void runTaskAsync(Runnable runnable) {
        Task.builder().async().execute(runnable);
    }

    @Override
    public void runTaskLater(Runnable runnable, Long delay, TimeUnit unit) {
        Task.builder().delay(delay,unit).execute(runnable);
    }

    @Override
    public void scheduleTask(Runnable runnable, Long repeat, TimeUnit unit) {
        Task.builder().interval(repeat,unit).execute(runnable);
    }

    public static SpongeBanSystemBootstrap getInstance() {
        return instance;
    }
}

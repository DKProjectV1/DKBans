package de.fridious.bansystem.extension.gui;

/*
 * (C) Copyright 2018 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 30.12.18 14:46
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

import ch.dkrieger.bansystem.bukkit.utils.Reflection;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import de.fridious.bansystem.extension.gui.commands.*;
import de.fridious.bansystem.extension.gui.guis.GuiManager;
import de.fridious.bansystem.extension.gui.guis.ban.BanGlobalGui;
import de.fridious.bansystem.extension.gui.guis.ban.BanSelfGui;
import de.fridious.bansystem.extension.gui.guis.ban.BanTemplateGui;
import de.fridious.bansystem.extension.gui.guis.history.HistoryAllGui;
import de.fridious.bansystem.extension.gui.guis.history.HistoryEntryDeleteGui;
import de.fridious.bansystem.extension.gui.guis.history.HistoryGlobalGui;
import de.fridious.bansystem.extension.gui.guis.kick.KickGlobalGui;
import de.fridious.bansystem.extension.gui.guis.kick.KickSelfGui;
import de.fridious.bansystem.extension.gui.guis.kick.KickTemplateGui;
import de.fridious.bansystem.extension.gui.guis.playerinfo.PlayerInfoGlobalGui;
import de.fridious.bansystem.extension.gui.guis.playerinfo.PlayerInfoGui;
import de.fridious.bansystem.extension.gui.guis.report.*;
import de.fridious.bansystem.extension.gui.guis.unban.UnBanChooseBanTypeGui;
import de.fridious.bansystem.extension.gui.guis.unban.UnBanSelfGui;
import de.fridious.bansystem.extension.gui.guis.unban.UnBanTemplateGui;
import de.fridious.bansystem.extension.gui.guis.warn.WarnGlobalGui;
import de.fridious.bansystem.extension.gui.guis.warn.WarnSelfGui;
import de.fridious.bansystem.extension.gui.guis.warn.WarnTemplateGui;
import de.fridious.bansystem.extension.gui.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class DKBansGuiExtension extends JavaPlugin {

    private static DKBansGuiExtension instance;
    private String chatPrefix, consolePrefix;
    private GuiManager guiManager;
    private boolean configItemIds;
    private GuiConfig config;

    @Override
    public void onLoad() {
        instance = this;
        this.configItemIds = !Bukkit.getBukkitVersion().contains("1.13");
        registerJsonAdapters();
        this.chatPrefix = "&8» &4DKBansGuiExtension &8| &f";
        this.consolePrefix = "[DKBansGuiExtension] ";
        System.out.println(this.consolePrefix + "plugin is starting");
        System.out.println(this.consolePrefix + "DKBansGuiExtension "+ getDescription().getVersion() +" by Philipp Elvin Friedhoff/Fridious");
        System.out.println(this.consolePrefix + "GitHub: https://github.com/Fridious");
        this.guiManager = new GuiManager();
        this.config = new GuiConfig();
        this.config.loadConfig();
        System.out.println(this.consolePrefix + "plugin successfully started");
    }

    @Override
    public void onEnable() {
        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable() {

    }

    public boolean isConfigItemIds() {
        return configItemIds;
    }

    public GuiManager getGuiManager() {
        return guiManager;
    }

    public GuiConfig getGuiConfig() {
        return config;
    }

    public String getChatPrefix() {
        return chatPrefix;
    }

    public String getConsolePrefix() {
        return consolePrefix;
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerLoginListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryOpenListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryCloseListener(), this);
        Bukkit.getPluginManager().registerEvents(new BukkitNetworkPlayerBanListener(), this);
        Bukkit.getPluginManager().registerEvents(new BukkitNetworkPlayerUnBanListener(), this);
        Bukkit.getPluginManager().registerEvents(new BukkitNetworkPlayerReportListener(), this);
        Bukkit.getPluginManager().registerEvents(new BukkitNetworkPlayerReportsProcessListener(), this);
        Bukkit.getPluginManager().registerEvents(new BukkitNetworkPlayerHistoryUpdateListener(), this);
    }

    private void registerCommands() {
        GuiManager guiManager = getGuiManager();
        getCommand("dkbansguiextension").setExecutor(new DKBansGuiExtensionCommand());
        if(guiManager.isGuiEnabled(BanGlobalGui.class) || guiManager.isGuiEnabled(BanSelfGui.class) || guiManager.isGuiEnabled(BanTemplateGui.class))
            getCommand("ban").setExecutor(new BanCommand());
        if(guiManager.isGuiEnabled(PlayerInfoGui.class) || guiManager.isGuiEnabled(PlayerInfoGlobalGui.class))
            getCommand("playerinfo").setExecutor(new PlayerInfoCommand());
        if(guiManager.isGuiEnabled(ReportGlobalGui.class) || guiManager.isGuiEnabled(ReportSelfGui.class) || guiManager.isGuiEnabled(ReportTemplateGui.class))
            getCommand("report").setExecutor(new ReportCommand());
        if(guiManager.isGuiEnabled(ReportControlGui.class) || guiManager.isGuiEnabled(ReportListGui.class))
            getCommand("reports").setExecutor(new ReportsCommand());
        if(guiManager.isGuiEnabled(WarnGlobalGui.class) || guiManager.isGuiEnabled(WarnSelfGui.class) || guiManager.isGuiEnabled(WarnTemplateGui.class))
            getCommand("warn").setExecutor(new WarnCommand());
        if(guiManager.isGuiEnabled(KickGlobalGui.class) || guiManager.isGuiEnabled(KickSelfGui.class) || guiManager.isGuiEnabled(KickTemplateGui.class))
            getCommand("kick").setExecutor(new KickCommand());
        if(guiManager.isGuiEnabled(UnBanChooseBanTypeGui.class) || guiManager.isGuiEnabled(UnBanSelfGui.class) || guiManager.isGuiEnabled(UnBanTemplateGui.class))
            getCommand("unban").setExecutor(new UnBanCommand());
        if(guiManager.isGuiEnabled(HistoryAllGui.class) || guiManager.isGuiEnabled(HistoryEntryDeleteGui.class) || guiManager.isGuiEnabled(HistoryGlobalGui.class))
            getCommand("history").setExecutor(new HistoryCommand());
    }

    private void registerJsonAdapters() {
        try {
            Class<?> propertyMapClass = Reflection.getClass("com.mojang.authlib.properties.PropertyMap");
            Class<?> propertyMapSerializer = propertyMapClass.getDeclaredClasses()[0];

            GeneralUtil.GSON_BUILDER.disableHtmlEscaping() .registerTypeAdapter(propertyMapClass, propertyMapSerializer.newInstance());
            GeneralUtil.GSON_BUILDER_NOT_PRETTY.disableHtmlEscaping().registerTypeAdapter(propertyMapClass, propertyMapSerializer.newInstance());
            GeneralUtil.createGSON();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static DKBansGuiExtension getInstance() {
        return instance;
    }
}
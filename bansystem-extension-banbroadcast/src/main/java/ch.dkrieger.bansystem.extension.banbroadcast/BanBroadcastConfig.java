/*
 * (C) Copyright 2018 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 30.12.18 14:39
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

package ch.dkrieger.bansystem.extension.banbroadcast;

import ch.dkrieger.bansystem.lib.BanSystem;

public class BanBroadcastConfig {

    public boolean ChatBanMessageEnabled, NetworkBanMessageEnabled, KickMessageEnabled, UnbanMessageEnabled, WarnMessageEnabled;

    public String ChatBanMessagePermanently, ChatBanMessageTemporary, NetworkBanMessagePermanently, NetworkBanMessageTemporary, KickMessage, UnbanMessage, WarnMessage;

    public BanBroadcastConfig() {
        NetworkBanMessageEnabled = BanSystem.getInstance().getConfig().addAndGetBooleanValue("extension.banbroadcast.enabled.ban.network",true);
        ChatBanMessageEnabled = BanSystem.getInstance().getConfig().addAndGetBooleanValue("extension.banbroadcast.enabled.ban.chat",true);
        UnbanMessageEnabled = BanSystem.getInstance().getConfig().addAndGetBooleanValue("extension.banbroadcast.enabled.unban",true);
        KickMessageEnabled = BanSystem.getInstance().getConfig().addAndGetBooleanValue("extension.banbroadcast.enabled.kick",true);
        WarnMessageEnabled = BanSystem.getInstance().getConfig().addAndGetBooleanValue("extension.banbroadcast.enabled.warn",true);
        BanSystem.getInstance().getConfig().save();

        NetworkBanMessagePermanently = BanSystem.getInstance().getMessageConfig().addAndGetMessageValue("extension.banbroadcast.ban.network.permanently","[prefix]&9[player] &7was &cbaned &7by &8[staff] &7for &4[reason]&7.");
        NetworkBanMessageTemporary = BanSystem.getInstance().getMessageConfig().addAndGetMessageValue("extension.banbroadcast.ban.network.temporary","[prefix]&9[player] &7was &cbaned &7by &8[staff] &7for &4[reason]&7.");
        ChatBanMessagePermanently = BanSystem.getInstance().getMessageConfig().addAndGetMessageValue("extension.banbroadcast.ban.chat.permanently","[prefix]&9[player] &7was &9muted &7by &8[staff] &7for &4[reason]&7.");
        ChatBanMessageTemporary = BanSystem.getInstance().getMessageConfig().addAndGetMessageValue("extension.banbroadcast.ban.chat.temporary","[prefix]&9[player] &7was &6muted &7by &8[staff] &7for &4[reason]&7.");
        UnbanMessage = BanSystem.getInstance().getMessageConfig().addAndGetMessageValue("extension.banbroadcast.unban","[prefix]&9[player] &7was &aunbanned &7by &8[staff] &7for &4[reason]&7.");
        KickMessage = BanSystem.getInstance().getMessageConfig().addAndGetMessageValue("extension.banbroadcast.kick","[prefix]&9[player] &7was &ekicked &7by &8[staff] &7for &4[reason]&7.");
        WarnMessage = BanSystem.getInstance().getMessageConfig().addAndGetMessageValue("extension.banbroadcast.warn","[prefix]&9[player] &7was &6warned &7by &8[staff] &7for &4[reason]&7.");
        BanSystem.getInstance().getMessageConfig().save();
    }
}

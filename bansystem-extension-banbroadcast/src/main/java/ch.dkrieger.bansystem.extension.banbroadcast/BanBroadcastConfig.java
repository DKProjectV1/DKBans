package ch.dkrieger.bansystem.extension.banbroadcast;

import ch.dkrieger.bansystem.lib.BanSystem;

public class BanBroadcastConfig {

    public boolean ChatBanMessageEnabled, NetworkBanMessageEnabled, KickMessageEnabled, UnbanMessageEnabled;

    public String ChatBanMessagePermanently, ChatBanMessageTemporary, NetworkBanMessagePermanently, NetworkBanMessageTemporary, KickMessage, UnbanMessage;

    public BanBroadcastConfig() {
        NetworkBanMessageEnabled = BanSystem.getInstance().getConfig().addAndGetBooleanValue("extension.banbroadcast.enabled.ban.network",true);
        ChatBanMessageEnabled = BanSystem.getInstance().getConfig().addAndGetBooleanValue("extension.banbroadcast.enabled.ban.chat",true);
        UnbanMessageEnabled = BanSystem.getInstance().getConfig().addAndGetBooleanValue("extension.banbroadcast.enabled.unban",true);
        KickMessageEnabled = BanSystem.getInstance().getConfig().addAndGetBooleanValue("extension.banbroadcast.enabled.kick",true);
        BanSystem.getInstance().getConfig().save();

        NetworkBanMessagePermanently = BanSystem.getInstance().getMessageConfig().addAndGetMessageValue("extension.banbroadcast.ban.network.permanently","[prefix]&9[player] &7was &cbaned &7by &8[staff] &7for &4[reason]&7.");
        NetworkBanMessageTemporary = BanSystem.getInstance().getMessageConfig().addAndGetMessageValue("extension.banbroadcast.ban.network.temporary","[prefix]&9[player] &7was &cbaned &7by &8[staff] &7for &4[reason]&7.");
        ChatBanMessagePermanently = BanSystem.getInstance().getMessageConfig().addAndGetMessageValue("extension.banbroadcast.ban.chat.permanently","[prefix]&9[player] &7was &6muted &7by &8[staff] &7for &4[reason]&7.");
        ChatBanMessageTemporary = BanSystem.getInstance().getMessageConfig().addAndGetMessageValue("extension.banbroadcast.ban.chat.temporary","[prefix]&9[player] &7was &6muted &7by &8[staff] &7for &4[reason]&7.");
        UnbanMessage = BanSystem.getInstance().getMessageConfig().addAndGetMessageValue("extension.banbroadcast.unban","[prefix]&9[player] &7was &aunbanned &7by &8[staff] &7for &4[reason]&7.");
        KickMessage = BanSystem.getInstance().getMessageConfig().addAndGetMessageValue("extension.banbroadcast.kick","[prefix]&9[player] &7was &ekicked &7by &8[staff] &7for &4[reason]&7.");
        BanSystem.getInstance().getMessageConfig().save();
    }
}

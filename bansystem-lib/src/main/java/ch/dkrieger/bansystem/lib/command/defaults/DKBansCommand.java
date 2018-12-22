package ch.dkrieger.bansystem.lib.command.defaults;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 15.07.18 16:24
 *
 */

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import net.md_5.bungee.api.chat.TextComponent;


import java.util.List;

public class DKBansCommand extends NetworkCommand {

    public DKBansCommand() {
        super("dkbans");
        getAliases().add("dkban");
        setPrefix(Messages.PREFIX_NETWORK);
    }
    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        sender.sendMessage(GeneralUtil.createLinkedMCText(Messages.PREFIX_NETWORK+"§7BanSystem §cv"+ BanSystem.getInstance().getVersion()+" §7by §cDavide Wietlisbach","https://www.spigotmc.org/resources/dkbans-bansystem-playermangement-bungeecord-english-german.52570/"));
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}

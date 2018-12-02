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


import java.util.List;

public class DKBansCommand extends NetworkCommand {

    public DKBansCommand() {
        super("dkbans");
        getAliases().add("dkban");
    }
    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        sender.sendMessage(Messages.PREFIX_NETWORK+"§7BanSystem §cv"+ BanSystem.getInstance().getVersion()+" §7by §cDavide Wietlisbach");
        sender.sendMessage(GeneralUtil.createLinkedMCText(" §8» §bOfficial page §8[§7Klick§8]","https://www.spigotmc.org/resources/dkbans-bansystem-playermangement-bungeecord-english-german.52570/"));
        sender.sendMessage(GeneralUtil.createLinkedMCText(" §8» §bTwitter @DevKrieger","https://twitter.com/DevKrieger"));
        sender.sendMessage(GeneralUtil.createLinkedMCText(" §8» §0Github @DevKrieger","https://github.com/DevKrieger/"));
        sender.sendMessage(GeneralUtil.createLinkedMCText(" §8» §5Discord https://discordapp.com/invite/PawBsVy","https://discordapp.com/invite/PawBsVy"));
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}

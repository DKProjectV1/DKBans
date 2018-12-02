package ch.dkrieger.bansystem.lib;

import net.md_5.bungee.api.chat.TextComponent;

public interface Network {

    public void broadcast(String message);

    public void broadcast(TextComponent component);

}

package ch.dkrieger.bansystem.bukkit.event;

import ch.dkrieger.bansystem.lib.utils.Document;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BukkitDKBansMessageReceiveEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Document document;

    public BukkitDKBansMessageReceiveEvent(Document document) {
        this.document = document;
    }

    public Document getDocument() {
        return document;
    }
    public String getAction(){
        return document.getString("action");
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}

package ch.dkrieger.bansystem.bungeecord.event;

import ch.dkrieger.bansystem.lib.utils.Document;
import net.md_5.bungee.api.plugin.Event;

public class ProxiedDKBansMessageReceiveEvent extends Event {

    private Document document;

    public ProxiedDKBansMessageReceiveEvent(Document document) {
        this.document = document;
    }

    public Document getDocument() {
        return document;
    }
    public String getAction(){
        return document.getString("action");
    }

}

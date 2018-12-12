package ch.dkrieger.bansystem.lib.broadcast;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.HashMap;
import java.util.Map;

public class BroadcastManager {

    private Map<Integer,Broadcast> broadcasts;

    public BroadcastManager() {
        this.broadcasts = new HashMap<>();
        reloadLocal();
    }
    public Broadcast getBroadcast(int id){
        return this.broadcasts.get(id);
    }
    public Broadcast createBroadcast(String message){
        return createBroadcast(new Broadcast(-1,message,null,System.currentTimeMillis()
                ,System.currentTimeMillis(),false,null));
    }
    public Broadcast createBroadcast(Broadcast broadcast){
        broadcast.setID(BanSystem.getInstance().getStorage().createBroadcast(broadcast));
        this.broadcasts.put(broadcast.getID(),broadcast);
        return broadcast;
    }
    public void updateBroadcast(Broadcast broadcast){
        this.broadcasts.put(broadcast.getID(),broadcast);
        BanSystem.getInstance().getStorage().updateBroadcast(broadcast);
    }
    public void deleteBroadcast(Broadcast broadcast){
        deleteBroadcast(broadcast.getID());
    }
    public void deleteBroadcast(int id){
        this.broadcasts.remove(id);
    }
    public TextComponent build(Broadcast broadcast){
        TextComponent component = new TextComponent(broadcast.getMessage());
        if(broadcast.getHover() != null) component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT
                ,new ComponentBuilder(broadcast.getHover()).create()));
        if(broadcast.getClick() != null) component.setClickEvent(new ClickEvent(broadcast.getClick().getType().toClickAction()
                ,broadcast.getClick().getMessage()));
        return component;
    }
    public void reloadLocal(){
        this.broadcasts.clear();
        GeneralUtil.iterateForEach(BanSystem.getInstance().getStorage().loadBroadcasts(),object ->broadcasts.put(object.getID(),object));
    }
}

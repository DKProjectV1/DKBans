package ch.dkrieger.bansystem.lib.player.history;

import ch.dkrieger.bansystem.lib.Messages;

public enum BanType {

    NETWORK(Messages.BAN_TYPE_NETWORK),
    CHAT(Messages.BAN_TYPE_CHAT);

    private String display;

    BanType(String display){
        this.display = display;
    }

    public String getDisplay(){
        return display;
    }

    public static BanType parse(String parse){
        try{
            return valueOf(parse.toUpperCase());
        }catch (Exception exception){}
        return null;
    }
}

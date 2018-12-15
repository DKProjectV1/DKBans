package ch.dkrieger.bansystem.lib.player.history;

public enum BanType {

    NETWORK(),
    CHAT();

    public String getDisplay(){
        return null;
    }

    public static BanType parse(String parse){
        try{
            return valueOf(parse.toUpperCase());
        }catch (Exception exception){}
        return null;
    }
}

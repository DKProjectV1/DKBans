package ch.dkrieger.bansystem.lib.config.mode;

public enum KickMode {

    SELF(),
    TEMPLATE();

    public static KickMode parse(String name){
        try {
            return valueOf(name);
        }catch (Exception exception){}
        return SELF;
    }

}

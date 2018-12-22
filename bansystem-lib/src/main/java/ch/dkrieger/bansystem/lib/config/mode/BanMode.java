package ch.dkrieger.bansystem.lib.config.mode;

public enum BanMode {

    SELF(),
    POINT(),
    TEMPLATE();

    public static BanMode parse(String name){
        try {
            return valueOf(name);
        }catch (Exception exception){}
        return TEMPLATE;
    }
}

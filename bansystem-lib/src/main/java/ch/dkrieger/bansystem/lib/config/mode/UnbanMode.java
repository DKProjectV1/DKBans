package ch.dkrieger.bansystem.lib.config.mode;

public enum UnbanMode {

    SELF(),
    TEMPLATE();

    public static UnbanMode parse(String name){
        try {
            return valueOf(name);
        }catch (Exception exception){}
        return SELF;
    }

}

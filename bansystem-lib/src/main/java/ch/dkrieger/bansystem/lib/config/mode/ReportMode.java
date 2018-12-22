package ch.dkrieger.bansystem.lib.config.mode;

public enum ReportMode {

    SELF(),
    TEMPLATE();

    public static ReportMode parse(String name){
        try {
            return valueOf(name);
        }catch (Exception exception){}
        return TEMPLATE;
    }

}

package ch.dkrieger.bansystem.lib.filter;

public enum FilterType {

    MESSAGE(),
    PROMOTION(),
    NICKNAME(),
    COMMAND(),
    MUTECOMMAND();

    public static FilterType ParseNull(String parse){
        try{
            return valueOf(parse.toUpperCase());
        }catch (Exception e){}
        return null;
    }
}

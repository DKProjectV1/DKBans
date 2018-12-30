package ch.dkrieger.bansystem.lib.storage;

public enum StorageType {

    JSON(),
    MYSQL(),
    SQLITE(),
    POSTGRESQL(),
    MONGODB();

    public static StorageType parse(String name){
        try{
            return valueOf(name.toUpperCase());
        }catch (Exception exception){}
        return null;
    }
}

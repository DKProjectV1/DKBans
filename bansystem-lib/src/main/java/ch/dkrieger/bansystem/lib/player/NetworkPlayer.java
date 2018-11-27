package ch.dkrieger.bansystem.lib.player;

import java.util.List;
import java.util.UUID;

public class NetworkPlayer {

    private int id;
    private String Name, color;
    private UUID uuid;
    private long lastLogin, firstLogin, onlineTime;
    private boolean bypass;

    private List<OnlineSession> onlineSessions;

    public NetworkPlayer() {
        RuntimeTypeAdapterFactory<Animal> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
                .of(Animal.class, "type")
                .registerSubtype(Dog.class, "dog")
                .registerSubtype(Cat.class, "cat");

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(runtimeTypeAdapterFactory)
                .create();
    }

    /*
    RuntimeTypeAdapterFactory<Animal> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
.of(Animal.class, "type")
.registerSubtype(Dog.class, "dog")
.registerSubtype(Cat.class, "cat");

Gson gson = new GsonBuilder()
    .registerTypeAdapterFactory(runtimeTypeAdapterFactory)
    .create();
     */


    /*

    History
     - Last Bans
     - Last Kicks
     -

     */


}

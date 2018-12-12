package ch.dkrieger.bansystem.lib.player;

public class OnlineSession {

    private String client,clientVesion, ip, country, lastServer, proxy, clientLanguage;
    private long connected, disconnected;

    public String getClient() {
        return client;
    }

    public String getClientVesion() {
        return clientVesion;
    }

    public String getIp() {
        return ip;
    }

    public String getCountry() {
        return country;
    }

    public String getLastServer() {
        return lastServer;
    }

    public String getProxy() {
        return proxy;
    }

    public String getClientLanguage() {
        return clientLanguage;
    }

    public long getConnected() {
        return connected;
    }

    public long getDisconnected() {
        return disconnected;
    }
    public long getDuration(){

    }
}

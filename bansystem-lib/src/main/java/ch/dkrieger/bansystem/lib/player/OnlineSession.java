package ch.dkrieger.bansystem.lib.player;

public class OnlineSession {

    private String ip, country, lastServer, proxy, clientLanguage;
    private int clientVersion;
    private long connected, disconnected;

    public OnlineSession(String ip, String country, String lastServer, String proxy, String clientLanguage,int clientVersion, long connected, long disconnected) {
        this.ip = ip;
        this.country = country;
        this.lastServer = lastServer;
        this.proxy = proxy;
        this.clientLanguage = clientLanguage;
        this.clientVersion = clientVersion;
        this.connected = connected;
        this.disconnected = disconnected;
    }

    public int getClientVersion() {
        return clientVersion;
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
        return this.connected-disconnected;
    }
}

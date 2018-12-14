package ch.dkrieger.bansystem.lib.stats;

public class NetworkStats extends Stats{

    private int bans,mutes, unbans, kicks;

    public int getBans() {
        return bans;
    }

    public int getMutes() {
        return mutes;
    }

    public int getUnbans() {
        return unbans;
    }

    public int getKicks() {
        return kicks;
    }
}

package ch.dkrieger.bansystem.lib.player.history.entry;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import ch.dkrieger.bansystem.lib.utils.RuntimeTypeAdapterFactory;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class HistoryEntry {

    public static Map<String,Class<? extends HistoryEntry> > GSONTYPEADAPTER = new HashMap<>();

    static {
        GSONTYPEADAPTER.put("BAN",Ban.class);
        GSONTYPEADAPTER.put("KICK",Kick.class);
        GSONTYPEADAPTER.put("UNBAN",Unban.class);
        buildAdapter();
    }

    private UUID uuid;
    private String ip, reason, message;
    private long timeStamp;
    private int id, points, reasonID;
    private String staff;
    private Document properties;

    public HistoryEntry(UUID uuid, String ip, String reason, String message, long timeStamp, int id, int points, int reasonID, String staff, Document properties) {
        this.uuid = uuid;
        this.ip = ip;
        this.reason = reason;
        this.message = message;
        this.timeStamp = timeStamp;
        this.id = id;
        this.points = points;
        this.reasonID = reasonID;
        this.staff = staff;
        this.properties = properties;
    }
    public UUID getUUID() {
        return uuid;
    }

    public String getIp() {
        return ip;
    }

    public String getReason() {
        return reason;
    }

    public String getMessage() {
        return message;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public int getPoints() {
        return points;
    }

    public int getID() {
        return id;
    }

    public int getReasonID() {
        return reasonID;
    }

    public String getStaff() {
        return staff;
    }
    public String getStaffName(){
        if(staff == null) return "Console";
        try{
            return BanSystem.getInstance().getPlayerManager().getPlayer(UUID.fromString(this.staff)).getColoredName();
        }catch (Exception exception){}
        return this.staff;
    }
    public Document getProperties() {
        return properties;
    }

    public NetworkPlayer getPlayer(){
        return BanSystem.getInstance().getPlayerManager().getPlayer(this.uuid);
    }

    @SuppressWarnings("This is only for databse insert functions")
    public void setID(int id) {
        this.id = id;
    }

    public abstract String getTypeName();

    public abstract TextComponent getListMessage();

    public abstract TextComponent getInfoMessage();

    public static void buildAdapter(){
        RuntimeTypeAdapterFactory<HistoryEntry> adapter = RuntimeTypeAdapterFactory.of(HistoryEntry.class, "historyAdapterType");
        for(Map.Entry<String,Class<? extends HistoryEntry>> entry : GSONTYPEADAPTER.entrySet()) adapter.registerSubtype(entry.getValue(),entry.getKey());
        GeneralUtil.GSON_BUILDER.registerTypeAdapterFactory(adapter);
        GeneralUtil.GSON_BUILDER_NOT_PRETTY.registerTypeAdapterFactory(adapter);
        GeneralUtil.createGSON();
    }
}

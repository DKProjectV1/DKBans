package ch.dkrieger.bansystem.lib.player.history.entry;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.UUID;

public class Kick extends HistoryEntry {

    private String server;

    public Kick(UUID uuid, String ip, String reason, String message, long timeStamp, int id, int points, int reasonID, String staff, Document properties, String server) {
        super(uuid, ip, reason, message, timeStamp, id, points, reasonID, staff, properties);
        this.server = server;
    }

    public String getServer() {
        return server;
    }
    @Override
    public String getTypeName() {
        return "Kick";
    }

    @Override
    public TextComponent getListMessage() {
        return new TextComponent(Messages.HISTORY_LIST_KICK
                .replace("[player]",getPlayer().getColoredName())
                .replace("[id]",""+getID())
                .replace("[reason]",getReason())
                .replace("[reasonID]",""+getReasonID())
                .replace("[time]", BanSystem.getInstance().getConfig().dateFormat.format(getTimeStamp()))
                .replace("[timeout]",BanSystem.getInstance().getConfig().dateFormat.format(getTimeStamp()))
                .replace("[message]",getMessage())
                .replace("[type]",getTypeName())
                .replace("[staff]",getStaffName())
                .replace("[server]",getServer())
                .replace("[points]",""+getPoints())
                .replace("[prefix]",Messages.PREFIX_BAN));
    }

    @Override
    public TextComponent getInfoMessage() {
        return new TextComponent(Messages.HISTORY_INFO_KICK
                .replace("[prefix]",Messages.PREFIX_BAN)
                .replace("[id]",""+getID())
                .replace("[reason]",getReason())
                .replace("[type]",getTypeName())
                .replace("[points]",""+getPoints())
                .replace("[reasonID]",""+getReasonID())
                .replace("[message]",getMessage())
                .replace("[time]",BanSystem.getInstance().getConfig().dateFormat.format(getTimeStamp()))
                .replace("[ip]",getIp())
                .replace("[server]",getServer())
                .replace("[staff]",getStaffName())
                .replace("[player]",getPlayer().getColoredName()));
    }

    public TextComponent toMessage(){
        return new TextComponent(Messages.KICK_MESSAGE
                .replace("[prefix]",Messages.PREFIX_BAN)
                .replace("[id]",""+getID())
                .replace("[reason]",getReason())
                .replace("[type]",getTypeName())
                .replace("[points]",""+getPoints())
                .replace("[reasonID]",""+getReasonID())
                .replace("[message]",getMessage())
                .replace("[time]",BanSystem.getInstance().getConfig().dateFormat.format(getTimeStamp()))
                .replace("[ip]",getIp())
                .replace("[server]",getServer())
                .replace("[staff]",getStaffName())
                .replace("[player]",getPlayer().getColoredName()));
    }
}

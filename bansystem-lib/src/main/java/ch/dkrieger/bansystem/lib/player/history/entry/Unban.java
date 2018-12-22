package ch.dkrieger.bansystem.lib.player.history.entry;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.utils.Document;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.UUID;


public class Unban extends HistoryEntry {

    private BanType banType;

    public Unban(UUID uuid, String ip, String reason, String message, long timeStamp, int id, int points, int reasonID, String staff, Document properties, BanType type) {
        super(uuid, ip, reason, message, timeStamp, id, points, reasonID, staff, properties);
        this.banType = type;
    }

    public BanType getBanType() {
        return banType;
    }

    @Override
    public TextComponent getListMessage() {
        return new TextComponent(Messages.HISTORY_LIST_UNBAN
                .replace("[player]",getPlayer().getColoredName())
                .replace("[id]",""+getID())
                .replace("[reason]",getReason())
                .replace("[reasonID]",""+getReasonID())
                .replace("[time]", BanSystem.getInstance().getConfig().dateFormat.format(getTimeStamp()))
                .replace("[timeout]",BanSystem.getInstance().getConfig().dateFormat.format(getTimeStamp()))
                .replace("[message]",getMessage())
                .replace("[type]",getTypeName())
                .replace("[staff]",getStaffName())
                .replace("[banType]",getBanType().getDisplay())
                .replace("[points]",""+getPoints())
                .replace("[prefix]",Messages.PREFIX_BAN));
    }

    @Override
    public TextComponent getInfoMessage() {
        return new TextComponent(Messages.HISTORY_INFO_UNBAN
                .replace("[prefix]",Messages.PREFIX_BAN)
                .replace("[id]",""+getID())
                .replace("[reason]",getReason())
                .replace("[type]",getTypeName())
                .replace("[points]",""+getPoints())
                .replace("[reasonID]",""+getReasonID())
                .replace("[message]",getMessage())
                .replace("[time]",BanSystem.getInstance().getConfig().dateFormat.format(getTimeStamp()))
                .replace("[ip]",getIp())
                .replace("[banType]",getBanType().getDisplay())
                .replace("[staff]",getStaffName())
                .replace("[player]",getPlayer().getColoredName()));
    }

    @Override
    public String getTypeName() {
        return "Unban";
    }
}

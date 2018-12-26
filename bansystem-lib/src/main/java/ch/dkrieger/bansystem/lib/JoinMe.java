package ch.dkrieger.bansystem.lib;

import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.utils.ImageMessage;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JoinMe {

    private UUID player;
    private String server;
    private long timeOut;

    public JoinMe(NetworkPlayer player, String server, long timeOut) {
        this(player.getUUID(),server,timeOut);
    }
    public JoinMe(UUID player, String server, long timeOut) {
        this.player = player;
        this.server = server;
        this.timeOut = timeOut;
    }
    public UUID getUUID() {
        return player;
    }
    public NetworkPlayer getPlayer(){
        return BanSystem.getInstance().getPlayerManager().getPlayer(this.player);
    }
    public String getServer() {
        return server;
    }
    public long getTimeOut() {
        return timeOut;
    }
    public List<TextComponent> create(){
        List<TextComponent> components = new ArrayList<>();
        components.add(createComponent(Messages.JOINME_LINE1));
        components.add(createComponent(Messages.JOINME_LINE2));
        components.add(createComponent(Messages.JOINME_LINE3));
        components.add(createComponent(Messages.JOINME_LINE4));
        components.add(createComponent(Messages.JOINME_LINE5));
        components.add(createComponent(Messages.JOINME_LINE6));
        components.add(createComponent(Messages.JOINME_LINE7));
        components.add(createComponent(Messages.JOINME_LINE8));

        try{
            BufferedImage image = ImageIO.read(new URL("https://minotar.net/avatar/"+player+"/8.png"));
            if(image != null){
                List<TextComponent> newComponents = new ArrayList<>();
                ImageMessage message = new ImageMessage(image,8, '█');
                int i = 0;
                for(TextComponent line : components){
                    TextComponent newComp = new TextComponent(message.getLines()[i]);
                    newComp.addExtra(line);
                    newComp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/joinme "+player));
                    newComponents.add(newComp);
                    i++;
                }
                return newComponents;
            }
        }catch (Exception exception){}

        return components;
    }
    private TextComponent createComponent(String message){
        TextComponent component =  new TextComponent(message.replace("[player]",BanSystem.getInstance().getPlayerManager().getPlayer(player).getColoredName())
                .replace("[server]",server).replace("[prefix]",Messages.PREFIX_NETWORK));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/joinme "+player));
        return component;
    }
}

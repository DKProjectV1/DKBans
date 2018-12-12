package ch.dkrieger.bansystem.lib.broadcast;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Broadcast {

    private int id;
    private String message, hover;
    private long created, lastChange;
    private boolean auto;
    private Click click;

    public Broadcast(int id, String message, String hover, long created, long lastChange, boolean auto, Click click) {
        this.id = id;
        this.message = message;
        this.hover = hover;
        this.created = created;
        this.lastChange = lastChange;
        this.auto = auto;
        this.click = click;
    }

    public int getID() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getHover() {
        return hover;
    }

    public long getCreated() {
        return created;
    }
    public long getLastChange() {
        return lastChange;
    }
    public boolean isAuto() {
        return auto;
    }

    public Click getClick() {
        return click;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setHover(String hover) {
        this.hover = hover;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public void setLastChange(long lastChange) {
        this.lastChange = lastChange;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    public void setClick(Click click) {
        this.click = click;
    }

    public TextComponent build(){
        return null;
    }

    public static class Click {

        private String message;
        private ClickType type;

        public Click(String message, ClickType type) {
            this.message = message;
            this.type = type;
        }
        public String getMessage() {
            return message;
        }
        public ClickType getType() {
            return type;
        }

        public void setMessage(String message) {
            this.message = message;
        }
        public void setType(ClickType type) {
            this.type = type;
        }

    }
    public static enum ClickType {
        URL(),
        COMMAND(),
        OPENCHAT();

        public ClickEvent.Action toClickAction(){
            return (this==URL?ClickEvent.Action.OPEN_URL:(this==OPENCHAT?ClickEvent.Action.SUGGEST_COMMAND:ClickEvent.Action.RUN_COMMAND));
        }
    }
}

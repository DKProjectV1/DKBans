package ch.dkrieger.bansystem.lib.player.chatlog;

import ch.dkrieger.bansystem.lib.filter.FilterType;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.util.Date;
import java.util.List;

public class ChatLog {

    private List<ChatLogEntry> entries;

    public ChatLog(List<ChatLogEntry> entries) {
        this.entries = entries;
    }
    public List<ChatLogEntry> getEntries() {
        return this.entries;
    }
    public List<ChatLogEntry> getEntries(Date from, Date to){
        return getEntries(null,null,from,to);
    }
    public List<ChatLogEntry> getEntries(FilterType filter){
        return getEntries(filter,null,null);
    }
    public List<ChatLogEntry> getEntries(String server){
        return getEntries(server,null,null);
    }
    public List<ChatLogEntry> getEntries(FilterType filter,Date from, Date to){
        return getEntries(filter,null, from, to);
    }
    public List<ChatLogEntry> getEntries(String server,Date from, Date to){
        return getEntries(null,server, from, to);
    }
    public List<ChatLogEntry> getEntries(FilterType filter,String server,Date from, Date to){
        return getEntries(new Filter(server,(from!=null?from.getTime():0),(to!=null?to.getTime():0),filter));
    }
    public List<ChatLogEntry> getEntries(Filter filter){
        return GeneralUtil.iterateAcceptedReturn(this.entries, object ->
                (filter.getServer() == null || object.getServer().equalsIgnoreCase(filter.getServer()))
                        && (filter.getFilter() == null || (object.getFilter() == null || object.getFilter().equals(filter.getFilter())))
                        && (filter.getFrom() <= 0 || object.getTime() >= filter.getFrom())
                        && (filter.getTo() <= 0|| object.getTime() <= filter.getTo()) );
    }
    public static class Filter {

        private String server;
        private long from, to;
        private FilterType filter;

        public Filter() {}

        public Filter(long from, long to, FilterType filtered) {
            this(null,from,to,filtered);
        }

        public Filter(String server, long from, long to, FilterType filtered) {
            this.server = server;
            this.from = from;
            this.to = to;
            this.filter = filtered;
        }

        public String getServer() {
            return server;
        }

        public long getFrom() {
            return from;
        }

        public long getTo() {
            return to;
        }

        public FilterType getFilter() {
            return filter;
        }

        public void setServer(String server) {
            this.server = server;
        }

        public void setFrom(long from) {
            this.from = from;
        }

        public void setTo(long to) {
            this.to = to;
        }

        public void setFilter(FilterType filtered) {
            this.filter = filtered;
        }
    }
}

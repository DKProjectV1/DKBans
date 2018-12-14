package ch.dkrieger.bansystem.lib.player.chatlog;

import ch.dkrieger.bansystem.lib.filter.FilterType;

import java.util.List;

public class ChatLog {

    private List<ChatLogEntry> entries;


    public List<ChatLogEntry> getEntries(Filter filter){

    }

    public static class Filter {

        private String server;
        private long from, to;
        private FilterType filtered;



    }
}

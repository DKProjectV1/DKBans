package ch.dkrieger.bansystem.lib.utils;

import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.reason.BanReason;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 16.11.18 17:47
 *
 */

public class GeneralUtil {

    public static final Random RANDOM = new Random();
    public static final GsonBuilder GSON_BUILDER = new GsonBuilder().setPrettyPrinting();
    public static Gson GSON = GSON_BUILDER.create();
    public static Gson GSON_NOT_PRETTY = new Gson();
    public static final JsonParser PARSER = new JsonParser();

    public static void createGSON(){
        GSON = GSON_BUILDER.create();
    }
    public static String getRandomString(final int size){
        char data = ' ';
        String dat = "";
        for(int i=0;i<=size;i++) {
            data = (char)(RANDOM.nextInt(25)+97);
            dat = data+dat;
        }
        return dat;
    }
    public static <U> U getHighestKey(final Map<U, Integer> map) {
        return map.entrySet().stream().sorted(Map.Entry.<U,Integer>comparingByValue().reversed()).limit(1).map(Map.Entry::getKey).findFirst().orElse(null);
    }
    public static int getMaxPages(int pagesize, List<?> list) {
        int max = pagesize;
        int i = list.size();
        if (i % max == 0) return i/max;
        double j = i / pagesize;
        int h = (int) Math.floor(j*100)/100;
        return h+1;
    }

    public static String calculateTime(long duration, boolean shortcut){
        long millis = duration;
        long seconds = 0;
        long minutes = 0;
        long hours = 0;
        long days = 0;
        long weeks = 0;

        while(millis > 1000){
            millis -=1000;
            seconds++;
        }
        while(seconds > 60){
            seconds -=60;
            minutes++;
        }
        while(minutes > 60){
            minutes -=60;
            hours++;
        }
        while(hours > 24){
            hours -=24;
            days++;
        }
        while(days > 7){
            days -=7;
            weeks++;
        }
        if(shortcut) return weeks+Messages.TIME_WEEK_SHORTCUT+" "+
                days+Messages.TIME_DAY_SHORTCUT+" "+
                hours+Messages.TIME_HOUR_SHORTCUT+" "+
                minutes+Messages.TIME_MINUTE_SHORTCUT+" "+
                seconds+Messages.TIME_SECOND_SHORTCUT;
        return weeks+(weeks == 1?Messages.TIME_WEEK_SINGLUAR:Messages.TIME_WEEK_PLURAL)+" "+
                days+(days == 1?Messages.TIME_DAY_SINGLUAR:Messages.TIME_DAY_PLURAL)+" "+
                hours+(hours == 1?Messages.TIME_HOUR_SINGLUAR:Messages.TIME_HOUR_PLURAL)+" "+
                minutes+(minutes == 1?Messages.TIME_MINUTE_SINGLUAR:Messages.TIME_MINUTE_PLURAL)+" "+
                seconds+(seconds == 1?Messages.TIME_SECOND_SINGLUAR:Messages.TIME_SECOND_PLURAL);
    }

    public static boolean isNumber(String value){
        try{
            Long.parseLong(value);
            return true;
        }catch(NumberFormatException exception){
            return false;
        }
    }
    public static String rotateString(String string){
        String newstring = "";
        char[] chararray = string.toCharArray();
        for(int i = chararray.length-1;i > -1;i--) newstring += chararray[i];
        return newstring;
    }
    public static Boolean equalsOne(String string, String... values){
        for(String value : values) if(value.equalsIgnoreCase(string)) return true;
        return false;
    }
    public static Boolean equalsALL(String string, String... values){
        for(String value : values) if(!value.equalsIgnoreCase(string)) return false;
        return true;
    }
    public static String encode(String password){
        return encode(password.getBytes());
    }
    public static String encode(byte[] bytes) {
        MessageDigest digest = getMessageDigest("MD5");
        byte[] hash = digest.digest(bytes);
        StringBuilder builder = new StringBuilder();
        for(int val : hash) builder.append(Integer.toHexString(val&0xff));
        return builder.toString();
    }
    public static String completEncode(String password){
        return completEncode(password.getBytes());
    }
    public static String completEncode(byte[] bytes){
        String d = encode(encode(bytes));
        String complethash = "";
        for(char c : d.toCharArray()) complethash+=encode(""+c);
        return complethash;
    }
    public static MessageDigest getMessageDigest(String name) {
        try {
            return MessageDigest.getInstance(name);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    public static List<String> startsWith(String search, List<String> list){
        Iterator<String> iterator = list.iterator();
        String name = null;
        while(iterator.hasNext() && (name= iterator.next()) != null) if(!name.toLowerCase().startsWith(search)) iterator.remove();
        return list;
    }

    public static <U> U iterate(List<U> list, AcceptAble<U> acceptAble) {
        Iterator<U> iterator = list.iterator();
        U result = null;
        while(iterator.hasNext() && (result=iterator.next()) != null) if(acceptAble.accept(result)) return result;
        return null;
    }

    public static  TextComponent createLinkedMCText(String text, String link){
        TextComponent component = new TextComponent(text);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,link));
        return component;
    }
    public static long convertToMIllis(long time, String timeType){
        try{
            return TimeUnit.valueOf(timeType.toUpperCase()).toMillis(time);
        }catch (Exception exception){}
        if(equalsOne(timeType,"second","seconds","sekunde","seunden","s")) return TimeUnit.SECONDS.toMillis(time);
        else if(equalsOne(timeType,"minute","minutes","minute","minuten","m")) return TimeUnit.MINUTES.toMillis(time);
        else if(equalsOne(timeType,"hour","hours","stunde","stunden","h","std")) return TimeUnit.HOURS.toMillis(time);
        else if(equalsOne(timeType,"day","days","tage","tag","d","t")) return TimeUnit.DAYS.toMillis(time);
        else if(equalsOne(timeType,"week","weeks","woche","wochen","w")) return TimeUnit.DAYS.toMillis(time*7);
        else if(equalsOne(timeType,"month","months","monate","monaten","mo")) return TimeUnit.DAYS.toMillis(time*30);
        else if(equalsOne(timeType,"year","years","jahr","jahre","y","j")) return TimeUnit.DAYS.toMillis(time*360);
       throw new IllegalArgumentException("False time unit");
    }
    public static String arrayToString(String[] array, String split){
        String result = "";
        for(String string : array) result += string+split;
        return result;
    }
    public interface AcceptAble<T> {
        boolean accept(T object);
    }
}

/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 05.04.19 22:47
 * @Website https://github.com/DevKrieger/DKBans
 *
 * The DKBans Project is under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package ch.dkrieger.bansystem.lib.utils;

import ch.dkrieger.bansystem.lib.Messages;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class GeneralUtil {

    public static final Random RANDOM = new Random();
    public static final GsonBuilder GSON_BUILDER = new GsonBuilder().setPrettyPrinting();
    public static final GsonBuilder GSON_BUILDER_NOT_PRETTY = new GsonBuilder();
    public static Gson GSON = GSON_BUILDER.create();
    public static Gson GSON_NOT_PRETTY = GSON_BUILDER_NOT_PRETTY.create();
    public static final JsonParser PARSER = new JsonParser();

    public static final Function<String,String> STRING_FORMATTER = s -> s;

    private static final String MOJANG_PLAYER_BY_NAME = "https://api.mojang.com/users/profiles/minecraft/%s";

    public static void createGSON(){
        GSON = GSON_BUILDER.create();
        GSON_NOT_PRETTY = GSON_BUILDER_NOT_PRETTY.create();
    }
    public static int getRandom(int low, int high){
        return RANDOM.nextInt(high-low) + low;
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

    public static String calculateRemaining(long duration, boolean shortcut){
        if(duration < 1) return Messages.TIME_FINISH;
        /*
        long seconds = duration % 60;
        long minutes = (duration % 3600) / 60;
        Instant now = Instant.now();
        Instant timeout = Instant.

        Duration duration2 = Duration.between(i1,i1);
        */
        long secondsInMillis = 1000;
        long minutesInMillis = secondsInMillis * 60;
        long hoursInMillis = minutesInMillis *60;
        long daysInMillis = hoursInMillis *24;

        long days = duration / daysInMillis;
        duration = duration % daysInMillis;

        long hours = duration / hoursInMillis;
        duration = duration % hoursInMillis;

        long minutes = duration / minutesInMillis;
        duration = duration % minutesInMillis;

        long seconds = duration / secondsInMillis;

        if(shortcut) return days+Messages.TIME_DAY_SHORTCUT+" "+
                hours+Messages.TIME_HOUR_SHORTCUT+" "+
                minutes+Messages.TIME_MINUTE_SHORTCUT+" "+
                seconds+Messages.TIME_SECOND_SHORTCUT;
        return days+(days == 1?Messages.TIME_DAY_SINGULAR:Messages.TIME_DAY_PLURAL)+" "+
                hours+(hours == 1?Messages.TIME_HOUR_SINGULAR:Messages.TIME_HOUR_PLURAL)+" "+
                minutes+(minutes == 1?Messages.TIME_MINUTE_SINGULAR:Messages.TIME_MINUTE_PLURAL)+" "+
                seconds+(seconds == 1?Messages.TIME_SECOND_SINGULAR:Messages.TIME_SECOND_PLURAL);
    }
    public static String calculateDuration(long duration){
        if(duration <= 0) return Messages.TIME_PERMANENTLY_NORMAL;
        duration = duration/1000;
        if(duration < 60) return duration+" "+(duration==1?Messages.TIME_SECOND_SINGULAR:Messages.TIME_SECOND_PLURAL);
        else if(duration <3600){
            duration = Math.round((float)duration/60);
            return duration+" "+(duration==1?Messages.TIME_MINUTE_SINGULAR:Messages.TIME_MINUTE_PLURAL);
        }
        else if(duration < 86400){
            duration = Math.round((float) (duration/60)/60);
            return duration+" "+(duration==1?Messages.TIME_HOUR_SINGULAR:Messages.TIME_HOUR_PLURAL);
        }
        duration = Math.round((float)((duration/60)/60)/24);
        return duration+" "+(duration==1?Messages.TIME_DAY_SINGULAR:Messages.TIME_DAY_PLURAL);
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
    public static boolean equalsOne(String string, String... values){
        for(String value : values) if(value.equalsIgnoreCase(string)) return true;
        return false;
    }
    public static boolean equalsAll(String string, String... values){
        for(String value : values) if(!value.equalsIgnoreCase(string)) return false;
        return true;
    }
    public static String encodeMD5(String password){
        return encodeMD5(password.getBytes());
    }
    public static String encodeMD5(byte[] bytes) {
        MessageDigest digest = getMessageDigest("MD5");
        byte[] hash = digest.digest(bytes);
        StringBuilder builder = new StringBuilder();
        for(int val : hash) builder.append(Integer.toHexString(val&0xff));
        return builder.toString();
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

    public static <U> U iterateOne(Iterable<U> list, AcceptAble<U> acceptAble) {
        Iterator<U> iterator = list.iterator();
        U result = null;
        while(iterator.hasNext() && (result=iterator.next()) != null) if(acceptAble.accept(result)) return result;
        return null;
    }
    public static <U> void iterateForEach(Iterable<U> list, ForEach<U> forEach){
        Iterator<U> iterator = list.iterator();
        U result = null;
        while(iterator.hasNext() && (result=iterator.next()) != null) forEach.forEach(result);
    }
    public static <U> void iterateAcceptedForEach(Iterable<U> list, AcceptAble<U> acceptAble, ForEach<U> forEach) {
        Iterator<U> iterator = list.iterator();
        U result = null;
        while(iterator.hasNext() && (result=iterator.next()) != null) if(acceptAble.accept(result)) forEach.forEach(result);
    }
    public static <U> List<U> iterateAcceptedReturn(Iterable<U> list, AcceptAble<U> acceptAble){
        List<U> result = new ArrayList<>();
        iterateAcceptedForEach(list,acceptAble,result::add);
        return result;
    }
    public static <U> void iterateAndRemove(Iterable<U> list, AcceptAble<U> acceptAble){
        Iterator<U> iterator = list.iterator();
        U result = null;
        while(iterator.hasNext() && (result=iterator.next()) != null) if(acceptAble.accept(result)) iterator.remove();
    }
    public static  TextComponent createLinkedMCText(String text, String link){
        TextComponent component = new TextComponent(text);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,link));
        return component;
    }
    public static long convertToMillis(long time, String timeType){
        if(timeType == null) return TimeUnit.DAYS.toMillis(time);
        try{
            return TimeUnit.valueOf(timeType.toUpperCase()).toMillis(time);
        }catch (Exception exception){}
        if(equalsOne(timeType,"second","seconds","sekunde","sekunden","s")) return TimeUnit.SECONDS.toMillis(time);
        else if(equalsOne(timeType,"minute","minutes","minute","minuten","m")) return TimeUnit.MINUTES.toMillis(time);
        else if(equalsOne(timeType,"hour","hours","stunde","stunden","h","std")) return TimeUnit.HOURS.toMillis(time);
        else if(equalsOne(timeType,"day","days","tage","tag","d","t")) return TimeUnit.DAYS.toMillis(time);
        else if(equalsOne(timeType,"week","weeks","woche","wochen","w")) return TimeUnit.DAYS.toMillis(time*7);
        else if(equalsOne(timeType,"month","months","monate","monaten","mo")) return TimeUnit.DAYS.toMillis(time*30);
        else if(equalsOne(timeType,"year","years","jahr","jahre","y","j")) return TimeUnit.DAYS.toMillis(time*360);
        throw new IllegalArgumentException("False time unit");
    }
    public static TextComponent replaceTextComponent(TextComponent text, String replacement, TextComponent component){
        final List<BaseComponent> components = text.getExtra();
        if(components != null && components.size() > 0){
            for (BaseComponent subComponent : components) {
                if(subComponent instanceof TextComponent)replaceTextComponent((TextComponent) subComponent,replacement,component);
            }
            text.setExtra(components);
        }
        if(text.getText() != null && !(text.getText().equalsIgnoreCase("null") )&& !(text.getText().equalsIgnoreCase(""))){
            TextComponent newTC = replaceTextComponent(text.getText(),replacement,component);
            text.setText("");
            newTC.setText("");
            if(text.getExtra() == null) text.setExtra(new ArrayList<>());
            text.getExtra().addAll(newTC.getExtra());
        }
        return text;
    }
    public static TextComponent replaceTextComponent(String text, String replacement, TextComponent component){
        TextComponent message = new TextComponent();
        if(text.contains(replacement)){
            int index = text.lastIndexOf(replacement);
            message.addExtra(new TextComponent(text.substring(0,index)));
            message.addExtra(component);
            text = text.substring(index).replace(replacement,"");
        }
        if(text.length() > 0) message.addExtra(text);
        message.setText("");
        return message;
    }

    public static String arrayToString(String[] array, String split){
        String result = "";
        for(String string : array) result += string+split;
        return result;
    }

    public static Boolean isIP4Address(String ip){
        try {
            if(ip == null || ip.isEmpty()) return false;
            String[] parts = ip.split("\\.");
            if(parts.length != 4) return false;
            for(String s : parts){
                int i = Integer.parseInt(s);
                if((i < 0) || (i > 255)) return false;
            }
            if(ip.endsWith(".")) return false;
            return true;
        }catch (NumberFormatException exception){
            return false;
        }
    }
    public static List<String> calculateTabComplete(String search, String not, List<String> options){
        return calculateTabComplete(search, not, options,STRING_FORMATTER);
    }

    public static <T> List<String> calculateTabComplete(String search, String not, List<T> options, Function<T,String> formatter){
        search = search.toLowerCase();
        List<String> result = new LinkedList<>();
        for(T name : options){
            String object = formatter.apply(name).toLowerCase();
            if(object.toLowerCase().startsWith(search) && !(object.equalsIgnoreCase(not))) result.add(object);
        }
        return result;
    }

    public static String buildNextLineColor(String message){
        String newMessage = "";
        String lastColor = null;
        boolean nextColor = false;

        for(char c : message.toCharArray()){
            if(c == '§') nextColor = true;
            else {
                if(c == ' '){
                    if(lastColor != null){
                        newMessage += (" "+lastColor);
                        continue;
                    }
                }else if(nextColor){
                    ChatColor  color = ChatColor.getByChar(c);
                    if(color != null) lastColor = "§"+c;
                }
                nextColor = false;
            }
            newMessage += c;
        }
        return newMessage;
    }

    public static UUID getMojangUUIDByName(String name) {
        String url = String.format(MOJANG_PLAYER_BY_NAME, name);
        String content = getWebsiteContent(url);
        JsonObject jsonObject = GSON.fromJson(content, JsonObject.class);
        if(jsonObject == null) return null;
        String uuidString = jsonObject.get("id").getAsString();
        StringBuilder dashBuilder = new StringBuilder(uuidString)
                .insert(20, '-')
                .insert(16, '-')
                .insert(12, '-')
                .insert(8, '-');
        return UUID.fromString(dashBuilder.toString());
    }

    private static String getWebsiteContent(String url) {
        try {
            URL website = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) website.openConnection();
            int statusCode = connection.getResponseCode();
            if (statusCode >= 200 && statusCode < 400) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return response.toString();
            } else return null;
        } catch(IOException ignored) {
            return null;
        }
    }

    public interface AcceptAble<T> {
        boolean accept(T object);
    }
    public interface ForEach<T> {
        void forEach(T object);
    }
}

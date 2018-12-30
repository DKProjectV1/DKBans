/*
 * (C) Copyright 2018 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 30.12.18 14:39
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

package ch.dkrieger.bansystem.lib.broadcast;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import net.md_5.bungee.api.chat.*;

import java.util.*;

public class BroadcastManager {

    private Map<Integer,Broadcast> broadcasts;
    private Map<String,BuildAdapter> buildAdapters;
    private int nextBroadcast;

    public BroadcastManager() {
        nextBroadcast = 1;
        this.broadcasts = new HashMap<>();
        this.buildAdapters = new HashMap<>();

        registerBuildAdapter("player",NetworkPlayer::getColoredName);
        registerBuildAdapter("color",NetworkPlayer::getColor);
        registerBuildAdapter("country",NetworkPlayer::getCountry);
        registerBuildAdapter("time", player -> BanSystem.getInstance().getConfig().dateFormat.format(System.currentTimeMillis()));
        registerBuildAdapter("prefix",player -> Messages.PREFIX_NETWORK);

        reloadLocal();
    }
    public Broadcast getNext(){
        List<Broadcast> broadcasts = getAutoBroadcasts();
        broadcasts.sort((o1, o2) -> (o1.getID()>o2.getID()?1:-1));
        if(broadcasts.size() < 1) return null;
        if(BanSystem.getInstance().getConfig().autobroadcastSorted){
            Broadcast broadcast = null;
            int lastId = 0;
            for(Broadcast bc : broadcasts){
                if(bc.getID()==nextBroadcast){
                    nextBroadcast++;
                    return bc;
                }
                if(broadcast == null || (bc.getID() > nextBroadcast && bc.getID() < lastId)){
                    lastId = bc.getID();
                    broadcast = bc;
                }
            }
            System.out.println(broadcast);
            nextBroadcast = broadcast.getID()+1;
            return broadcast;
        }else return broadcasts.get(GeneralUtil.RANDOM.nextInt(broadcasts.size()));
    }
    public void registerBuildAdapter(String replace, BuildAdapter adapter){
        this.buildAdapters.put(replace,adapter);
    }
    public Broadcast getBroadcast(int id){
        return this.broadcasts.get(id);
    }
    public List<Broadcast> getAutoBroadcasts(){
        List<Broadcast> broadcasts = new ArrayList<>();
        GeneralUtil.iterateAcceptedForEach(this.broadcasts.values(),Broadcast::isAuto,broadcasts::add);
        return broadcasts;
    }
    public List<Broadcast> getBroadcasts(){
        return new ArrayList<>(broadcasts.values());
    }
    public Broadcast createBroadcast(String message){
        return createBroadcast(new Broadcast(-1,message,null,null,System.currentTimeMillis()
                ,System.currentTimeMillis(),false,new Broadcast.Click("", Broadcast.ClickType.URL)));
    }
    public Broadcast createBroadcast(Broadcast broadcast){
        broadcast.setID(BanSystem.getInstance().getStorage().createBroadcast(broadcast));
        this.broadcasts.put(broadcast.getID(),broadcast);
        return broadcast;
    }
    public void updateBroadcast(Broadcast broadcast){
        this.broadcasts.put(broadcast.getID(),broadcast);
        BanSystem.getInstance().getStorage().updateBroadcast(broadcast);
    }
    public void deleteBroadcast(Broadcast broadcast){
        deleteBroadcast(broadcast.getID());
    }
    public void deleteBroadcast(int id){
        this.broadcasts.remove(id);
    }
    public TextComponent build(Broadcast broadcast){
        return build(broadcast,null);
    }
    public TextComponent build(Broadcast broadcast, NetworkPlayer player){
        List<BaseComponent> components = new ArrayList<>();
        String message = broadcast.getMessage();
        String click = broadcast.getClick().getMessage();
        String hover = broadcast.getHover();
        String before = "";
        String id = "";
        if(player != null){
            for(Map.Entry<String,BuildAdapter> adapter : this.buildAdapters.entrySet()){
                if(message.contains("["+adapter.getKey()+"]")) message= message.replace("["+adapter.getKey()+"]",adapter.getValue().build(player));
                if(hover.contains("["+adapter.getKey()+"]")) hover= hover.replace("["+adapter.getKey()+"]",adapter.getValue().build(player));
                if(click.contains("["+adapter.getKey()+"]")) click= click.replace("["+adapter.getKey()+"]",adapter.getValue().build(player));
            }
        }
        for(char c : message.toCharArray()){
            if(before.endsWith("[include=")){
                if(c == ']'){
                    components.add(new TextComponent(before.substring(0,before.length()-9)));
                    Broadcast subBroadcast = null;
                    if(GeneralUtil.isNumber(id) && broadcast.getID() != Integer.valueOf(id)) subBroadcast = getBroadcast(Integer.valueOf(id));
                    if(subBroadcast != null){
                        components.add(build(subBroadcast,player));
                        before = "";
                        id ="";
                    }else before = "{BC-Unknown}";
                }else id += c;
            }else before += c;
        }
        if(before.length() > 0) components.add(new TextComponent(before));
        TextComponent component = new TextComponent();
        component.setExtra(components);
        component.setText("");
        if(hover.length() > 1) component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder(hover).create()));
        if(click.length() > 1) component.setClickEvent(new ClickEvent(broadcast.getClick().getType().toClickAction(),click));
        return component;
    }
    public void reloadLocal(){
        this.broadcasts.clear();
        GeneralUtil.iterateForEach(BanSystem.getInstance().getStorage().loadBroadcasts(),object ->broadcasts.put(object.getID(),object));
    }
    public interface BuildAdapter {
        String build(NetworkPlayer player);
    }
}

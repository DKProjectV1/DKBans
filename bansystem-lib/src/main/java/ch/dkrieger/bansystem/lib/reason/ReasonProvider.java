/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 14.03.19 19:43
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

package ch.dkrieger.bansystem.lib.reason;

import ch.dkrieger.bansystem.lib.DKBansPlatform;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.HistoryPoints;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.lib.utils.Duration;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import com.google.gson.JsonObject;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ReasonProvider {

    private DKBansPlatform platform;
    private List<KickReason> kickReasons;
    private List<BanReason> banReasons;
    private List<ReportReason> reportReasons;
    private List<UnbanReason> unbanReasons;
    private List<WarnReason> warnReasons;

    public ReasonProvider(DKBansPlatform platform){
        this.platform = platform;
        this.banReasons = new ArrayList<>();
        this.kickReasons = new ArrayList<>();
        this.reportReasons = new ArrayList<>();
        this.unbanReasons = new ArrayList<>();
        this.warnReasons = new ArrayList<>();
        loadBanReasons();
        loadUnbanReasons();
        loadReportReasons();
        loadKickReasons();
        loadWarnReasons();
    }
    public ReasonProvider(DKBansPlatform platform,List<KickReason> kickReasons, List<BanReason> banReasons, List<ReportReason> reportReasons, List<UnbanReason> unbanReasons) {
        this.platform = platform;
        this.kickReasons = kickReasons;
        this.banReasons = banReasons;
        this.reportReasons = reportReasons;
        this.unbanReasons = unbanReasons;
    }
    public List<KickReason> getKickReasons() {
        return kickReasons;
    }
    public List<BanReason> getBanReasons() {
        return banReasons;
    }
    public List<ReportReason> getReportReasons() {
        return reportReasons;
    }
    public List<UnbanReason> getUnbanReasons() {
        return unbanReasons;
    }
    public List<WarnReason> getWarnReasons() {
        return warnReasons;
    }

    public KickReason searchKickReason(String search){
        if(GeneralUtil.isNumber(search)) return getKickReason(Integer.valueOf(search));
        return getKickReason(search);
    }
    public KickReason getKickReason(int id){
      return GeneralUtil.iterateOne(this.kickReasons,reason -> reason.getID() == id);
    }
    public KickReason getKickReason(String name){
        return GeneralUtil.iterateOne(this.kickReasons,reason -> reason.hasAlias(name));
    }

    public BanReason searchBanReason(String search){
        if(GeneralUtil.isNumber(search)) return getBanReason(Integer.valueOf(search));
        return getBanReason(search);
    }

    public BanReason getBanReason(int id){
        return GeneralUtil.iterateOne(this.banReasons,reason -> reason.getID() == id);
    }


    public BanReason getBanReason(String name){
        return GeneralUtil.iterateOne(this.banReasons,reason -> reason.hasAlias(name));
    }

    public ReportReason searchReportReason(String search){
        if(GeneralUtil.isNumber(search)) return getReportReason(Integer.valueOf(search));
        return getReportReason(search);
    }

    public ReportReason getReportReason(int id){
        return GeneralUtil.iterateOne(this.reportReasons,reason -> reason.getID() == id);
    }
    public ReportReason getReportReason(String name){
        return GeneralUtil.iterateOne(this.reportReasons,reason -> reason.hasAlias(name));
    }

    public UnbanReason searchUnbanReason(String search){
        if(GeneralUtil.isNumber(search)) return getUnbanReason(Integer.valueOf(search));
        return getUnbanReason(search);
    }
    public UnbanReason getUnbanReason(int id){
        return GeneralUtil.iterateOne(this.unbanReasons,reason -> reason.getID() == id);
    }
    public UnbanReason getUnbanReason(String name){
        return GeneralUtil.iterateOne(this.unbanReasons,reason -> reason.hasAlias(name));
    }

    public WarnReason searchWarnReason(String search){
        if(GeneralUtil.isNumber(search)) return getWarnReason(Integer.valueOf(search));
        return getWarnReason(search);
    }
    public WarnReason getWarnReason(int id){
        return GeneralUtil.iterateOne(this.warnReasons,reason -> reason.getID() == id);
    }
    public WarnReason getWarnReason(String name){
        return GeneralUtil.iterateOne(this.warnReasons,reason -> reason.hasAlias(name));
    }

    public void loadBanReasons(){
        File file = new File(this.platform.getFolder(),"ban-reasons.yml");
        Configuration config = null;
        if(file.exists()){
            try{
                config = YamlConfiguration.getProvider(YamlConfiguration.class).load(file);
                Configuration reasons = config.getSection("reasons");
                this.banReasons = new ArrayList<>();
                if(reasons != null){
                    for(String key : reasons.getKeys()){
                        try{
                            if(!config.contains("reasons."+key+".points.durations")){
                                config.set("reasons."+key+".points.durations.0.type",BanType.NETWORK.toString());
                                config.set("reasons."+key+".points.durations.0.time",-2);
                                config.set("reasons."+key+".points.durations.0.unit",TimeUnit.DAYS.toString());
                                YamlConfiguration.getProvider(YamlConfiguration.class).save(config,file);
                            }

                            Map<Integer,BanReasonEntry> templateDurations = new HashMap<>();
                            Configuration durationConfig = config.getSection("reasons."+key+".durations");
                            for(String DKey : durationConfig.getKeys()){
                                templateDurations.put(Integer.valueOf(DKey),new BanReasonEntry(
                                        BanType.valueOf(config.getString("reasons."+key+".durations."+DKey+".type"))
                                        ,config.getLong("reasons."+key+".durations."+DKey+".time")
                                        ,TimeUnit.valueOf(config.getString("reasons."+key+".durations."+DKey+".unit"))));
                            }
                            Map<Integer,BanReasonEntry> pointsDurations = new HashMap<>();
                            Configuration pointsConfig = config.getSection("reasons."+key+".points.durations");
                            for(String DKey : pointsConfig.getKeys()){
                                pointsDurations.put(Integer.valueOf(DKey),new BanReasonEntry(
                                        BanType.valueOf(config.getString("reasons."+key+".points.durations."+DKey+".type"))
                                        ,config.getLong("reasons."+key+".points.durations."+DKey+".time")
                                        ,TimeUnit.valueOf(config.getString("reasons."+key+".points.durations."+DKey+".unit"))));
                            }
                            this.banReasons.add(new BanReason(Integer.valueOf(key)
                                    ,new HistoryPoints(config.getInt("reasons."+key+".points.points")
                                    ,BanType.valueOf(config.getString("reasons."+key+".historytype")))
                                    ,config.getString("reasons."+key+".name")
                                    ,config.getString("reasons."+key+".display")
                                    ,config.getString("reasons."+key+".permission")
                                    ,config.getBoolean("reasons."+key+".hidden")
                                    ,config.getStringList("reasons."+key+".points.aliases")
                                    ,loadProperties(config,"reasons."+key+".properties")
                                    ,config.getDouble("reasons."+key+".points.divider")
                                    ,BanType.valueOf(config.getString("reasons."+key+".historytype"))
                                    ,templateDurations,pointsDurations));
                        }catch (Exception exception){
                            exception.printStackTrace();
                            System.out.println(Messages.SYSTEM_PREFIX+"Could not load ban-reason "+key);
                            System.out.println(Messages.SYSTEM_PREFIX+"Error: "+exception.getMessage());
                        }
                    }
                }
                if(this.banReasons.size() > 0) return;
            }catch (Exception exception){
                if(file.exists()){
                    file.renameTo(new File(this.platform.getFolder(),"ban-reasons-old-"+GeneralUtil.getRandomString(15)+".yml"));
                    System.out.println(Messages.SYSTEM_PREFIX+"Could not load ban-reasons, generating new (Saved als old)");
                    System.out.println(Messages.SYSTEM_PREFIX+"Error: "+exception.getMessage());
                }
            }
        }
        config = new Configuration();

        Map<Integer,BanReasonEntry> pointsDurations = new HashMap<>();

        pointsDurations.put(0,new BanReasonEntry(BanType.NETWORK,-2, TimeUnit.DAYS));
        this.banReasons.add(new BanReason(1,new HistoryPoints(30,BanType.NETWORK),"Hacking","Hacking","dkbans.ban.reason.hacking"
                ,false, Arrays.asList("hacks","hacker"),new Document(),0.0, BanType.NETWORK
                ,pointsDurations
                ,new BanReasonEntry(BanType.NETWORK,30, TimeUnit.DAYS)
                ,new BanReasonEntry(BanType.NETWORK,60, TimeUnit.DAYS)
                ,new BanReasonEntry(BanType.NETWORK,90, TimeUnit.DAYS)
                ,new BanReasonEntry(BanType.NETWORK,-1, TimeUnit.DAYS)));

        pointsDurations.clear();
        pointsDurations.put(0,new BanReasonEntry(BanType.CHAT,30, TimeUnit.MINUTES));
        pointsDurations.put(5,new BanReasonEntry(BanType.CHAT,-2, TimeUnit.MINUTES));
        pointsDurations.put(50,new BanReasonEntry(BanType.NETWORK,-2, TimeUnit.MINUTES));
        this.banReasons.add(new BanReason(2,new HistoryPoints(7,BanType.CHAT),"Provocation","Provocation","dkbans.ban.reason.provocation"
                ,false, Arrays.asList("provocation","provo"),new Document(),2,BanType.CHAT
                ,pointsDurations
                ,new BanReasonEntry(BanType.CHAT,30,TimeUnit.MINUTES)
                ,new BanReasonEntry(BanType.CHAT,4,TimeUnit.HOURS)
                ,new BanReasonEntry(BanType.CHAT,10,TimeUnit.DAYS)
                ,new BanReasonEntry(BanType.CHAT,30,TimeUnit.DAYS)
                ,new BanReasonEntry(BanType.NETWORK,10,TimeUnit.DAYS)
                ,new BanReasonEntry(BanType.NETWORK,30,TimeUnit.DAYS)));

        pointsDurations.clear();
        pointsDurations.put(0,new BanReasonEntry(BanType.CHAT,5, TimeUnit.MINUTES));
        pointsDurations.put(9,new BanReasonEntry(BanType.CHAT,-2, TimeUnit.MINUTES));
        pointsDurations.put(40,new BanReasonEntry(BanType.NETWORK,-2, TimeUnit.MINUTES));
        this.banReasons.add(new BanReason(3,new HistoryPoints(9,BanType.CHAT),"Insult","Insult","dkbans.ban.reason.insult"
                ,false, Arrays.asList("insult"),new Document(),0,BanType.CHAT
                ,pointsDurations
                ,new BanReasonEntry(BanType.CHAT,5,TimeUnit.HOURS)
                ,new BanReasonEntry(BanType.CHAT,1,TimeUnit.DAYS)
                ,new BanReasonEntry(BanType.CHAT,10,TimeUnit.DAYS)
                ,new BanReasonEntry(BanType.CHAT,30,TimeUnit.DAYS)
                ,new BanReasonEntry(BanType.NETWORK,10,TimeUnit.DAYS)
                ,new BanReasonEntry(BanType.NETWORK,30,TimeUnit.DAYS)));

        pointsDurations.clear();
        pointsDurations.put(0,new BanReasonEntry(BanType.NETWORK,5, TimeUnit.MINUTES));
        pointsDurations.put(9,new BanReasonEntry(BanType.NETWORK,-2, TimeUnit.MINUTES));
        this.banReasons.add(new BanReason(4,new HistoryPoints(9,BanType.NETWORK),"Spam/Promotion","Spam/Promotion","dkbans.ban.reason.promotion"
                ,false, Arrays.asList("spam","spamming"),new Document(),3,BanType.NETWORK
                ,pointsDurations
                ,new BanReasonEntry(BanType.NETWORK,3,TimeUnit.DAYS)
                ,new BanReasonEntry(BanType.NETWORK,10,TimeUnit.DAYS)
                ,new BanReasonEntry(BanType.NETWORK,30,TimeUnit.DAYS)
                ,new BanReasonEntry(BanType.NETWORK,-1,TimeUnit.DAYS)));

        this.banReasons.add(new BanReason(5,new HistoryPoints(60,BanType.NETWORK),"Permanently","Permanently","dkbans.ban.reason.permanent"
                ,false, Arrays.asList("perma","perm"),new Document(),0.0,BanType.NETWORK
                ,pointsDurations
                ,new BanReasonEntry(BanType.NETWORK,-1,TimeUnit.DAYS)));
        for(BanReason reason : this.banReasons){
            config.set("reasons."+reason.getID()+".name",reason.getName());
            config.set("reasons."+reason.getID()+".display",reason.getRawDisplay());
            config.set("reasons."+reason.getID()+".permission",reason.getPermission());
            config.set("reasons."+reason.getID()+".aliases",reason.getAliases());
            config.set("reasons."+reason.getID()+".hidden",reason.isHidden());
            config.set("reasons."+reason.getID()+".historytype",reason.getHistoryType().toString());
            for(Map.Entry<Integer, BanReasonEntry> entry : reason.getTemplateDurations().entrySet()){
                config.set("reasons."+reason.getID()+".durations."+entry.getKey()+".type",entry.getValue().getType().toString());
                config.set("reasons."+reason.getID()+".durations."+entry.getKey()+".time",entry.getValue().getDuration().getTime());
                config.set("reasons."+reason.getID()+".durations."+entry.getKey()+".unit",entry.getValue().getDuration().getUnit().toString());
            }
            config.set("reasons."+reason.getID()+".points.points",reason.getPoints().getPoints());
            config.set("reasons."+reason.getID()+".points.divider",reason.getDivider());
            for(Map.Entry<Integer, BanReasonEntry> entry : reason.getPointsDurations().entrySet()){
                config.set("reasons."+reason.getID()+".points.durations."+entry.getKey()+".type",entry.getValue().getType().toString());
                config.set("reasons."+reason.getID()+".points.durations."+entry.getKey()+".time",entry.getValue().getDuration().getTime());
                config.set("reasons."+reason.getID()+".points.durations."+entry.getKey()+".unit",entry.getValue().getDuration().getUnit().toString());
            }
            config.set("reasons."+reason.getID()+".properties.MyProperty","Hey");
            config.set("reasons."+reason.getID()+".properties.AnotherProperty",10);
        }
        try{
            YamlConfiguration.getProvider(YamlConfiguration.class).save(config,file);
        }catch (Exception e){}
    }
    public void loadUnbanReasons(){
        File file = new File(this.platform.getFolder(),"unban-reasons.yml");
        Configuration config = null;
        if(file.exists()){
            try{
                config = YamlConfiguration.getProvider(YamlConfiguration.class).load(file);
                Configuration reasons = config.getSection("reasons");
                this.unbanReasons = new ArrayList<>();
                if(reasons != null){
                    for(String key : reasons.getKeys()){
                        try{
                            this.unbanReasons.add(new UnbanReason(Integer.valueOf(key)
                                    ,new HistoryPoints(config.getInt("reasons."+key+".points.remove.points"),BanType.NETWORK)
                                    ,config.getString("reasons."+key+".name")
                                    ,config.getString("reasons."+key+".display")
                                    ,config.getString("reasons."+key+".permission")
                                    ,config.getBoolean("reasons."+key+".hidden")
                                    ,config.getStringList("reasons."+key+".aliases")
                                    ,loadProperties(config,"reasons."+key+".properties")
                                    ,config.getInt("reasons."+key+".points.maximal")
                                    ,config.getBoolean("reasons."+key+".remove.all")
                                    ,config.getIntList("reasons."+key+".notforbanid")
                                    ,new Duration(config.getLong("reasons."+key+".duration.maximal.time")
                                    ,TimeUnit.valueOf(config.getString("reasons."+key+".duration.maximal.unit")))
                                    ,new Duration(config.getLong("reasons."+key+".duration.remove.time")
                                    ,TimeUnit.valueOf(config.getString("reasons."+key+".duration.remove.unit")))
                                    ,config.getDouble("reasons."+key+".duration.divider")
                                    ,config.getDouble("reasons."+key+".points.divider")
                                    ,BanType.parseNull(config.getString("reasons."+key+".bantype"))));
                        }catch (Exception exception){
                            exception.printStackTrace();
                            System.out.println(Messages.SYSTEM_PREFIX+"Could not load unban-reason "+key);
                            System.out.println(Messages.SYSTEM_PREFIX+"Error: "+exception.getMessage());
                        }
                    }
                }
                if(this.unbanReasons.size() > 0) return;
            }catch (Exception exception){
                if(file.exists()){
                    file.renameTo(new File(this.platform.getFolder(),"unban-reasons-old-"+GeneralUtil.getRandomString(15)+".yml"));
                    System.out.println(Messages.SYSTEM_PREFIX+"Could not load unban-reasons, generating new (Saved als old)");
                }
            }
        } //int id, int points, String name, String display, String permission, boolean hidden, List<String> aliases, int maxPoints, boolean removeAllPoints, List<Integer> notForBanID, Duration maxDuration, Duration removeDuration, double durationDivider
        config = new Configuration();
        this.unbanReasons.add(new UnbanReason(1,new HistoryPoints(0,BanType.NETWORK),"falseban","False ban","dkbans.unban.reason.falsban"
                ,false, Arrays.asList("false"),new Document(),120,true,Arrays.asList()
                ,new Duration(-1,TimeUnit.DAYS),new Duration(-1,TimeUnit.DAYS),0D,0D,null));

        this.unbanReasons.add(new UnbanReason(2,new HistoryPoints(0,BanType.NETWORK),"acceptedrequest","Accepted unban request","dkbans.unban.reason.acceptedrequest"
                ,false, Arrays.asList("accepted"),new Document(),100,true,Arrays.asList(),new Duration(-1,TimeUnit.DAYS)
                ,new Duration(-1,TimeUnit.DAYS),2D,1.5D,null));

        this.unbanReasons.add(new UnbanReason(3,new HistoryPoints(4,BanType.NETWORK),"-3days","-3 Days","dkbans.unban.reason.falsban"
                ,false, Arrays.asList("-3days"),new Document(),30,true,Arrays.asList(5),new Duration(-1,TimeUnit.DAYS)
                ,new Duration(3,TimeUnit.DAYS),0D,0D,null));


        for(UnbanReason reason : this.unbanReasons){
            config.set("reasons."+reason.getID()+".name",reason.getName());
            config.set("reasons."+reason.getID()+".display",reason.getRawDisplay());
            config.set("reasons."+reason.getID()+".permission",reason.getPermission());
            config.set("reasons."+reason.getID()+".aliases",reason.getAliases());
            config.set("reasons."+reason.getID()+".hidden",reason.isHidden());
            config.set("reasons."+reason.getID()+".notforbanid",reason.getNotForBanID());
            config.set("reasons."+reason.getID()+".bantype",reason.getBanType()==null?"ALL":reason.getBanType());
            config.set("reasons."+reason.getID()+".points.remove.points",reason.getPoints().getPoints());
            config.set("reasons."+reason.getID()+".points.remove.all",reason.isRemoveAllPoints());
            config.set("reasons."+reason.getID()+".points.maximal",reason.getMaxPoints());
            config.set("reasons."+reason.getID()+".points.divider",reason.getPointsDivider());
            config.set("reasons."+reason.getID()+".duration.divider",reason.getDurationDivider());
            config.set("reasons."+reason.getID()+".duration.remove.time",reason.getRemoveDuration().getTime());
            config.set("reasons."+reason.getID()+".duration.remove.unit",reason.getRemoveDuration().getUnit().toString());
            config.set("reasons."+reason.getID()+".duration.maximal.time",reason.getMaxDuration().getTime());
            config.set("reasons."+reason.getID()+".duration.maximal.unit",reason.getMaxDuration().getUnit().toString());
            config.set("reasons."+reason.getID()+".properties.MyProperty","Hey");
            config.set("reasons."+reason.getID()+".properties.AnotherProperty",10);
        }
        try{
            YamlConfiguration.getProvider(YamlConfiguration.class).save(config,file);
        }catch (Exception e){}
    }
    public void loadReportReasons(){
        File file = new File(this.platform.getFolder(),"report-reasons.yml");
        Configuration config = null;
        if(file.exists()){
            try{
                config = YamlConfiguration.getProvider(YamlConfiguration.class).load(file);
                Configuration reasons = config.getSection("reasons");
                this.reportReasons = new ArrayList<>();
                if(reasons != null) {
                    for (String key : reasons.getKeys()) {
                        try{
                            this.reportReasons.add(new ReportReason(Integer.valueOf(key)
                                    ,config.getString("reasons."+key+".name")
                                    ,config.getString("reasons."+key+".display")
                                    ,config.getString("reasons."+key+".permission")
                                    ,config.getBoolean("reasons."+key+".hidden")
                                    ,config.getStringList("reasons."+key+".aliases")
                                    ,loadProperties(config,"reasons."+key+".properties")
                                    ,config.getInt("reasons."+key+".forbanreason")));
                        }catch (Exception exception){
                            exception.printStackTrace();
                            System.out.println(Messages.SYSTEM_PREFIX+"Could not load report-reason "+key);
                            System.out.println(Messages.SYSTEM_PREFIX+"Error: "+exception.getMessage());
                        }
                    }
                }
                if(this.reportReasons.size() > 0) return;
            }catch (Exception exception){
                if(file.exists()){
                    file.renameTo(new File(this.platform.getFolder(),"report-reasons-old-"+GeneralUtil.getRandomString(15)+".yml"));
                    System.out.println(Messages.SYSTEM_PREFIX+"Could not load report-reasons, generating new (Saved als old)");
                    System.out.println(Messages.SYSTEM_PREFIX+"Error: "+exception.getMessage());
                }
            }
        }
        config = new Configuration();
        this.reportReasons.add(new ReportReason(1,"Hacking","Hacking","dkbans.report.reason.hacking"
                ,false,Arrays.asList("hacks","hacker"),new Document(),1));
        this.reportReasons.add(new ReportReason(2,"Provocation","Provocation","dkbans.report.reason.provocation"
                ,false,Arrays.asList("provocation","provo"),new Document(),2));
        this.reportReasons.add(new ReportReason(3,"Insult","Insult","dkbans.report.reason.insult"
                ,false,Arrays.asList("insult"),new Document(),3));
        this.reportReasons.add(new ReportReason(4,"Spam/Provocation","Spam/Promotion","dkbans.report.reason.promotion"
                ,false,Arrays.asList("spam","spamming"),new Document(),4));

        for(ReportReason reason : this.reportReasons){
            config.set("reasons."+reason.getID()+".name",reason.getName());
            config.set("reasons."+reason.getID()+".display",reason.getRawDisplay());
            config.set("reasons."+reason.getID()+".permission",reason.getPermission());
            config.set("reasons."+reason.getID()+".aliases",reason.getAliases());
            config.set("reasons."+reason.getID()+".hidden",reason.isHidden());
            config.set("reasons."+reason.getID()+".forbanreason",reason.getForBan());
            config.set("reasons."+reason.getID()+".properties.MyProperty","Hey");
            config.set("reasons."+reason.getID()+".properties.AnotherProperty",10);
        }
        try{
            YamlConfiguration.getProvider(YamlConfiguration.class).save(config,file);
        }catch (Exception e){}
    }
    public void loadKickReasons(){
        File file = new File(this.platform.getFolder(),"kick-reasons.yml");
        Configuration config = null;
        if(file.exists()){
            try{
                config = YamlConfiguration.getProvider(YamlConfiguration.class).load(file);
                Configuration reasons = config.getSection("reasons");
                this.kickReasons= new ArrayList<>();
                if(reasons != null) {
                    for(String key : reasons.getKeys()) {
                        try{
                            this.kickReasons.add(new KickReason(Integer.valueOf(key)
                                    ,new HistoryPoints(config.getInt("reasons."+key+".points.points")
                                    ,BanType.valueOf(config.getString("reasons."+key+".points.type")))
                                    ,config.getString("reasons."+key+".name")
                                    ,config.getString("reasons."+key+".display")
                                    ,config.getString("reasons."+key+".permission")
                                    ,config.getBoolean("reasons."+key+".hidden")
                                    ,config.getStringList("reasons."+key+".aliases")
                                    ,loadProperties(config,"reasons."+key+".properties")));
                        }catch (Exception exception){
                            exception.printStackTrace();
                            System.out.println(Messages.SYSTEM_PREFIX+"Could not load kick-reason "+key);
                            System.out.println(Messages.SYSTEM_PREFIX+"Error: "+exception.getMessage());
                        }
                    }
                }
                if(this.kickReasons.size() > 0) return;
            }catch (Exception exception){
                if(file.exists()){
                    file.renameTo(new File(this.platform.getFolder(),"kick-reasons-old-"+GeneralUtil.getRandomString(15)+".yml"));
                    System.out.println(Messages.SYSTEM_PREFIX+"Could not load kick-reasons, generating new (Saved als old)");
                    exception.printStackTrace();
                }
            }
        }
        config = new Configuration();
        this.kickReasons.add(new KickReason(1,new HistoryPoints(3,BanType.CHAT),"Provocation","Provocation","dkbans.kick.reason.provocation"
                ,false,Arrays.asList("provocation","provo"),new Document()));
        this.kickReasons.add(new KickReason(2,new HistoryPoints(5,BanType.CHAT),"Insult","Insult","dkbans.kick.reason.insult"
                ,false,Arrays.asList("insult"),new Document()));
        this.kickReasons.add(new KickReason(3,new HistoryPoints(3,BanType.NETWORK),"Bugusing","Bugusing","dkbans.kick.reason.bugusing"
                ,false, Arrays.asList("bug","bugging"),new Document()));
        this.kickReasons.add(new KickReason(4,new HistoryPoints(0,BanType.CHAT),"Annoy","Annoy","dkbans.kick.reason.annoy"
                ,false, Arrays.asList("annoy"),new Document()));

        for(KickReason reason : this.kickReasons){
            config.set("reasons."+reason.getID()+".name",reason.getName());
            config.set("reasons."+reason.getID()+".display",reason.getRawDisplay());
            config.set("reasons."+reason.getID()+".permission",reason.getPermission());
            config.set("reasons."+reason.getID()+".aliases",reason.getAliases());
            config.set("reasons."+reason.getID()+".hidden",reason.isHidden());
            config.set("reasons."+reason.getID()+".points.points",reason.getPoints().getPoints());
            config.set("reasons."+reason.getID()+".points.type",reason.getPoints().getHistoryType().toString());
            config.set("reasons."+reason.getID()+".properties.MyProperty","Hey");
            config.set("reasons."+reason.getID()+".properties.AnotherProperty",10);
        }
        try{
            YamlConfiguration.getProvider(YamlConfiguration.class).save(config,file);
        }catch (Exception e){}
    }
    public void loadWarnReasons(){
        File file = new File(this.platform.getFolder(),"warn-reasons.yml");
        Configuration config = null;
        if(file.exists()){
            try{
                config = YamlConfiguration.getProvider(YamlConfiguration.class).load(file);
                Configuration reasons = config.getSection("reasons");
                this.warnReasons= new ArrayList<>();
                if(reasons != null) {
                    for (String key : reasons.getKeys()) {
                        try{
                            this.warnReasons.add(new WarnReason(Integer.valueOf(key)
                                    ,new HistoryPoints(config.getInt("reasons."+key+".points.points")
                                    ,BanType.valueOf(config.getString("reasons."+key+".points.type")))
                                    ,config.getString("reasons."+key+".name")
                                    ,config.getString("reasons."+key+".display")
                                    ,config.getString("reasons."+key+".permission")
                                    ,config.getBoolean("reasons."+key+".hidden")
                                    ,config.getStringList("reasons."+key+".aliases")
                                    ,loadProperties(config,"reasons."+key+".properties")
                                    ,config.getInt("reasons."+key+".autoban.count")
                                    ,config.getInt("reasons."+key+".autoban.banid"),config.getInt("reasons."+key+".kickcount")));
                        }catch (Exception exception){
                            exception.printStackTrace();
                            System.out.println(Messages.SYSTEM_PREFIX+"Could not load warn-reason "+key);
                            System.out.println(Messages.SYSTEM_PREFIX+"Error: "+exception.getMessage());
                        }
                    }
                }
                if(this.warnReasons.size() > 0) return;
            }catch (Exception exception){
                if(file.exists()){
                    file.renameTo(new File(this.platform.getFolder(),"warn-reasons-old-"+GeneralUtil.getRandomString(15)+".yml"));
                    System.out.println(Messages.SYSTEM_PREFIX+"Could not load warn-reasons, generating new (Saved als old)");
                }
            }
        }
        config = new Configuration();
        this.warnReasons.add(new WarnReason(1,new HistoryPoints(3,BanType.CHAT),"Provocation","Provocation","dkbans.warn.reason.provocation"
                ,false,Arrays.asList("provocation","provo"),new Document(),3,2,2));
        this.warnReasons.add(new WarnReason(2,new HistoryPoints(4,BanType.CHAT),"Insult","Insult","dkbans.warn.reason.insult"
                ,false,Arrays.asList("insult"),new Document(),3,3,2));
        this.warnReasons.add(new WarnReason(3,new HistoryPoints(8,BanType.NETWORK),"Spam/promotion","Spam/Promotion","dkbans.warn.reason.promotion"
                ,false,Arrays.asList("spamming","spam","promotion"),new Document(),2,4,2));

        for(WarnReason reason : this.warnReasons){
            config.set("reasons."+reason.getID()+".name",reason.getName());
            config.set("reasons."+reason.getID()+".display",reason.getRawDisplay());
            config.set("reasons."+reason.getID()+".permission",reason.getPermission());
            config.set("reasons."+reason.getID()+".aliases",reason.getAliases());
            config.set("reasons."+reason.getID()+".hidden",reason.isHidden());
            config.set("reasons."+reason.getID()+".points.points",reason.getPoints().getPoints());
            config.set("reasons."+reason.getID()+".points.type",reason.getPoints().getHistoryType().toString());
            config.set("reasons."+reason.getID()+".autoban.count",reason.getAutoBanCount());
            config.set("reasons."+reason.getID()+".autoban.banid",reason.getForBan());
            config.set("reasons."+reason.getID()+".kickcount",reason.getKickFrom());
            config.set("reasons."+reason.getID()+".properties.MyProperty","Hey");
            config.set("reasons."+reason.getID()+".properties.AnotherProperty",10);
        }
        try{
            YamlConfiguration.getProvider(YamlConfiguration.class).save(config,file);
        }catch (Exception e){}
    }
    private Document loadProperties(Configuration configuration, String path){
        try{
            return new Document(loadSubJsonObject(configuration.getSection(path)));
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return new Document();
    }
    private JsonObject loadSubJsonObject(Configuration section){
        JsonObject object = new JsonObject();
        for(String key : section.getKeys()){
            try{
                Configuration subSection = section.getSection(key);
                object.add(key,loadSubJsonObject(subSection));
            }catch (Exception exception){
                object.addProperty(key,section.getString(key));
            }
        }
        return object;
    }
}

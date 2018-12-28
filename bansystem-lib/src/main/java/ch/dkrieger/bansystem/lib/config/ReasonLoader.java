package ch.dkrieger.bansystem.lib.config;

import ch.dkrieger.bansystem.lib.DKBansPlatform;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.reason.BanReason;
import ch.dkrieger.bansystem.lib.reason.BanReasonEntry;
import ch.dkrieger.bansystem.lib.reason.ReasonProvider;
import ch.dkrieger.bansystem.lib.utils.Document;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ReasonLoader {

    private  DKBansPlatform platform;

    public static void main(String[] args){
        getBanReasonData();
    }

    public ReasonLoader(DKBansPlatform platform) {
        this.platform = platform;
    }
    public DKBansPlatform getPlatform() {
        return platform;
    }
    public ReasonProvider loadReasons(){
        return null;
    }
    public static List<BanReason> getBanReasonData(){
        File file = new File("banreasons.json");
        if(file.exists()){
            try{
                Document data = Document.loadData(file);
                if(data.contains("reasons")){
                    List<BanReason> reasons = data.getObject("reasons",new TypeToken<List<BanReason>>(){}.getType());
                    if(reasons != null && reasons.size() > 0){
                        reasons.sort((r1, r2) -> {
                            if(r1.getID() < r2.getID()) return -1;
                            else return 1;
                        });
                        return reasons;
                    }
                }
            }catch (Exception exception){
                System.err.println(Messages.SYSTEM_PREFIX+" Could not load banreasons.json, check your config.");
                System.err.println(Messages.SYSTEM_PREFIX+" Generating defualt reasons.");
                file.renameTo(new File("banreasons-error-"+System.currentTimeMillis()));
            }
        }
        List<BanReason> reasons = new LinkedList<>();
        reasons.add(new BanReason(1,30,"hacking","&4Hacking","dkbans.ban.reason.hacking"
                ,false, Arrays.asList("cheating"),0,BanType.NETWORK
                ,new BanReasonEntry(BanType.NETWORK,30, TimeUnit.DAYS)
                ,new BanReasonEntry(BanType.NETWORK,60, TimeUnit.DAYS)
                ,new BanReasonEntry(BanType.NETWORK,-1, TimeUnit.DAYS)));

        reasons.add(new BanReason(1,30,"Provocation","&4Provocation","dkbans.ban.reason.provocation"
                ,false, Arrays.asList("provo","pr"),0,BanType.CHAT
                ,new BanReasonEntry(BanType.CHAT,12, TimeUnit.HOURS)
                ,new BanReasonEntry(BanType.CHAT,3, TimeUnit.DAYS)
                ,new BanReasonEntry(BanType.CHAT,30, TimeUnit.DAYS)
                ,new BanReasonEntry(BanType.CHAT,60, TimeUnit.DAYS)
                ,new BanReasonEntry(BanType.CHAT,90, TimeUnit.DAYS)
                ,new BanReasonEntry(BanType.NETWORK,60, TimeUnit.DAYS)));
        new Document().append("reasons",reasons).saveData(file);
        return reasons;
    }


}

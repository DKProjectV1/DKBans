package ch.dkrieger.bansystem.restapi;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.config.Config;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

public class DKBansRestApiConfig {

    public List<String> accessTokens;
    public List<String> ipWhitelist;
    public InetSocketAddress address;
    public Boolean sslEnabled, sslCustom;
    public File sslCertificate, sslPrivateKey;

    public DKBansRestApiConfig() {
        Config config = BanSystem.getInstance().getConfig();
        this.accessTokens = config.addAndGetStringListValue("extension.restapi.accesstokens", Arrays.asList(GeneralUtil.getRandomString(40)));
        this.ipWhitelist = config.addAndGetStringListValue("extension.restapi.ipwhitelist", Arrays.asList("127.0.0.1",getAddress()));
        this.address = new InetSocketAddress(config.addAndGetStringValue("extension.restapi.address.host","0.0.0.0")
                ,config.addAndGetIntValue("extension.restapi.address.port",8080));
        this.sslEnabled = config.addAndGetBooleanValue("extension.restapi.ssl",false);
        this.sslCustom = config.addAndGetBooleanValue("extension.restapi.custom.enabled",false);
        this.sslCertificate = new File(config.addAndGetStringValue("extension.restapi.custom.certificate","certificate.txt"));
        this.sslPrivateKey = new File(config.addAndGetStringValue("extension.restapi.custom.privatekey","private.key"));
        config.save();
    }
    private String getAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        }catch (Exception exception) {
            return "0.0.0.0";
        }
    }
}

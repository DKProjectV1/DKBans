/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 15.07.19 11:31
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

package ch.dkrieger.bansystem.extension.restapi;

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

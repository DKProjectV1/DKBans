/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 01.01.19 13:25
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

package ch.dkrieger.bansystem.extension.restapi.handler;

import ch.dkrieger.bansystem.lib.utils.Document;

public abstract class RestApiHandler {

    private final String path;

    public RestApiHandler(String path) {
        if(path.contains(".")) path = path.substring(0,path.indexOf("."));
        if(!path.startsWith("/")) path = "/"+path;
        this.path = path;
    }
    public String getPath() {
        return this.path;
    }
    public abstract void onRequest(Document query, Document response);

}

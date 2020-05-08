/*
 * (C) Copyright 2020 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 08.05.20, 19:58
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

package ch.dkrieger.bansystem.lib.player.history;

import ch.dkrieger.bansystem.lib.Messages;

public enum BanType {

    NETWORK(),
    CHAT();

    public String getDisplay(){
        if(this == CHAT) return Messages.BAN_TYPE_CHAT;
        else return Messages.BAN_TYPE_NETWORK;
    }

    public static BanType parse(String parse){
        try{
            return valueOf(parse.toUpperCase());
        }catch (Exception ignored){}
        throw new IllegalArgumentException("Invalid ban type");
    }
    public static BanType parseNull(String parse){
        try{
            return valueOf(parse.toUpperCase());
        }catch (Exception exception){}
        return null;
    }
}

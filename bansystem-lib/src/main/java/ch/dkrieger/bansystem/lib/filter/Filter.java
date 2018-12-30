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

package ch.dkrieger.bansystem.lib.filter;

public class Filter {

    private int id;
    private String message;
    private FilterOperation operation;
    private FilterType type;

    public Filter(int id, String message, FilterOperation operation, FilterType type) {
        if(message.startsWith("/")) message = message.substring(1);
        this.id = id;
        this.message = message.toLowerCase();
        this.operation = operation;
        this.type = type;
    }

    public int getID() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public FilterOperation getOperation() {
        return operation;
    }

    public FilterType getType() {
        return type;
    }

    @SuppressWarnings("Only for creatin id changing")
    public void setID(int id) {
        this.id = id;
    }

    public boolean isBocked(String message){
        if(message.startsWith("/")) message = message.substring(1);
        message = message.toLowerCase().replaceFirst("/","");
        if(this.operation == FilterOperation.CONTAINS) return message.replace(" ","").contains(this.message);
        else{
            String[] words = null;
            if(message.contains(" ")) words = message.split(" ");
            else words = new String[]{message};
            if(this.operation == FilterOperation.EQUALS) {
                for(String word : words) if (word.equalsIgnoreCase(this.message)) return true;
            }else if(this.operation == FilterOperation.STARTSWITH){
                for(String word : words) if (word.startsWith(this.message)) return true;
            }else if(this.operation == FilterOperation.ENDSWITH){
                for(String word : words) if (word.endsWith(this.message)) return true;
            }
        }
        return false;
    }
}

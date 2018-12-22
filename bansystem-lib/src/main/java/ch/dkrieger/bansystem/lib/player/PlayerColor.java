package ch.dkrieger.bansystem.lib.player;

/*
 * Copyright (c) 2018 Davide Wietlisbach created on 29.01.18 17:42
 */

public class PlayerColor {

    private String permission, color;

    public PlayerColor(String permission, String color){
        this.permission = permission;
        this.color = color;
    }
    public String getPermission(){
        return this.permission;
    }
    public String getColor(){
        return this.color;
    }
}

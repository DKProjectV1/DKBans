package de.fridious.bansystem.extension.gui.api.inventory.gui;

import de.fridious.bansystem.extension.gui.api.inventory.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

/*
 * (C) Copyright 2018 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 30.12.18 20:25
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

public abstract class GUI implements InventoryHolder {

    private Inventory inventory;

    protected GUI(){

    }

    public GUI(String name, int size){
        this.inventory = Bukkit.createInventory(this,size,name);
    }

    public ItemStack getItem(int place){
        return this.inventory.getItem(place);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setItem(int place, ItemStack item){
        this.inventory.setItem(place,item);
    }

    public void setItem(int place, ItemBuilder itembuilder){
        this.inventory.setItem(place,itembuilder.build());
    }

    public void removeItem(int place){
        this.inventory.setItem(place,null);
    }

    public void addItem(ItemStack item){
        this.inventory.addItem(item);
    }

    public void addItem(ItemBuilder itembuilder){
        addItem(itembuilder.build());
    }

    public void fill(ItemBuilder builder){
        fill(builder.build());
    }

    public void fill(ItemStack item){
        for(int i = 0; i < inventory.getContents().length; i++){
            if(this.inventory.getItem(i) == null || this.inventory.getItem(i).getType() == Material.AIR)
                this.inventory.setItem(i,item);
        }
    }

    public void clear(){
        this.inventory.clear();
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void createInventory(String name, int size) {
        this.inventory = Bukkit.createInventory(this,size,name);
    }

    public void clear(int startPlace, int lastPlace){
        for(int i = startPlace; i <= lastPlace;i++) removeItem(i);
    }

    public void open(Player player){
        if(player.getOpenInventory().getTopInventory().equals(inventory))return;
        player.openInventory(this.inventory);
    }

    public void handleClick(InventoryClickEvent event){
        if(this instanceof PrivateGUI){
            Player clicker = (Player) event.getWhoClicked();
            if(((PrivateGUI)this).getOwner() == clicker) onClick(event);
        }else onClick(event);
    }

    public void handleClose(InventoryCloseEvent event){
        if(this instanceof PrivateGUI){
            Player clicker = (Player) event.getPlayer();
            if(((PrivateGUI)this).getOwner() == clicker) onClose(event);
        }else onClose(event);
    }

    public void handleOpen(InventoryOpenEvent event) {
        if(this instanceof PrivateGUI){
            Player clicker = (Player) event.getPlayer();
            if(((PrivateGUI)this).getOwner() == clicker) onOpen(event);
        }else onOpen(event);
    }

    protected abstract void onOpen(InventoryOpenEvent event);
    protected abstract void onClick(InventoryClickEvent event);
    protected abstract void onClose(InventoryCloseEvent event);
}
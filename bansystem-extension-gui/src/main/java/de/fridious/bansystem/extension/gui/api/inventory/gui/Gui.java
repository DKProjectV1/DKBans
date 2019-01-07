package de.fridious.bansystem.extension.gui.api.inventory.gui;

import ch.dkrieger.bansystem.lib.utils.Document;
import de.fridious.bansystem.extension.gui.DKBansGuiExtension;
import de.fridious.bansystem.extension.gui.api.inventory.item.ItemBuilder;
import de.fridious.bansystem.extension.gui.api.inventory.item.ItemStorage;
import de.fridious.bansystem.extension.gui.guis.GuiData;
import de.fridious.bansystem.extension.gui.utils.GuiExtensionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

public abstract class Gui<T> implements InventoryHolder {

    protected Inventory inventory;
    private int currentPage, pageSize;
    private List<? extends T> pageEntries;
    private Map<Integer, T> entryBySlot;
    private List<Class<? extends Event>> updateEvents;
    private String message;
    private boolean childGui;
    private final Map<String, Object> settings;

    protected Gui() {
        this.currentPage = 1;
        this.pageSize = 36;
        this.updateEvents = new LinkedList<>();
        this.settings = new ConcurrentHashMap<>();
        this.message = "";
        this.childGui = false;
    }

    public Gui(Inventory inventory) {
        this();
        this.inventory = inventory;
    }

    public Gui(String name, int size){
        this();
        this.inventory = Bukkit.createInventory(this,size,name);
    }

    public Gui(int size) {
        this(size, replace -> replace);
    }

    public Gui(int size, ItemStorage.StringReplacer stringReplacer) {
        this();
        GuiData guiData = DKBansGuiExtension.getInstance().getGuiManager().getInventoryData().get(this.getClass());
        this.inventory = Bukkit.createInventory(this, size, stringReplacer.replace(guiData.getTitle()));
        this.updateEvents.addAll(guiData.getUpdateEvents());
        this.settings.putAll(guiData.getSettings());
    }

    public Gui(InventoryType inventoryType) {
        this();
        this.inventory = Bukkit.createInventory(this, inventoryType);
    }

    public boolean hasChildGui() {
        return childGui;
    }

    public String getMessage() {
        return message;
    }

    public ItemStack getItem(int place){
        return this.inventory.getItem(place);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Map<String, Object> getSettings() {
        return settings;
    }

    /**
     * This method is for anvil guis and retun the input string of the anvil
     * It might be null, if the player doesn't edit the title in the anvil
     * @return String of input in anvil
     */
    public String getInput() {
        ItemStack itemStack = getItem(AnvilSlot.OUTPUT);
        if(itemStack == null) return null;
        String input = itemStack.getItemMeta().hasDisplayName() ? itemStack.getItemMeta().getDisplayName() : " ";
        return input == null || input.equalsIgnoreCase(itemStack.getType().toString()) ? " " : input;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public boolean hasNextPage() {
        return (this.currentPage)*this.pageSize < this.pageEntries.size();
    }

    public boolean hasPreviousPage() {
        return this.currentPage > 1;
    }

    public List<? extends T> getPageEntries() {
        return pageEntries;
    }

    public int getMaxPages() {
        int maxPages = (int)Math.ceil((double)this.pageEntries.size()/this.pageSize);
        return maxPages < 1 ? 1 : maxPages;
    }

    public List<Class<? extends Event>> getUpdateEvents() {
        return updateEvents;
    }

    public void setPageEntries(List<? extends T> pageEntries) {
        this.entryBySlot = new LinkedHashMap<>();
        if(this.pageEntries == null || this.pageEntries.isEmpty()) {
            this.pageEntries = pageEntries;
            setPage(this.currentPage);
            setArea(ItemStorage.get("placeholder"), this.pageSize, this.pageSize+8);
        }else this.pageEntries = pageEntries;
    }

    public Map<Integer, T> getEntryBySlot() {
        return entryBySlot;
    }

    public void setChildGui(boolean childGui) {
        this.childGui = childGui;
    }

    public void setPage(int page) {
        if(getPageEntries() != null) {
            getEntryBySlot().clear();
            if(getMaxPages() < getCurrentPage()) {
                this.currentPage--;
                setPage(this.currentPage);
                return;
            }
            clear(0, (this.pageSize-1));
            clear(this.pageSize+9, this.pageSize+17);
            List<T> rangeList = GuiExtensionUtils.getListRange(getPageEntries(),
                    (page == 1 ? 0  : (page-1)*(this.pageSize-1)) + (page>1 ? (page-1) : 0),
                    (page == 1 ? (this.pageSize-1) : page*(this.pageSize-1)) + (page>1 ? (page-1) : 0));
            int slot = 0;
            for (T t : rangeList) {
                getEntryBySlot().put(slot, t);
                setPageItem(slot, t);
                slot++;
            }
            setPageSwitchItems();
        }
    }

    public void setPageItem(int slot, T t) {

    }

    public void beforeUpdatePage() {

    }

    public void afterUpdatePage() {

    }

    public void updatePage(Event event) {
        if(event == null || this.updateEvents.contains(event.getClass())) {
            beforeUpdatePage();
            setPage(this.currentPage);
            afterUpdatePage();
        }
    }

    public void setPageSwitchItems() {
        setItem(this.pageSize+13, ItemStorage.get("currentpage", this.currentPage, getMaxPages()));
        if(hasNextPage()) {
            setItem(this.pageSize+17, ItemStorage.get("nextpage", this.currentPage, getMaxPages()));
        } else if(hasPreviousPage()) {
            setItem(this.pageSize+17, ItemStorage.get("previouspage", this.currentPage, getMaxPages()));
            return;
        }
        if(hasPreviousPage()) {
            setItem(this.pageSize+16, ItemStorage.get("previouspage", this.currentPage, getMaxPages()));
        }
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setItem(ItemStack itemStack, int... places) {
        for(int place : places) setItem(place, itemStack);
    }

    public void setItem(ItemStack itemStack, List<Integer> places) {
        for(int place : places) setItem(place, itemStack);
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

    public void setArea(ItemBuilder itemBuilder, int startPlace, int lastPlace) {
        setArea(itemBuilder.build(), startPlace, lastPlace);
    }

    public void setArea(ItemStack itemStack, int startPlace, int lastPlace) {
        for(int i = startPlace; i <= lastPlace; i++) setItem(i, itemStack);
    }

    public void clear(){
        this.inventory.clear();
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void createInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void createInventory(String name, int size) {
        this.inventory = Bukkit.createInventory(this,size,name);
    }

    public void createInventory(InventoryType inventoryType) {
        this.inventory = Bukkit.createInventory(this, inventoryType);
    }

    public void clear(int startPlace, int lastPlace){
        for(int i = startPlace; i <= lastPlace;i++) removeItem(i);
    }

    public void clear(int slot) {
        removeItem(slot);
    }

    public void open(Player player){
        if(player.getOpenInventory().getTopInventory().equals(inventory))return;
        player.openInventory(this.inventory);
    }

    public void handleClick(InventoryClickEvent event) {
        if(this.pageEntries != null) {
            if(event.getSlot() == this.inventory.getSize()-1) {
                if(hasNextPage()) {
                    this.currentPage++;
                    updatePage(null);
                } else if(hasPreviousPage()) {
                    this.currentPage--;
                    updatePage(null);
                }
            } else if(event.getSlot() == this.inventory.getSize()-2) {
                if(hasPreviousPage()) {
                    this.currentPage--;
                    updatePage(null);
                }
            }
        }
        if(this instanceof PrivateGui){
            Player clicker = (Player) event.getWhoClicked();
            if(((PrivateGui)this).getOwner() == clicker) onClick(event);
        }else onClick(event);
    }

    public void handleClose(InventoryCloseEvent event){
        if(this instanceof PrivateGui){
            Player clicker = (Player) event.getPlayer();
            if(((PrivateGui)this).getOwner() == clicker) onClose(event);
        }else onClose(event);
    }

    public void handleOpen(InventoryOpenEvent event) {
        if(this instanceof PrivateGui){
            Player clicker = (Player) event.getPlayer();
            if(((PrivateGui)this).getOwner() == clicker) onOpen(event);
        }else onOpen(event);
    }

    protected abstract void onOpen(InventoryOpenEvent event);
    protected abstract void onClick(InventoryClickEvent event);
    protected abstract void onClose(InventoryCloseEvent event);
}
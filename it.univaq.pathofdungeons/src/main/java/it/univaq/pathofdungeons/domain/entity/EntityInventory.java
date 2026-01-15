package it.univaq.pathofdungeons.domain.entity;

import java.util.ArrayList;
import java.util.HashMap;

import it.univaq.pathofdungeons.domain.Loadout;
import it.univaq.pathofdungeons.domain.items.Consumable;
import it.univaq.pathofdungeons.domain.items.EquipSlots;
import it.univaq.pathofdungeons.domain.items.Item;
import it.univaq.pathofdungeons.domain.items.equippable.Equippable;

public class EntityInventory {
    private static final int EQUIP_SLOTS_NUM = 5;
    private static final int ITEM_SLOTS_NUM = 5;
    private HashMap<EquipSlots, Equippable> equips;
    private ArrayList<Equippable> equipSlots;
    private ArrayList<Consumable> itemSlots;
    private int numEquips;
    private int numItems;
    private int gold;

    public EntityInventory(){
        this.equips = new HashMap<>();
        for(EquipSlots s: EquipSlots.values()){
            equips.put(s, null);
        }
        equipSlots = new ArrayList<>(EQUIP_SLOTS_NUM);
        itemSlots = new ArrayList<>(ITEM_SLOTS_NUM);
        numEquips = 0;
        numItems = 0;
        gold = 0;
    }

    public EntityInventory(Loadout loadout){
        this();
        for(Equippable e: loadout.getAllSlots()){
            if(e != null) equips.put(e.getSlot(), e);
        }
    }

    public Equippable getItemSlot(EquipSlots s){ return equips.get(s); }

    public Equippable equipItem(Equippable e){
        Equippable equipped = equips.get(e.getSlot());
        equips.put(e.getSlot(), e);
        return equipped;
    }

    public boolean addToEquipSlots(Equippable e){
        if(this.isEquipSlotsFull()) return false;
        if(this.tryAddToList(this.equipSlots, e)){
            numEquips++;
            return true;
        }
        return false;
    }

    public boolean addToItemSlots(Consumable item){
        if(this.isItemSlotsFull() && !(this.itemSlots.contains(item))) return false;
        if(!(this.itemSlots.contains(item))){
            if(this.isItemSlotsFull()) return false;
            if(this.tryAddToList(this.itemSlots, item)){
                numItems++;
                return true;
            }
            return false;
        }
        else{
            if(this.itemSlots.get(this.itemSlots.indexOf(item)).addStack(item.getAmount())) return true;
            return false;
        }
    }

    private <T extends Item> boolean tryAddToList(ArrayList<T> list, T item){
        for(int i = 0; i < list.size(); i++){
            if(list.get(i) != null) continue;
            list.set(i, item);
            return true;
        }
        return false;
    }

    public boolean isEquipSlotsFull(){ return numEquips >= EQUIP_SLOTS_NUM; }
    public boolean isItemSlotsFull(){ return numItems >= ITEM_SLOTS_NUM; }
    public void addGold(int amount){ this.gold += amount; }
    public void removeGold(int amount){ this.gold -= amount; }
    public int getGold(){ return this.gold; }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(Equippable e: this.equips.values()){
            if(e != null) sb.append(String.format("[Slot: %s, Item: %s] ", e.getSlot(), e.toString()));
        }
        return sb.toString();
    }
}

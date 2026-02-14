package it.univaq.pathofdungeons.domain.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.univaq.pathofdungeons.domain.Loadout;
import it.univaq.pathofdungeons.domain.items.EquipSlots;
import it.univaq.pathofdungeons.domain.items.ItemSlot;
import it.univaq.pathofdungeons.domain.items.Slot;
import it.univaq.pathofdungeons.domain.items.equippable.Equippable;
import it.univaq.pathofdungeons.domain.items.equippable.EquippableSlot;

/**
 * Class that represents the inventory of an entity. Contains
 * slots for equipping items and several slots to store other items into
 */
public class EntityInventory implements Serializable{
    private static final int EQUIP_SLOTS_NUM = 8;
    private static final int ITEM_SLOTS_NUM = 15;
    private HashMap<EquipSlots, EquippableSlot> equips;
    private ArrayList<Slot> itemSlots;
    private int gold;

    public EntityInventory(){
        this.equips = new HashMap<>();
        for(EquipSlots s: EquipSlots.values()){
            equips.put(s, new EquippableSlot(s));
        }
        itemSlots = new ArrayList<>(ITEM_SLOTS_NUM);
        for(int i = 0; i < ITEM_SLOTS_NUM; i++){
            itemSlots.add(new ItemSlot());
        }
        gold = 0;
    }

    /**
     * Initializes the inventory by equipping the items contained in the loadout
     * @param loadout
     */
    public EntityInventory(Loadout loadout){
        this();
        for(Equippable e: loadout.getAllSlots()){
            if(e != null){
                equips.get(e.getSlot()).setItem(e);
            }
        }
    }

    public EquippableSlot getEquipSlot(EquipSlots s){ return equips.get(s); }
    public List<Slot> getItemSlots(){ return this.itemSlots; }

    public boolean isEquipSlotsFull(){
        int numEquips = 0;
        for(EquippableSlot e: equips.values()){
            if(e.getItem() != null) numEquips++;
        }
        return numEquips >= EQUIP_SLOTS_NUM;
    }
    public boolean isItemSlotsFull(){
        int numItems = 0;
        for(Slot s: itemSlots){
            if(s.getItem() != null) numItems++;
        }
        return numItems >= ITEM_SLOTS_NUM;
    }
    public int getGold(){ return this.gold; }
    public void setGold(int amount){ this.gold = amount; }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(EquippableSlot e: this.equips.values()){
            if(e != null) sb.append(String.format("[Slot: %s, Item: %s] ", e.getSlot(), e.getItem().toString()));
        }
        return sb.toString();
    }
}

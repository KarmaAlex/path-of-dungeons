package it.univaq.pathofdungeons.game.impl;

import java.util.Iterator;
import java.util.List;

import it.univaq.pathofdungeons.domain.Loadout;
import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.domain.entity.EntityInventory;
import it.univaq.pathofdungeons.domain.items.Consumable;
import it.univaq.pathofdungeons.domain.items.Item;
import it.univaq.pathofdungeons.domain.items.ItemSlot;
import it.univaq.pathofdungeons.domain.items.Slot;
import it.univaq.pathofdungeons.domain.items.equippable.Equippable;
import it.univaq.pathofdungeons.domain.items.equippable.EquippableSlot;
import it.univaq.pathofdungeons.utils.FileLogger;

public class InventoryService{

    private InventoryService(){}

    /**
     * Give a loadout to target entity
     * @param l loadout to be given
     * @param t target entity
     * @return {@code true} when loading is successful
     */
    public static boolean loadLoadout(Loadout l, Entity t){
        for(Equippable e: l.getAllSlots()){
            if(e != null){
                InventoryService.equipItem(e, t);
                InventoryService.updateStatsEquip(e, t);
            }
        }
        return true;
    }

    /**
     * Equip an item to its correspoing slot in the target entity's inventory
     * @param e item to equip
     * @param t target entity
     */
    private static void equipItem(Equippable e, Entity t) {
        EntityInventory inv =  t.getInventory();
        inv.getEquipSlot(e.getSlot()).setItem(e);
    }

    /**
     * Checks whether or not a slot is valid for a give item, see {@link Slot} for details
     * @param i item to be checked
     * @param slot slot to check for validity
     * @return {@code true} if the slot is valid, {@code false} otherwise
     */
    public static boolean isSlotValid(Item i, Slot slot){
        return slot.isItemValid(i);
    }

    public static void addGold(Entity ent, int amount){
        FileLogger.getInstance().info(ent.getName() + " recieved " + amount + " gold");
        ent.getInventory().setGold(ent.getInventory().getGold() + amount); }
    public static void removeGold(Entity ent, int amount){ ent.getInventory().setGold(ent.getInventory().getGold() - amount); }

    /**
     * Checks whether or not two slot's items can be swapped between eachother. In particular it uses the isItemValid method to
     * check both ways. See {@link Slot} for details
     * @param s1 source slot
     * @param s2 destination slot
     * @return {@code true} if the swap can happen, {@code false} otherwise
     */
    public static boolean canSwap(Slot s1, Slot s2){
        return (s2.isItemValid(s1.getItem()) && s1.isItemValid(s2.getItem()));
    }

    /**
     * Swaps the item contens of the two given slots if possible
     * @param <T> types of slots being swapped, this is used in checking possibility of the swap happening
     * @param ent entity that owns the slots and whose stats should be modified if one or both items are equippable
     * @param slot1 source slot
     * @param slot2 destination slot
     * @return {@code true} if the swap happened successfully, {@code false} otherwise
     */
    public static <T extends Slot> boolean swapSlots(Entity ent, T slot1, T slot2){
        //If either slot is invalid for one of the items cancel the swap
        if((!InventoryService.canSwap(slot1, slot2)) || slot1.getItem() == null) return false;
        if(InventoryService.isEquippable(slot1.getItem())){
            // Source slot item is equippable
            try{
                //Source slot is an equippable slot, unequip item 1 and equip item 2 if it is an equippable
                EquippableSlot s = (EquippableSlot)slot1;
                Equippable oldItem = s.getItem();
                InventoryService.updateStatsUnequip(oldItem, ent);
                if(slot2.getItem() != null) InventoryService.updateStatsEquip((Equippable)slot2.getItem(), ent);
                slot1.setItem(slot2.getItem());
                slot2.setItem(oldItem);
                return true;
            }catch(ClassCastException e){
                //Source slot is not an equippable slot which means destination slot is equippable
                try {
                    Equippable oldItem = ((EquippableSlot)slot2).getItem();
                    if(oldItem != null) InventoryService.updateStatsUnequip(oldItem, ent);
                    InventoryService.updateStatsEquip((Equippable)slot1.getItem(), ent);
                    slot2.setItem(slot1.getItem());
                    slot1.setItem(oldItem);
                    return true;
                } catch (ClassCastException e1) {
                    //Neither slot is equippable even though item is so just swap them
                    Item oldItem = slot2.getItem();
                    slot2.setItem(slot1.getItem());
                    slot1.setItem(oldItem);
                    return true;
                }
            }
        }
        else{
            //Source slot item is not equippable, just swap them
            Item oldItem = slot2.getItem();
            slot2.setItem(slot1.getItem());
            slot1.setItem(oldItem);
            return true;
        }
    }

    /**
     * Adds an item to the entity's main inventory if possible
     * @param ent entity whose inventory should be modified
     * @param i item to add to the inventory
     * @return {@code true} if the item was added, {@code false} if the swap failed, for example if the inventory is full
     */
    public static boolean addItemToInventory(Entity ent, Item i){
        EntityInventory inv = ent.getInventory();
        if(inv.isItemSlotsFull()) return false;
        List<Slot> slots = inv.getItemSlots();
        Iterator<Slot> it = slots.iterator();
        while(it.hasNext()){
            Slot s = it.next();
            if(s.getItem() != null){
                if(!(InventoryService.isEquippable(i)) && s.getItem().equals(i) && s.getItem().getAmount() < s.getItem().getMaxStack()){
                    FileLogger.getInstance().info(ent.getName() + " recieved:\n " + i.toString());
                    int rem = s.getItem().addStack(i.getAmount());
                    i.reduceStack(i.getAmount() - rem);
                    if(rem > 0) InventoryService.addItemToInventory(ent, i);
                    return true;
                }
                continue;
            }
            s.setItem(i);
            FileLogger.getInstance().info(ent.getName() + " recieved:\n " + i.toString());
            return true;
        }
        return false;
    }

    public static void useItem(Entity target, ItemSlot slot){
        ((Consumable)slot.getItem()).useItem(target);
        slot.getItem().reduceStack(1);
        if(slot.getItem().getAmount() < 1){
            slot.setItem(null);
        }
    }

    /**
     * Checks whether an item is equippable
     * @param i item to be checked
     * @return {@code true} if the item belongs to the {@link Equippable} class (or subclasses), {@code false} otherwise
     */
    private static boolean isEquippable(Item i){
        try{
            Equippable e = (Equippable)i;
            return e != null;
        } catch(ClassCastException e){
            return false;
        }
    }

    /**
     * Internal method that updates an entity's stats upon unequipping an item
     * @param e item that was unequipped
     * @param t entity to update the stats of
     */
    private static void updateStatsEquip(Equippable e, Entity t){
        e.getStats().forEach(t::updateStat);
        if(e.getEffect() != null) t.addOnHitEffect(EffectFactory.getEffect(e.getEffect()));
    }
    
    /**
     * Internal method to update an entity's stats after an item was equipped
     * @param e item that was equipped
     * @param t target entity
     */
    private static void updateStatsUnequip(Equippable e, Entity t){
        e.getStats().forEach((stat, amount)-> t.updateStat(stat, -amount));
        if(e.getEffect() != null) t.removeOnHitEffect(e.getEffect());
    }

}

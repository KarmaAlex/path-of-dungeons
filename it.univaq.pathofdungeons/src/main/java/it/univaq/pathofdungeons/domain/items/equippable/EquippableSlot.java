package it.univaq.pathofdungeons.domain.items.equippable;

import it.univaq.pathofdungeons.domain.items.EquipSlots;
import it.univaq.pathofdungeons.domain.items.Item;
import it.univaq.pathofdungeons.domain.items.Slot;

/**
 * Class that represents an inventory slot where an item can be equipped
 */
public class EquippableSlot implements Slot{
    private Equippable item;
    private EquipSlots slot;

    public EquippableSlot(EquipSlots e){
        this.slot = e;
    }
    @Override
    public Equippable getItem(){ return this.item; }
    @Override
    public void setItem(Item item){ this.item = (Equippable)item; }
    public EquipSlots getSlot(){ return this.slot; }
    @Override
    public boolean isItemValid(Item item){
        try{
            Equippable i = (Equippable)item;
            //Make sure item is equippable, with added condition for rings being equippable in either slot
            return i == null || i.getSlot().equals(this.slot) || 
            ((this.slot.equals(EquipSlots.LRING) || this.slot.equals(EquipSlots.RRING)) && 
            (i.getSlot().equals(EquipSlots.RRING) || i.getSlot().equals(EquipSlots.LRING)));
        } catch(ClassCastException e){
            return false;
        }
    }
}

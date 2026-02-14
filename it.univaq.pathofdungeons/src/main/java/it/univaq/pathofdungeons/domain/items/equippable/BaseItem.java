package it.univaq.pathofdungeons.domain.items.equippable;

import java.io.Serializable;

import it.univaq.pathofdungeons.domain.items.EquipSlots;

/**
 * Class that represents a base item, meaning one with no stats, just a name and slot
 */
public class BaseItem implements Serializable{
    private String name;
    private EquipSlots slot;

    public BaseItem(String name, EquipSlots slot){
        this.name = name;
        this.slot = slot;
    }

    public String getName(){ return this.name; }
    public EquipSlots getSlot(){ return this.slot; }
}

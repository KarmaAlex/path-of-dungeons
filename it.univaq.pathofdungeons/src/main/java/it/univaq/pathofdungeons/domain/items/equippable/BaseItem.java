package it.univaq.pathofdungeons.domain.items.equippable;

import it.univaq.pathofdungeons.domain.items.EquipSlots;

public class BaseItem {
    private String name;
    private EquipSlots slot;

    public BaseItem(String name, EquipSlots slot){
        this.name = name;
        this.slot = slot;
    }

    public String getName(){ return this.name; }
    public EquipSlots getSlot(){ return this.slot; }

    @Override
    public String toString(){ return this.name; }
}

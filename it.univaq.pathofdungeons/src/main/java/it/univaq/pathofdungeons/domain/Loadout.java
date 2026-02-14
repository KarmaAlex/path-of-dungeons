package it.univaq.pathofdungeons.domain;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import it.univaq.pathofdungeons.domain.items.equippable.Boots;
import it.univaq.pathofdungeons.domain.items.equippable.Chestpiece;
import it.univaq.pathofdungeons.domain.items.equippable.Equippable;
import it.univaq.pathofdungeons.domain.items.equippable.Gloves;
import it.univaq.pathofdungeons.domain.items.equippable.Helmet;
import it.univaq.pathofdungeons.domain.items.equippable.Ring;

/**
 * Represents the item loadout of a player class to be loaded on startup.
 * Because of the way they are loaded loadouts should only include the items that are defined and should omit any item slots
 * that should not be filled
 */
public class Loadout {
    Helmet helmet;
    Chestpiece chestplate;
    Boots boots;
    Ring ring1;
    Ring ring2;
    Gloves gloves;

    public Loadout(Helmet helmet, Chestpiece chestpiece, Boots boots, Ring ring1, Ring ring2, Gloves gloves){
        this.helmet = helmet;
        this.chestplate = chestpiece;
        this.boots = boots;
        this.ring1 = ring1;
        this.ring2 = ring2;
        this.gloves = gloves;
    }

    public Gloves getGloves(){ return this.gloves; }
    public Helmet getHelmet(){ return this.helmet; }
    public Chestpiece getChestpiece(){ return this.chestplate; }
    public Ring getRing1(){ return this.ring1; }
    public Ring getRing2(){ return this.ring2; }
    public Boots getBoots(){ return this.boots; }
    public List<Equippable> getAllSlots(){ return new LinkedList<>(Arrays.asList(this.gloves, this.helmet, this.chestplate, this.ring1, this.ring2, this.boots)); }

    @Override
    public String toString(){
        return String.format("Helmet: %s%n Chestplate: %s%n Gloves: %s%n Left ring: %s%n Right ring: %s%n Boots: %s", helmet, chestplate, gloves, ring1, ring2, boots);
    }
}

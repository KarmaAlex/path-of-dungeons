package it.univaq.pathofdungeons.domain.items.equippable;

import java.util.HashMap;

import it.univaq.pathofdungeons.domain.effects.Effects;
import it.univaq.pathofdungeons.domain.entity.EntityStats;
import it.univaq.pathofdungeons.domain.items.EquipSlots;
import it.univaq.pathofdungeons.domain.items.Item;
import it.univaq.pathofdungeons.domain.spells.Spells;

public abstract class Equippable extends Item{
    HashMap<EntityStats, Integer> itemStats;
    Effects onHitEffect;
    Rarities rarity;
    BaseItem item;
    Spells spell;

    public Equippable(BaseItem item, HashMap<EntityStats, Integer> stats, Effects effect, Rarities rarity, Spells spell){
        this.item = item;
        this.itemStats = stats;
        this.onHitEffect = effect;
        this.rarity = rarity;
        this.spell = spell;
    }

    public EquipSlots getSlot(){ return this.item.getSlot(); }
    public int getStat(EntityStats es){ return itemStats.get(es) != null ? itemStats.get(es) : 0; }
    public Effects getEffect(){ return this.onHitEffect; }
    public HashMap<EntityStats, Integer> getStats(){ return this.itemStats; }
    public Rarities getRarity(){ return this.rarity; }
    public String getName(){ return this.item.toString(); }
    public BaseItem getBase(){ return this.item; }
    public void setBase(BaseItem b){ this.item = b; }
    public Spells getSpell(){ return this.spell; }

    @Override
    public String toString(){
        return String.format("Base: %s, Stats: %s, Effect: %s, Rarity: %s", item, itemStats, onHitEffect, rarity);
    }
}

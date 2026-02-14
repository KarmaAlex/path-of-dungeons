package it.univaq.pathofdungeons.domain.items.equippable;

import java.util.HashMap;
import java.util.Map;

import it.univaq.pathofdungeons.domain.effects.Effects;
import it.univaq.pathofdungeons.domain.entity.EntityStats;
import it.univaq.pathofdungeons.domain.items.EquipSlots;
import it.univaq.pathofdungeons.domain.items.Item;
import it.univaq.pathofdungeons.domain.spells.Spells;

/**
 * Class that represents any item that can be equipped in the user's inventory
 */
public abstract class Equippable extends Item{
    HashMap<EntityStats, Integer> itemStats;
    Effects onHitEffect;
    Rarities rarity;
    BaseItem item;
    Spells spell;

    protected Equippable(BaseItem base, Map<EntityStats, Integer> stats, Effects effect, Rarities rarity, Spells spell){
        super(1,1, "test");
        this.item = base;
        this.itemStats = new HashMap<>(stats);
        this.onHitEffect = effect;
        this.rarity = rarity;
        this.spell = spell;
        this.setEquippable(true);
    }

    public EquipSlots getSlot(){ return this.item.getSlot(); }
    public int getStat(EntityStats es){ return itemStats.get(es) != null ? itemStats.get(es) : 0; }
    public Effects getEffect(){ return this.onHitEffect; }
    public Map<EntityStats, Integer> getStats(){ return this.itemStats; }
    public Rarities getRarity(){ return this.rarity; }
    public BaseItem getBase(){ return this.item; }
    public void setBase(BaseItem b){ this.item = b; }
    public Spells getSpell(){ return this.spell; }

    @Override
    public String getName(){ return this.item.getName(); }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<EntityStats, Integer> e: this.itemStats.entrySet()){
            sb.append(String.format("  %s: %d%n", e.getKey(), e.getValue()));
        }
        return String.format(
            "Rarity: %s%nName: %s%nSlot: %s%nStats:%n%s%s%s", 
            rarity,
            item.getName(),
            item.getSlot(),
            sb.toString(),
            onHitEffect != null ? String.format("Effect: %s%n", onHitEffect) : "",
            spell != null ? String.format("Spell: %s", this.spell) : "");
    }
}

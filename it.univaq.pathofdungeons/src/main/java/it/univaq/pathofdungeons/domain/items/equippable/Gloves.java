package it.univaq.pathofdungeons.domain.items.equippable;

import java.util.HashMap;

import it.univaq.pathofdungeons.domain.effects.Effects;
import it.univaq.pathofdungeons.domain.entity.EntityStats;
import it.univaq.pathofdungeons.domain.spells.Spells;

public class Gloves extends Equippable{
    public Gloves(BaseItem item, HashMap<EntityStats, Integer> stats, Effects effect, Rarities rarity, Spells spell){
        super(item, stats, effect, rarity, spell);
    }
}

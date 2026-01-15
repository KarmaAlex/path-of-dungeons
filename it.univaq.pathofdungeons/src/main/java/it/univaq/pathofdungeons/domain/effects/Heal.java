package it.univaq.pathofdungeons.domain.effects;

import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.domain.entity.EntityStats;

public class Heal extends Effect{
    private static final int HEAL_AMOUNT = 35;

    public Heal() {
        super(Effects.HEAL);
    }

    @Override
    public void apply(Entity target){
        target.updateStat(EntityStats.HEALTH, HEAL_AMOUNT);
    }
}

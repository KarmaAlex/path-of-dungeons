package it.univaq.pathofdungeons.domain.effects;

import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.domain.entity.EntityStats;
import it.univaq.pathofdungeons.game.impl.EffectService;

public class Heal extends Effect{
    private static final int HEAL_AMOUNT = 35;

    public Heal() {
        super(Effects.HEAL, false);
    }

    @Override
    public void apply(Entity source, Entity target){
        target.updateStat(EntityStats.HEALTH, HEAL_AMOUNT);
        EffectService.removeEffect(this, target);
    }
}

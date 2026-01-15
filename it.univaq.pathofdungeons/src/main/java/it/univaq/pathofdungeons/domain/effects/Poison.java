package it.univaq.pathofdungeons.domain.effects;

import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.domain.entity.EntityStats;

public class Poison extends Effect{
    private static final int POISON_DAMAGE = 5;
    private static final int POISON_DURATION = 3;

    private int duration;

    public Poison() {
        super(Effects.POISON);
        duration = 0;
    }

    @Override
    public void onTurnStart(Entity target){
        target.updateStat(EntityStats.HEALTH, -POISON_DAMAGE);
        duration--;
        if(duration < 1){ target.removeEffect(this); }
    }

    @Override
    public void addStack(){
        this.duration += POISON_DURATION;
    }
}

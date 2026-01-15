package it.univaq.pathofdungeons.domain.effects;

import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.domain.entity.EntityStats;

public class Burn extends Effect{
    private static final int BURN_DAMAGE = 10;
    private static final int BURN_DURATION = 5;

    private int turnsLeft;

    public Burn(){
        super(Effects.BURN);
        turnsLeft = BURN_DURATION;
    }

    @Override
    public void onTurnStart(Entity target) {
        turnsLeft--;
        target.updateStat(EntityStats.HEALTH, -BURN_DAMAGE);
    }

    @Override
    public void onTurnEnd(Entity target) {
        if(turnsLeft < 1) target.removeEffect(this);
    }

    @Override
    public void addStack(){
        this.turnsLeft += BURN_DURATION;
    }

}

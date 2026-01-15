package it.univaq.pathofdungeons.domain.effects;

import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.domain.entity.EntityStats;

public class Defend extends Effect{
    private static final float DEFENCE_MULT = 0.25f;

    public Defend(){
        super(Effects.DEFEND);
    }

    @Override
    public void onTurnStart(Entity target){
        target.updateStat(EntityStats.PHYSDEFENCE, -(int) (target.getStat(EntityStats.PHYSDEFENCE) / DEFENCE_MULT));
        target.removeEffect(this);
    }

    @Override
    public void onApply(Entity target){
        target.updateStat(EntityStats.PHYSDEFENCE, (int) (target.getStat(EntityStats.PHYSDEFENCE) * DEFENCE_MULT));
    }
}

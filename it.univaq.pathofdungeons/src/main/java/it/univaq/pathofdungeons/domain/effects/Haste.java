package it.univaq.pathofdungeons.domain.effects;

import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.domain.entity.EntityStats;

public class Haste extends Effect{
    private static final float HASTE_MULT = 0.5f; 
    private static final int HASTE_DURATION = 5;

    private int duration;

    public Haste(){
        super(Effects.HASTE);
        this.duration = 0;
    }

    @Override
    public void onApply(Entity target){
        this.duration = HASTE_DURATION;
        target.updateStat(EntityStats.SPEED, (int) (target.getStat(EntityStats.SPEED) * HASTE_MULT));
    }

    @Override
    public void onTurnStart(Entity target){
        this.duration--;
        if(this.duration < 1){
            target.updateStat(EntityStats.SPEED, -(int)(target.getStat(EntityStats.SPEED) / HASTE_MULT));
            target.removeEffect(this);
        }
    }

    @Override
    public void addStack(){
        this.duration += HASTE_DURATION;
    }

}

package it.univaq.pathofdungeons.domain.effects;

import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.domain.entity.EntityStats;
import it.univaq.pathofdungeons.game.impl.EffectService;

public class Haste extends Effect{
    private static final float HASTE_MULT = 0.5f; 
    private static final int HASTE_DURATION = 5;

    public Haste(){
        super(Effects.HASTE, true, HASTE_DURATION);
    }

    @Override
    public void onApply(Entity source, Entity target){
        target.getEffects().get(this.getType()).incrDuration(this.getDuration());
        target.updateStat(EntityStats.SPEED, (int) (target.getStat(EntityStats.SPEED) * HASTE_MULT));
    }

    @Override
    public void onTurnStart(Entity target){
        target.getEffects().get(this.getType()).decrDuration();
        if(target.getEffects().get(this.getType()).getDuration() < 1){
            target.updateStat(EntityStats.SPEED, -(int)(target.getStat(EntityStats.SPEED) / HASTE_MULT));
            EffectService.removeEffect(this, target);
        }
    }

}

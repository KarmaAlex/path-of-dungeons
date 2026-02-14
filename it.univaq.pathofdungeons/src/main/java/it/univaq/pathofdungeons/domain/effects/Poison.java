package it.univaq.pathofdungeons.domain.effects;

import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.game.impl.EffectService;

public class Poison extends Effect{
    private static final int POISON_DAMAGE = 3;
    private static final int POISON_DURATION = 6;

    public Poison() {
        super(Effects.POISON, true, POISON_DURATION);
    }

    @Override
    public void onTurnStart(Entity target){
        int damage = POISON_DAMAGE * target.getEffects().get(this.getType()).getStacks();
        target.takeDamage(damage, false);
        target.getEffects().get(this.getType()).decrDuration();
        if(target.getEffects().get(this.getType()).getDuration() < 1){ EffectService.removeEffect(this, target); }
    }

}

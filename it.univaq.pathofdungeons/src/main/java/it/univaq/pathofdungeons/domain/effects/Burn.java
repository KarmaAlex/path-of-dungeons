package it.univaq.pathofdungeons.domain.effects;

import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.game.impl.EffectService;

public class Burn extends Effect{
    private static final int BURN_DAMAGE = 5;
    private static final int BURN_DURATION = 4;

    public Burn(){
        super(Effects.BURN, true, BURN_DURATION);
    }

    @Override
    public void onTurnStart(Entity target) {
        target.getEffects().get(this.getType()).decrDuration();
        int damage = BURN_DAMAGE * target.getEffects().get(this.getType()).getStacks();
        target.takeDamage(damage, false);
    }

    @Override
    public void onTurnEnd(Entity target) {
        if(target.getEffects().get(this.getType()).getDuration() < 1) EffectService.removeEffect(this, target);
    }

}

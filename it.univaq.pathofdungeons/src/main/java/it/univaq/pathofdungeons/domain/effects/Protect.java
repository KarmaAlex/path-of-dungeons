package it.univaq.pathofdungeons.domain.effects;

import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.game.impl.EffectService;

public class Protect extends Effect{
    private Entity protector;
    private static final int PROTECT_DURATION = 2;

    public Protect() {
        super(Effects.PROTECT, false, PROTECT_DURATION);
    }

    @Override
    public void onApply(Entity source, Entity target){
        this.protector = source;
    }

    @Override
    public int onDamageTaken(Entity target, int damage, boolean physical){
        if(protector.isAlive()){
            protector.takeDamage(damage, physical);
            return 0;
        }
        return damage;
    }

    @Override
    public void onTurnEnd(Entity target){
        target.getEffects().get(this.getType()).decrDuration();
        if(target.getEffects().get(this.getType()).getDuration() < 1){
            EffectService.removeEffect(this, target);
        }
    }

}

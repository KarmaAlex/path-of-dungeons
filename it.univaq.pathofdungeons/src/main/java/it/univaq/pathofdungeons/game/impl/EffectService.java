package it.univaq.pathofdungeons.game.impl;

import java.util.Iterator;

import it.univaq.pathofdungeons.domain.effects.Effect;
import it.univaq.pathofdungeons.domain.effects.EffectInfo;
import it.univaq.pathofdungeons.domain.effects.Effects;
import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.utils.BattleLogger;


public class EffectService {

    private EffectService(){}

    /**
     * Resolves all effects that should be applied at the start of the turn
     * @param turnHolder entity the effects should be resolved on
     */
    public static void onTurnStart(Entity turnHolder) {
        for(Effects e: turnHolder.getEffects().keySet()){
            turnHolder.getEffects().get(e).getEffect().onTurnStart(turnHolder);
        }
    }

    /**
     * Resolves all effects that should be applied at the end of the turn
     * @param turnHolder entity the effects should be resolved on
     */
    public static void onTurnEnd(Entity turnHolder) {
        for(Effects e: turnHolder.getEffects().keySet()){
            turnHolder.getEffects().get(e).getEffect().onTurnEnd(turnHolder);
        }
    }

    /**
     * Applies an effect to a given entity, if the effect is already present and is stackable it adds
     * a stack instead
     * @param e effect to be applied
     * @param source entity the effect originates from
     * @param target target of the effect
     */
    public static void applyEffect(Effect e, Entity source, Entity target) {
        BattleLogger.getInstance().info(String.format("%s is effected by %s", target.getName(), e.getType().toString()));
        if(target.getEffects().containsKey(e.getType()) && e.isStackable()){
            target.getEffects().get(e.getType()).incrStacks();
            target.getEffects().get(e.getType()).incrDuration(e.getDuration());
        }
        else{
            target.addEffect(e);
            e.onApply(source, target);
        }
    }

    public static int applyOnHitEffects(Entity target, int amount, boolean physical){
        int damage = amount;
        Iterator<EffectInfo> itr = target.getEffects().values().iterator();
        while(damage > 0 && itr.hasNext()){
            damage = itr.next().getEffect().onDamageTaken(target, damage, physical);
        }
        return damage;
    }
    
    /**
     * Removes an effect from the target's list of active effects
     * @param e effect to be removed
     * @param target entity to remove the effect from
     */
    public static void removeEffect(Effect e, Entity target){
        target.getEffects().remove(e.getType());
    }
}

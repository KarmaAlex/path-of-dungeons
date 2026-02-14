package it.univaq.pathofdungeons.domain.effects;

import java.io.Serializable;

import it.univaq.pathofdungeons.domain.entity.Entity;
/**
 * Abstract class that represents an effect that can be inflicted on an entity
 * during combat, has methods to apply effects at the start and end of turns and on taking damage
 */
public abstract class Effect implements Serializable{
    private Effects effectType;
    private boolean stackable;
    private int duration;

    protected Effect(Effects effect, boolean stackable){
        this.effectType = effect;
        this.stackable = stackable;
        this.duration = 0;
    }

    protected Effect(Effects effect, boolean stackable, int dur){
        this(effect, stackable);
        this.duration = dur;
    }

    /**
     * Defines what should happen when the effect is applied and the target's turn has started
     * @param target
     */
    public void onTurnStart(Entity target){}

    /**
     * Defines what should happen when the effect is applied and the target's turn has ended
     * @param target
     */
    public void onTurnEnd(Entity target){}

    
    /**
     * Defines what should happen when the effect is applied and the target has taken damage
     * @param target target entity
     * @param amount amount of damage taken
     * @param physical whether or not the damage is physical or magical
     * @return if any damage is consumed, returns the remaining damage, otherwise {@code amount}
     */
    public int onDamageTaken(Entity target, int amount, boolean physical){return amount;}

    /**
     * Apply the effect to the target entity originating from the source entity
     * @param source
     * @param target
     */
    public void apply(Entity source, Entity target){
        target.addEffect(this);
        this.onApply(source, target);
    }

    /**
     * Defines what should happen immediately upon effect application
     * @param source
     * @param target
     */
    public void onApply(Entity source, Entity target){}

    public Effects getType(){ return this.effectType; }

    public int getDuration(){return this.duration;}

    public boolean isStackable(){ return stackable; }
}

package it.univaq.pathofdungeons.domain.spells;

import java.util.Random;

import it.univaq.pathofdungeons.domain.BattleAction;
import it.univaq.pathofdungeons.domain.TargetTypes;
import it.univaq.pathofdungeons.domain.effects.Effects;
import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.domain.entity.EntityStats;
import it.univaq.pathofdungeons.game.MissingTargetException;
import it.univaq.pathofdungeons.game.impl.EffectFactory;

/**
 * Abstract class that represents a spell to be used by an entity
 */
public abstract class Spell extends BattleAction{
    private int spellDamage;
    private Effects spellEffect;
    private int spellCost;
    private String name;
    private int currDamage = 0;

    protected Spell(String name, Effects spellEffect, int spellDamage, int spellCost, TargetTypes tt){
        super(tt);
        this.spellEffect = spellEffect;
        this.spellDamage = spellDamage;
        this.spellCost = spellCost;
        this.name = name;
    }

    /**
     * Returns the damage that was calculated the last time {@code applySpell()} was called, should be used immediately after
     * to apply the damage that was calculated externally
     * @return damage amount
     */
    public int getDamage(){ return this.currDamage; }
    public int getCost(){ return this.spellCost; }
    public Effects getEffect(){ return this.spellEffect; }

    /**
     * Applies the effect of the spell to the target entity. Default implementation just calculates the damage
     * to apply to the target entity, should be overridden if a different behavior is desired
     * @param source source of the spell
     * @param target target of the spell
     * @throws MissingTargetException if the spell requires a target but none is passed
     */
    public void applySpell(Entity source, Entity target) throws MissingTargetException{
        if(target == null) throw new MissingTargetException();
        float mult = 1;
        if(new Random().nextInt(100) < source.getStat(EntityStats.SPELLCRITCHANCE)) mult = 1.5f;
        this.currDamage = (int)(source.getStat(EntityStats.MAGICATTACK) + this.spellDamage * mult);
    }

    /**
     * Applies the spell's special effect to the target entity
     * @param target target of the spell's effect
     */
    protected void applySpellEffect(Entity source, Entity target){
        EffectFactory.getEffect(this.getEffect()).apply(source, target);
    }

    @Override
    public String toString(){
        return this.name;
    }
}

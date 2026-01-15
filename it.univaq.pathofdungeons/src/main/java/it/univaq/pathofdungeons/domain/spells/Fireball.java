package it.univaq.pathofdungeons.domain.spells;

import java.util.Random;

import it.univaq.pathofdungeons.domain.TargetTypes;
import it.univaq.pathofdungeons.domain.effects.Effects;
import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.game.MissingTargetException;

public class Fireball extends Spell{
    private static final int FIREBALL_DAMAGE = 20;
    private static final int FIREBALL_COST = 10;
    private static final String SPELL_NAME = "Fireball";
    private static final int BURN_CHANCE = 50;

    public Fireball(){
        super(SPELL_NAME, Effects.BURN, FIREBALL_DAMAGE, FIREBALL_COST, TargetTypes.ENEMY);
    }

    @Override
    public void applySpell(Entity source, Entity target) throws MissingTargetException{
        super.applySpell(source, target);
        if(new Random().nextInt(100) < BURN_CHANCE) super.applySpellEffect(target);
    }
}

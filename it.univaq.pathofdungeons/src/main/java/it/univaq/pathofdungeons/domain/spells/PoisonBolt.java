package it.univaq.pathofdungeons.domain.spells;

import it.univaq.pathofdungeons.domain.TargetTypes;
import it.univaq.pathofdungeons.domain.effects.Effects;
import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.game.MissingTargetException;

public class PoisonBolt extends Spell{
    private static final String SPELL_NAME = "Poison bolt";
    private static final int SPELL_DAMAGE = 10;
    private static final int SPELL_COST = 25;

    public PoisonBolt(){
        super(SPELL_NAME, Effects.POISON, SPELL_DAMAGE, SPELL_COST, TargetTypes.ENEMY);
    }

    @Override
    public void applySpell(Entity source, Entity target) throws MissingTargetException{
        super.applySpell(source, target);
        this.applySpellEffect(target);
    }
}

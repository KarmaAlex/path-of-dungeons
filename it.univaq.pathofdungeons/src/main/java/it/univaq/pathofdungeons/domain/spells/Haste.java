package it.univaq.pathofdungeons.domain.spells;

import it.univaq.pathofdungeons.domain.TargetTypes;
import it.univaq.pathofdungeons.domain.effects.Effects;
import it.univaq.pathofdungeons.domain.entity.Entity;

public class Haste extends Spell{
    private static final String SPELL_NAME = "Haste";
    private static final int SPELL_COST = 25;

    public Haste(){
        super(SPELL_NAME, Effects.HASTE, 0, SPELL_COST, TargetTypes.ALLY_SELF);
    }

    @Override
    public void applySpell(Entity source, Entity target){
        this.applySpellEffect(source, source);
    }
}

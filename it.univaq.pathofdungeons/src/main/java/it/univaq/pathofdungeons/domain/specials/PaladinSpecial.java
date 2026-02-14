package it.univaq.pathofdungeons.domain.specials;

import it.univaq.pathofdungeons.domain.TargetTypes;
import it.univaq.pathofdungeons.domain.effects.Effects;
import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.game.MissingTargetException;
import it.univaq.pathofdungeons.game.impl.EffectFactory;
import it.univaq.pathofdungeons.game.impl.EffectService;

/**
 * Protect ally taking damage instead of them
 */
public class PaladinSpecial extends SpecialAttack{
    private static final String PALADIN_SPECIAL = "Protect";

    public PaladinSpecial(){
        super(TargetTypes.ALLY, PALADIN_SPECIAL, 0);
    }

    @Override
    public void useSpecial(Entity source, Entity target) throws MissingTargetException {
        EffectService.applyEffect(EffectFactory.getEffect(Effects.PROTECT), source, target);
    }

}

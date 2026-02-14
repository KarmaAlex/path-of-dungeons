package it.univaq.pathofdungeons.domain.specials;

import it.univaq.pathofdungeons.domain.TargetTypes;
import it.univaq.pathofdungeons.domain.effects.Effects;
import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.game.MissingTargetException;
import it.univaq.pathofdungeons.game.impl.EffectFactory;
import it.univaq.pathofdungeons.game.impl.EffectService;

public class WarriorSpecial extends SpecialAttack{
    private static final String WARRIOR_SPECIAL = "Dragon dance";

    public WarriorSpecial() {
        super(TargetTypes.SELF, WARRIOR_SPECIAL, 0);
    }

    @Override
    public void useSpecial(Entity source, Entity target) throws MissingTargetException {
        EffectService.applyEffect(EffectFactory.getEffect(Effects.DRAGON_DANCE), source, target);
    }
}

package it.univaq.pathofdungeons.domain.specials;

import it.univaq.pathofdungeons.domain.TargetTypes;
import it.univaq.pathofdungeons.domain.effects.Effects;
import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.game.MissingTargetException;
import it.univaq.pathofdungeons.game.impl.EffectFactory;
import it.univaq.pathofdungeons.game.impl.EffectService;

public class ArcherSpecial extends SpecialAttack{
    private static final String ARCHER_SPECIAL = "Venoshock";
    private static final int ARCHER_SPECIAL_DAMAGE = 50;

    public ArcherSpecial() {
        super(TargetTypes.ENEMY, ARCHER_SPECIAL, ARCHER_SPECIAL_DAMAGE);
    }

    @Override
    public void useSpecial(Entity source, Entity target) throws MissingTargetException {
        EffectService.applyEffect(EffectFactory.getEffect(Effects.POISON), source, target);
    }

}

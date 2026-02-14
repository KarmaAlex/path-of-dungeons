package it.univaq.pathofdungeons.domain.specials;

import java.util.List;
import java.util.Random;

import it.univaq.pathofdungeons.domain.TargetTypes;
import it.univaq.pathofdungeons.domain.effects.Effects;
import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.game.impl.EffectFactory;
import it.univaq.pathofdungeons.game.impl.EffectService;

public class MageSpecial extends SpecialAttack{
    private static final String MAGE_SPECIAL = "Light that burns the sky";
    private static final int MAGE_SPECIAL_DAMAGE = 20;
    private static final int BURN_CHANCE = 50;

    public MageSpecial() {
        super(TargetTypes.ALL_ENEMIES, MAGE_SPECIAL, MAGE_SPECIAL_DAMAGE);
    }

    @Override
    public void useSpecial(Entity source, List<Entity> targets){
        Random rand = new Random();
        for(Entity e: targets){
            if(rand.nextInt(100) < BURN_CHANCE){
                EffectService.applyEffect(EffectFactory.getEffect(Effects.BURN), source, e);
            }
        }
    }

}

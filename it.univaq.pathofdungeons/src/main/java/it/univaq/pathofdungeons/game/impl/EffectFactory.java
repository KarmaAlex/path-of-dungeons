package it.univaq.pathofdungeons.game.impl;

import it.univaq.pathofdungeons.domain.effects.Burn;
import it.univaq.pathofdungeons.domain.effects.Defend;
import it.univaq.pathofdungeons.domain.effects.DragonDance;
import it.univaq.pathofdungeons.domain.effects.Effect;
import it.univaq.pathofdungeons.domain.effects.Effects;
import it.univaq.pathofdungeons.domain.effects.Haste;
import it.univaq.pathofdungeons.domain.effects.Heal;
import it.univaq.pathofdungeons.domain.effects.Poison;
import it.univaq.pathofdungeons.domain.effects.Protect;

public class EffectFactory{

    private EffectFactory(){}

    /**
     * Returns an instance of an effect based on the give Effects object
     * @param effect object representing the type of effect wanted
     * @return a new instance of that effect
     */
    public static Effect getEffect(Effects effect){
        switch(effect){
            case BURN:
                return new Burn();
            case DEFEND:
                return new Defend();
            case HEAL:
                return new Heal();
            case POISON:
                return new Poison();
            case HASTE:
                return new Haste();
            case PROTECT:
                return new Protect();
            case DRAGON_DANCE:
                return new DragonDance();
        }
        return null;
    }
}

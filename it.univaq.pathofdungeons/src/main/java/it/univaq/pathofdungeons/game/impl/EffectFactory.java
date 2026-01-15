package it.univaq.pathofdungeons.game.impl;

import it.univaq.pathofdungeons.domain.effects.Burn;
import it.univaq.pathofdungeons.domain.effects.Defend;
import it.univaq.pathofdungeons.domain.effects.Effect;
import it.univaq.pathofdungeons.domain.effects.Effects;
import it.univaq.pathofdungeons.domain.effects.Haste;
import it.univaq.pathofdungeons.domain.effects.Heal;
import it.univaq.pathofdungeons.domain.effects.Poison;

public class EffectFactory{
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
            default:
                return null;
        }
    }
}

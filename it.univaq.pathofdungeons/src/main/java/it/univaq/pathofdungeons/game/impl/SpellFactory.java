package it.univaq.pathofdungeons.game.impl;

import it.univaq.pathofdungeons.domain.spells.Fireball;
import it.univaq.pathofdungeons.domain.spells.Haste;
import it.univaq.pathofdungeons.domain.spells.Heal;
import it.univaq.pathofdungeons.domain.spells.PoisonBolt;
import it.univaq.pathofdungeons.domain.spells.Spell;
import it.univaq.pathofdungeons.domain.spells.Spells;

/**
 * Factory that produces Spell objects as needed
 */
public class SpellFactory{
    /**
     * Get a spell based on the requested Spells object
     * @param spell Spells object corresponding to the desired spell
     * @return a new instance of the desired spell
     */
    public static Spell getSpell(Spells spell){
        switch(spell){
            case FIREBALL:
                return new Fireball();
            case POISONBOLT:
                return new PoisonBolt();
            case HASTE:
                return new Haste();
            case HEAL:
                return new Heal();
            default:
                return null;
        }
    }
}

package it.univaq.pathofdungeons.domain.items.consumables;

import it.univaq.pathofdungeons.domain.items.Consumable;
import it.univaq.pathofdungeons.domain.items.ConsumableType;

/**
 * Class that represents a potion
 */
public abstract class Potion extends Consumable{

    protected Potion(int amount, int maxStack, String name, ConsumableType type) {
        super(amount, maxStack, name, type);
    }

}

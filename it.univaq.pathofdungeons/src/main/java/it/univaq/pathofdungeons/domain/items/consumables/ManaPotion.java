package it.univaq.pathofdungeons.domain.items.consumables;

import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.domain.entity.EntityStats;
import it.univaq.pathofdungeons.domain.items.ConsumableType;

/**
 * Class that represents a mana potion, restores mana on use
 */
public class ManaPotion extends Potion{
    private static final String MANA_POTION_NAME = "Mana potion";
    private static final int MANA_POTION_STACK = 20;
    private static final int MANA_AMOUNT = 50;

    public ManaPotion(int amount) {
        super(amount, MANA_POTION_STACK, MANA_POTION_NAME, ConsumableType.MANA_POTION);
    }

    @Override
    public void useItem(Entity target){
        target.updateStat(EntityStats.MANA, MANA_AMOUNT);
    }

}

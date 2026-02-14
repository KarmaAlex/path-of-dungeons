package it.univaq.pathofdungeons.domain.items.consumables;

import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.domain.entity.EntityStats;
import it.univaq.pathofdungeons.domain.items.ConsumableType;
import it.univaq.pathofdungeons.utils.BattleLogger;

/**
 * Class that represents a health potion, heals the target on use
 */
public class HealthPotion extends Potion{
    private static final String HEALTH_POTION_NAME = "Health potion";
    private static final int HEALTH_POTION_STACK = 20;
    private static final int HEAL_AMOUNT = 100;

    public HealthPotion(int amount) {
        super(amount, HEALTH_POTION_STACK, HEALTH_POTION_NAME, ConsumableType.HEALTH_POTION);
    }

    @Override
    public void useItem(Entity target){
        BattleLogger.getInstance().info(String.format("%s healed by %d", target.getName(), HEAL_AMOUNT));
        target.updateStat(EntityStats.HEALTH, HEAL_AMOUNT);
    }

}

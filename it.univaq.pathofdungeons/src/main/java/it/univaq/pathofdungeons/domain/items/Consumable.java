package it.univaq.pathofdungeons.domain.items;

import it.univaq.pathofdungeons.domain.entity.Entity;

/**
 * Class for items that can be consumed, like potions
 */
public abstract class Consumable extends Item{
    private ConsumableType type;

    protected Consumable(int amount, int maxStack, String name, ConsumableType type){
        super(amount, maxStack, name);
        this.type = type;
    }

    public abstract void useItem(Entity target);
    
    @Override
    public boolean equals(Object o){
        if(!(o instanceof Consumable)) return false;
        Consumable o1 = (Consumable) o;
        return o1.type.equals(this.type);
    }

    @Override
    public String toString(){
        return String.format("%s %d/%d", this.getName(), this.getAmount(), this.getMaxStack());
    }
}

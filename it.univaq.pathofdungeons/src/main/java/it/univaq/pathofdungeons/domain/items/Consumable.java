package it.univaq.pathofdungeons.domain.items;

public class Consumable extends Item{
    private ConsumableType type;
    private int amount;
    private int maxStack;

    public int getAmount(){ return this.amount; }

    public boolean addStack(int amount){
        if(this.amount + amount > maxStack) return false;
        this.amount += amount;
        return true;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Consumable)) return false;
        Consumable o1 = (Consumable) o;
        if(o1.type.equals(this.type)) return true;
        return false;
    }
}

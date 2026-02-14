package it.univaq.pathofdungeons.domain.items;

import java.io.Serializable;

/**
 * Class representing any generic item that can be put in an inventory
 */
public abstract class Item implements Serializable{
    private boolean equippable = false;
    private int amount;
    private int maxStack;
    private String name;

    protected Item(int amount, int itemStack, String name){
        this.amount = amount;
        this.maxStack = itemStack;
        this.name = name;
    }

    /**
     * Adds amount to the stack size of the item, up to {@code maxStack}
     * @param amount
     * @return remainder after adding items to the stack, 0 if all were added
     */
    public int addStack(int amount){
        if(this.amount + amount > maxStack){
            int rem = amount - (maxStack - this.amount);
            this.amount = maxStack;
            return rem;
        }
        this.amount += amount;
        return 0;
    }

    public void reduceStack(int amount){
        this.amount = Math.max(this.amount - amount, 0);
    }

    public int getAmount(){ return this.amount; }
    public int getMaxStack(){ return this.maxStack; }

    protected void setEquippable(boolean val){ this.equippable = val; }
    public boolean isEquippable(){ return this.equippable; }

    public String getName(){ return this.name; }
}

package it.univaq.pathofdungeons.domain.items;

import java.io.Serializable;

public interface Slot extends Serializable{
    public Item getItem();
    public void setItem(Item item);
    /**
     * Returns whether the give item is valid for this slot, implementations depend on slot types
     * @param item
     * @return {@code true} if valid for the slot, {@code false} otherwise
     */
    public boolean isItemValid(Item item);
}

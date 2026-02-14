package it.univaq.pathofdungeons.domain.items;

/**
 * Class for slots that can contain any item
 */
public class ItemSlot implements Slot{
    private Item item;

    public ItemSlot(){this.item = null;}
    @Override
    public Item getItem(){ return this.item; }
    @Override
    public void setItem(Item item){ this.item = item; }
    @Override
    public boolean isItemValid(Item item){ return true; }
}

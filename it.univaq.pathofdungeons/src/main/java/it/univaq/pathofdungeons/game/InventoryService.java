package it.univaq.pathofdungeons.game;

import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.domain.items.Item;
import it.univaq.pathofdungeons.domain.items.equippable.Equippable;

public interface InventoryService {
    public boolean equipItem(Equippable e, Entity t);
    public boolean addItem(Item i, Entity t);
}

package it.univaq.pathofdungeons.domain.dungeon.rooms;

import java.util.List;
import java.util.Map;

import it.univaq.pathofdungeons.domain.entity.player.Player;
import it.univaq.pathofdungeons.domain.items.Item;
import it.univaq.pathofdungeons.game.impl.ItemFactory;

/**
 * Shop that allows players to purchase from a selection of randomly
 * generated items, always including one potion type
 */
public class Shop extends Room{

    Map<Item, Integer> items;

    public Shop() {
        super(RoomTypes.SHOP, "This is a shop, interact to buy items");
    }

    @Override
    public void onEnter(){
        if(!this.getExplored()){
            this.items = ItemFactory.getShopItems();
        }
        this.setExplored(true);
    }

    public Map<Item, Integer> getShopItems(){ return this.items; }

    @Override
    public void onInteract(List<Player> players){

    }

}

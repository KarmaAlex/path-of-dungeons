package it.univaq.pathofdungeons.domain.dungeon.rooms;

import java.util.List;
import java.util.Random;

import it.univaq.pathofdungeons.domain.entity.player.Player;
import it.univaq.pathofdungeons.game.impl.InventoryService;
import it.univaq.pathofdungeons.game.impl.ItemFactory;

/**
 * Room that contains a secret, runs a random event when interacted with
 */
public class SecretRoom extends Room{
    private boolean eventCompleted;

    public SecretRoom(){
        super(RoomTypes.SECRET, "This room seems suspicious...");
        this.eventCompleted = false;
    }

    @Override
    public void onEnter(){
        this.setExplored(true);
    }

    @Override
    public void onInteract(List<Player> players){
        if(!eventCompleted){
            switch(NPCEvents.values()[new Random().nextInt(NPCEvents.values().length)]){
                case GIVE_ITEM_TO_ALL:
                    this.setInfo("You find an item for each of your party members");
                    for(Player p: players){
                        InventoryService.addItemToInventory(p, ItemFactory.getRandomEquippable());
                    }
                    break;
                case GIVE_ITEM_TO_RANDOM:
                    Player p1 = players.get(new Random().nextInt(players.size()));
                    this.setInfo("You find an item on the ground and give it to " + p1.getName());
                    InventoryService.addItemToInventory(p1, ItemFactory.getRandomEquippable());
                    break;
                case GIVE_POTION_TO_ALL:
                    this.setInfo("You find some potions on the ground for everyone");
                    for(Player p: players){
                        InventoryService.addItemToInventory(p, ItemFactory.getRandomPotionStack());
                    }
                    break;
                case GIVE_POTION_TO_RANDOM:
                    Player p2 = players.get(new Random().nextInt(players.size()));
                    this.setInfo("You find some potions on the ground and give them to " + p2.getName());
                    InventoryService.addItemToInventory(p2, ItemFactory.getRandomPotionStack());
                    break;
                case NOTHING:
                    this.setInfo("It seems you were wrong, the room is empty");
                    break;
                default:
                    break;
            }
        }
        else this.setInfo("You have already found this room's secret");
    }

}

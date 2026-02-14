package it.univaq.pathofdungeons.domain.dungeon.rooms;

import java.util.List;
import java.util.Random;

import it.univaq.pathofdungeons.domain.entity.player.Player;
import it.univaq.pathofdungeons.game.impl.InventoryService;
import it.univaq.pathofdungeons.game.impl.ItemFactory;

/**
 * Room that contains an NPC, runs a random event on interact
 */
public class NPCRoom extends Room{
    private static String[] npcNames = {"Cynthia", "Bill", "Oak", "Birch", "Lusamine", "Wattson", "Brawly", "Flannery"};

    private String npcName;
    private boolean eventCompleted;

    public NPCRoom(){
        super(RoomTypes.NPC, "");
        this.eventCompleted = false;
    }

    @Override
    public void onEnter(){
        this.npcName = npcNames[new Random().nextInt(npcNames.length)];
        this.setInfo("This room contains " + npcName + ", interact to talk to them");
        this.setExplored(true);
    }

    @Override
    public void onInteract(List<Player> players){
        if(!this.eventCompleted){
            switch(NPCEvents.values()[new Random().nextInt(NPCEvents.values().length)]){
                case GIVE_ITEM_TO_ALL:
                    this.setInfo(npcName+ " says: I have an item for each of you, here you go.");
                    for(Player p: players){
                        InventoryService.addItemToInventory(p, ItemFactory.getRandomEquippable());
                    }
                    break;
                case GIVE_ITEM_TO_RANDOM:
                    Player p1 = players.get(new Random().nextInt(players.size()));
                    this.setInfo(npcName + " says: I have an item for you, " + p1.getName() + " can have it.");
                    InventoryService.addItemToInventory(p1, ItemFactory.getRandomEquippable());
                    break;
                case GIVE_POTION_TO_ALL:
                    this.setInfo(npcName + " says: I have some potions for all of you");
                    for(Player p: players){
                        InventoryService.addItemToInventory(p, ItemFactory.getRandomPotionStack());
                    }
                    break;
                case GIVE_POTION_TO_RANDOM:
                    Player p2 = players.get(new Random().nextInt(players.size()));
                    this.setInfo(npcName + " says: Here, " + p2.getName() +", you can have some potions");
                    InventoryService.addItemToInventory(p2, ItemFactory.getRandomPotionStack());
                    break;
                case NOTHING:
                    this.setInfo(npcName + " says: Sorry, I have nothing to give you");
                    break;
                default:
                    break;
            }
            this.eventCompleted = true;
        }
        else this.setInfo(npcName + " says: I've done all i can to help, good luck in the dungeon");
    }

}

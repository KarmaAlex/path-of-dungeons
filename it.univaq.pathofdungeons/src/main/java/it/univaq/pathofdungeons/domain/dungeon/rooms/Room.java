package it.univaq.pathofdungeons.domain.dungeon.rooms;

import java.util.ArrayList;
import java.util.LinkedList;

import it.univaq.pathofdungeons.domain.dungeon.DungeonDirections;
import it.univaq.pathofdungeons.domain.entity.enemies.Enemy;
import it.univaq.pathofdungeons.domain.entity.player.Player;
import it.univaq.pathofdungeons.domain.items.Item;

public abstract class Room {
    private ArrayList<Room> adjacent;
    private LinkedList<Item> items;
    private RoomTypes roomType;
    private boolean explored = false;
    private LinkedList<Enemy> enemies;

    public Room(RoomTypes roomType){
        items = new LinkedList<>();
        adjacent = new ArrayList<>(DungeonDirections.values().length);
        for(int i = 0; i < DungeonDirections.values().length; i++){
            adjacent.add(null);
        }
        this.roomType = roomType;
        this.enemies = new LinkedList<>();
    }

    /**
     * Returns the room adjacent to this in the given direction
     * @param dir Direction to get the room in
     * @return The room adjacent in the given direction, null if there is none
     */
    public Room getRoom(DungeonDirections dir){ return this.adjacent.get(dir.index()); }

    /**
     * Returns a copy of the arraylist that represents adjacent rooms,
     * it should be noted that because it is a copy any changes will require another call
     * to this method to be visible
     * @return A copy of the ArrayList that contains adjacent rooms
     */
    public ArrayList<Room> getAdjacent(){ return new ArrayList<>(adjacent); }

    /**
     * Sets the room next to this one in the dir direction to the given one
     * @param dir Direction to set the room in
     * @param room Room to be set
     */
    public void setRoom(DungeonDirections dir, Room room){ this.adjacent.set(dir.index(), room); }

    public RoomTypes getRoomType(){ return this.roomType; }

    public boolean getExplored(){ return this.explored; }

    public LinkedList<Item> getItems(){ return this.items; }

    public Item swapItems(Item pickup, Item equipped){
        if(items.remove(pickup)){
            this.items.add(equipped);
            return pickup;
        }
        return null; 
    }

    public Item getItem(int i){ return this.items.get(i); }

    public LinkedList<Enemy> getEnemies(){ return this.enemies; }

    protected void setExplored(boolean explored){ this.explored = explored; }

    public abstract void onEnter();

    public abstract void onInteract(LinkedList<Player> players);
}

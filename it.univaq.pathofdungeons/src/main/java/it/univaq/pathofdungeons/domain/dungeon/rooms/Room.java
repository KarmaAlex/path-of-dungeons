package it.univaq.pathofdungeons.domain.dungeon.rooms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.univaq.pathofdungeons.domain.dungeon.DungeonDirections;
import it.univaq.pathofdungeons.domain.entity.enemies.Enemy;
import it.univaq.pathofdungeons.domain.entity.player.Player;

/**
 * Abstract class that represents a generic room
 */
public abstract class Room implements Serializable{
    private ArrayList<Room> adjacent;
    private RoomTypes roomType;
    private boolean explored = false;
    private LinkedList<Enemy> enemies;
    private String info;

    protected Room(RoomTypes roomType, String info){
        this.info = info;
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
    public List<Room> getAdjacent(){ return new ArrayList<>(adjacent); }

    /**
     * Sets the room next to this one in the dir direction to the given one
     * @param dir Direction to set the room in
     * @param room Room to be set
     */
    public void setRoom(DungeonDirections dir, Room room){ this.adjacent.set(dir.index(), room); }

    public RoomTypes getRoomType(){ return this.roomType; }

    public boolean getExplored(){ return this.explored; }

    public List<Enemy> getEnemies(){ return this.enemies; }

    protected void setExplored(boolean explored){ this.explored = explored; }

    public abstract void onEnter();

    public void onInteract(List<Player> players){}

    public String getInfo(){ return this.info; }

    protected void setInfo(String info){ this.info = info; }
}

package it.univaq.pathofdungeons.game.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import it.univaq.pathofdungeons.domain.dungeon.Dungeon;
import it.univaq.pathofdungeons.domain.dungeon.DungeonCoords;
import it.univaq.pathofdungeons.domain.dungeon.DungeonDirections;
import it.univaq.pathofdungeons.domain.dungeon.DungeonGrid;
import it.univaq.pathofdungeons.domain.dungeon.rooms.BasicRoom;
import it.univaq.pathofdungeons.domain.dungeon.rooms.BossRoom;
import it.univaq.pathofdungeons.domain.dungeon.rooms.CombatRoom;
import it.univaq.pathofdungeons.domain.dungeon.rooms.GenRooms;
import it.univaq.pathofdungeons.domain.dungeon.rooms.NPCRoom;
import it.univaq.pathofdungeons.domain.dungeon.rooms.Room;
import it.univaq.pathofdungeons.domain.dungeon.rooms.SecretRoom;
import it.univaq.pathofdungeons.domain.dungeon.rooms.Shop;
import it.univaq.pathofdungeons.utils.FileLogger;

/**
 * Utility class that contains the logic for generating the dungeon map.
 */
public class DungeonGenerator{
    private static final int DOOR_GEN_CHANCE_PERC = 30;
    private static final int ROOM_GEN_CHANCE_PERC = 60;
    
    private DungeonGenerator(){}

    /**
     * Generates a dungeon of a given size by adding rooms to its map randomly. This happens by exploring
     * existing room using a Breadth-First search and randomly adding adjacent rooms. Connections between
     * rooms are also randomly added if two disconnected rooms are adjacent.
     * @param size Desired number of rooms in the dungeon
     * @return New Dungeon containing the starting room and a grid representation of the map
     */
    public static Dungeon createDungeon(int size){
        Room startingRoom = new BasicRoom();
        //Explore the room we create
        startingRoom.onEnter();
        DungeonGrid grid = new DungeonGrid();
        Room currentRoom = startingRoom;
        int numRooms = 1;
        Random rand = new Random();
        DungeonCoords currentCoords = new DungeonCoords(0,0);
        DungeonCoords lastAddedCoords = null;

        grid.add(currentCoords, currentRoom);
        //Used for traversing dungeon rooms breadth-first
        LinkedList<DungeonCoords> toCheck = new LinkedList<>();
        LinkedList<DungeonCoords> backup = new LinkedList<>();

        while(numRooms < size){
            int numAdj = 0;
            currentRoom = grid.get(currentCoords);
            List<Room> adjacent = currentRoom.getAdjacent();
            for(DungeonDirections dir: DungeonDirections.values()){
                int oldWidth = grid.getWidth();
                int oldHeight = grid.getHeight();
                if(adjacent.get(dir.index()) == null){
                    Room adj = grid.getAdj(dir, currentCoords);
                    if(adj == null){
                        Room tempRoom = generateRoom();
                        if(tempRoom != null){
                            currentRoom.setRoom(dir, tempRoom);
                            tempRoom.setRoom(DungeonDirections.fromIndex(dir.getOpposite()), currentRoom);
                            grid.add(dir, tempRoom, currentCoords);
                            // record coords of the room we just added
                            lastAddedCoords = DungeonCoords.move(currentCoords, dir);
                            // If adding rooms left or above and array is shifted the room coords need to be updated
                            if((dir == DungeonDirections.LEFT && oldWidth != grid.getWidth()) || (dir == DungeonDirections.UP && oldHeight != grid.getHeight())){
                                currentCoords = DungeonCoords.move(currentCoords, DungeonDirections.fromIndex(dir.getOpposite()));
                                for(int i = 0; i < toCheck.size(); i++){
                                    toCheck.set(i, DungeonCoords.move(toCheck.get(i), DungeonDirections.fromIndex(dir.getOpposite())));
                                }
                                for(int i = 0; i < backup.size(); i++){
                                    backup.set(i, DungeonCoords.move(backup.get(i), DungeonDirections.fromIndex(dir.getOpposite())));
                                }
                            }
                            toCheck.add(DungeonCoords.move(currentCoords, dir));
                            numAdj++;
                            numRooms++;
                        }
                    }
                    else if(rand.nextInt(100) < DOOR_GEN_CHANCE_PERC) {
                        currentRoom.setRoom(dir, adj);
                        adj.setRoom(DungeonDirections.fromIndex(dir.getOpposite()), currentRoom);
                        numAdj++;
                    } else numAdj ++;
                }
                // Add a connection to the adjacent room if it doesn't already exist with a 30% chance
                else if(currentRoom.getRoom(dir).getRoom(DungeonDirections.fromIndex(dir.getOpposite())) != currentRoom){
                    if(rand.nextInt(100) < DOOR_GEN_CHANCE_PERC) currentRoom.getRoom(dir).setRoom(DungeonDirections.fromIndex(dir.getOpposite()), currentRoom);
                    numAdj++;
                }
                else numAdj++;
                if (numRooms >= size) break;
            }
            if(numAdj < 4){
                if(!toCheck.isEmpty()){
                    backup.add(currentCoords);
                    currentCoords = toCheck.removeFirst();
                } else if(!backup.isEmpty()){
                    backup.add(currentCoords);
                    currentCoords = backup.removeFirst();
                }
            }
            else if(!toCheck.isEmpty()) currentCoords = toCheck.removeFirst();
            else if(!backup.isEmpty()) currentCoords = backup.removeFirst();
        }
        //Make the last room added a boss room
        if(lastAddedCoords != null){
            addBoss(grid, lastAddedCoords);
        }
        Dungeon dungeon = new Dungeon(startingRoom, grid);
        dungeon.setService(new DungeonServiceImpl());
        FileLogger.getInstance().info("Created dungeon");
        return dungeon;
    }

    /**
     * Has a chance to return a random room chosen using a weighted random function
     * @return A new instance of a randomly chosen room type
     */
    private static Room generateRoom(){
        Random rand = new Random();
        if(rand.nextInt(100) < ROOM_GEN_CHANCE_PERC){
            GenRooms type = null;
            int r = rand.nextInt(100) + 1;
            for(int i = 0; i < GenRooms.values().length; i++){
                r -= GenRooms.values()[i].getWeight();
                if(r <= 0){
                    type = GenRooms.values()[i];
                    break;
                }
            }
            switch (type) {
                case BASIC:
                    return new BasicRoom();
                case COMBAT:
                    return new CombatRoom();
                case NPC:
                    return new NPCRoom();
                case SECRET:
                    return new SecretRoom();
                case SHOP:
                    return new Shop();
            }
        }
        return null;
    }

    /**
     * Replace the room at the given coords with a BossRoom while preserving connections to adjacent rooms
     * @param grid reference grid
     * @param coords coordinates of the last room that was added
     */
    private static void addBoss(DungeonGrid grid, DungeonCoords coords){
        Room old = grid.get(coords);
        if(old == null) return;
        BossRoom boss = new BossRoom();
        for(DungeonDirections dir: DungeonDirections.values()){
            Room adj = old.getRoom(dir);
            if(adj != null){
                boss.setRoom(dir, adj);
                adj.setRoom(DungeonDirections.fromIndex(dir.getOpposite()), boss);
            }
        }
        grid.add(coords, boss);
        FileLogger.getInstance().info("Added boss room at " + coords);
    }
}

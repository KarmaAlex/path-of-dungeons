package it.univaq.pathofdungeons.game.impl;

import java.util.List;

import it.univaq.pathofdungeons.domain.dungeon.Dungeon;
import it.univaq.pathofdungeons.domain.dungeon.DungeonCoords;
import it.univaq.pathofdungeons.domain.dungeon.DungeonDirections;
import it.univaq.pathofdungeons.domain.dungeon.DungeonGrid;
import it.univaq.pathofdungeons.domain.dungeon.rooms.Room;
import it.univaq.pathofdungeons.domain.dungeon.rooms.RoomTypes;
import it.univaq.pathofdungeons.domain.entity.player.Player;
import it.univaq.pathofdungeons.game.DungeonService;

public class DungeonServiceImpl implements DungeonService{
    Dungeon dungeon;

    public void setDungeon(Dungeon dungeon){
        this.dungeon = dungeon;
    }

    /**
     * Generates a square representation of the rooms surrounding the current
     * room. Each entry of the matrix contains only the type of the room.
     * @param size size of one of the sides of the square
     * @return a square matrix with the current room at the center
     */
    @Override
    public RoomTypes[][] getRoomGrid(int size){
        RoomTypes[][] out = new RoomTypes[size][size];
        DungeonGrid grid = dungeon.getGrid();
        DungeonCoords currentCoords = grid.find(dungeon.getCurrentRoom());
        int x = Math.max(currentCoords.getX() - size / 4, 0);
        int yBase = Math.max(currentCoords.getY() - size / 4, 0);
        int y = 0;

        for(int i = 1; i < out.length; i+=2){
            y = yBase;
            for(int j = 1; j < out[i].length; j+=2){
                Room room = grid.get(new DungeonCoords(x, y));
                if(room != null){
                    if(!room.getExplored()) out[i][j] = RoomTypes.UNEXPLORED;
                    else out[i][j] = room.getRoomType();
                    for(DungeonDirections d: DungeonDirections.values()){
                        try{
                            RoomTypes type;
                            if(d == DungeonDirections.LEFT || d == DungeonDirections.RIGHT) type = RoomTypes.CORRIDORH;
                            else type = RoomTypes.CORRIDORV;
                            if(room.getRoom(d) != null){ out[i + d.offsetX()][j + d.offsetY()] = type; }
                            else out[i + d.offsetX()][j + d.offsetY()] = RoomTypes.NONE;
                        } catch(IndexOutOfBoundsException e){ continue; }
                    }
                }
                y++;
            }
            x++;
        }
        return out;
    }

    /**
     * Moves the players in the dungeon in the given direction by changing the dungeon's current room value
     * @param dir direction to move in
     * @return true if the room was already explored, false if no movement happened or the room wasn't explored
     */
    @Override
    public boolean movePlayers(DungeonDirections dir){
        Room currentRoom = dungeon.getCurrentRoom();
        if(currentRoom.getRoom(dir) != null){
            dungeon.setCurrentRoom(currentRoom.getRoom(dir));
            boolean wasExplored = dungeon.getCurrentRoom().getExplored();
            dungeon.getCurrentRoom().onEnter();
            return wasExplored;
        }
        return false;
    }

    @Override
    public void interact(List<Player> players){
        this.dungeon.getCurrentRoom().onInteract(players);
    }
}

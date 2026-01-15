package it.univaq.pathofdungeons.domain.dungeon;

import it.univaq.pathofdungeons.domain.dungeon.rooms.Room;
import it.univaq.pathofdungeons.game.DungeonService;

public class Dungeon {
    private Room startingRoom;
    private DungeonGrid grid;
    private Room currentRoom;
    private DungeonService dungeonService;

    public Dungeon(Room sr, DungeonGrid dg){
        this.startingRoom = sr;
        this.grid = dg;
        this.currentRoom = sr;
    }

    public DungeonService getDungeonService(){ return this.dungeonService; }

    public Room getCurrentRoom(){ return this.currentRoom; }

    public void setCurrentRoom(Room room){ this.currentRoom = room; }

    public Room getStartingRoom(){ return this.startingRoom; }

    public DungeonGrid getGrid(){ return this.grid; }

    public void setService(DungeonService ds){ ds.setDungeon(this); this.dungeonService = ds;}

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        String[][] out = new String[grid.getWidth() * 2][grid.getHeight() * 2];

        for(int i = 0; i < out.length; i += 2){
            for(int j = 0; j < out[i].length; j += 2){
                Room currentRoom = grid.get(new DungeonCoords(i/2, j/2));
                if(currentRoom != null){
                    if(currentRoom.equals(this.currentRoom)) out[i][j] = "C";
                    else out[i][j] = "R";
                    for(DungeonDirections d: DungeonDirections.values()){
                        if(currentRoom.getRoom(d) != null){
                            out[i + d.offsetX()][j + d.offsetY()] = "-";
                        }
                        else{
                            try{
                                if(out[i + d.offsetX()][j + d.offsetY()] == null) out[i + d.offsetX()][j + d.offsetY()] = " ";
                            } catch(IndexOutOfBoundsException e){
                                continue;
                            }
                        }
                    }
                }
                else {
                    out[i][j] = " ";
                    for(DungeonDirections d: DungeonDirections.values()){
                        try{
                            if(out[i + d.offsetX()][j + d.offsetY()] == null) out[i + d.offsetX()][j + d.offsetY()] = " ";
                        } catch(IndexOutOfBoundsException e){
                            continue;
                        }
                    }
                }
            }
        }

        for(int i = 1; i < out.length; i += 2){
            for(int j = 0; j < out[i].length; j++){
                if(out[i][j] == null) out[i][j] = " ";
            }
        }

        for(int i = 0; i < out.length; i++){
            for(int j = 0; j < out[i].length; j++){
                sb.append(out[i][j]);
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}

package it.univaq.pathofdungeons.domain.dungeon;

import java.util.ArrayList;

import it.univaq.pathofdungeons.domain.dungeon.rooms.Room;

/**
 * Class that represents the grid representation of the map of the dungeon
 * Used primarily to check for room adjacency when deciding passages between them and for drawing the map on the interface
 */
public class DungeonGrid {

    private ArrayList<ArrayList<Room>> grid;

    public DungeonGrid(){
        grid = new ArrayList<>();
    }

    /**
     * Get the room at the coordinates specified by the given DungeonCoords object
     * @param coords Object that contains the coordinates of the desired room
     * @return The room at the give position
     */
    public Room get(DungeonCoords coords){
        try{
            return grid.get(coords.getX()).get(coords.getY());
        } catch(IndexOutOfBoundsException e){
            return null;
        }
        
    }

    /**
     * Returns the room adjacent to the one in the given coordinates and given direction
     * @param dir Direction to check adjacency in
     * @param coords Room to check adjacency from
     * @return The adjacent room, null otherwise
     */
    public Room getAdj(DungeonDirections dir, DungeonCoords coords){
        if(coords == null) return null;
        try{
            switch (dir) {
                case LEFT:
                    return this.get(new DungeonCoords(coords.getX() - 1, coords.getY()));
                case RIGHT:
                    return this.get(new DungeonCoords(coords.getX() + 1, coords.getY()));
                case UP:
                    return this.get(new DungeonCoords(coords.getX(), coords.getY() - 1));
                case DOWN:
                    return this.get(new DungeonCoords(coords.getX(), coords.getY() + 1));
            }
        } catch(IndexOutOfBoundsException e){
            return null;
        }
        return null;
    }

    /**
     * Adds a room at the coordinates specified by the first parameter
     * @param coords Coordinates of the final location of the room
     * @param room Room object to be added to the grid
     * @return true if addition is successful, false otherwise
     */
    public boolean add(DungeonCoords coords, Room room){
        if(coords == null) return false;
        try{
            grid.get(coords.getX());
        } catch(IndexOutOfBoundsException e){
            this.extendCols(coords.getX());
        }
        try{
            grid.get(coords.getX()).set(coords.getY(), room);
        } catch(IndexOutOfBoundsException e){
            this.extendRow(coords.getX(), coords.getY());
            grid.get(coords.getX()).set(coords.getY(), room);
        }
        return true;
    }

    /**
     * Get the height of the grid
     * @return The maximum height of all columns of the grid
     */
    public int getHeight(){
        int max = 0;
        for(ArrayList<Room> a: grid){
            if(a.size() > max) max = a.size();
        }
        return max;
    }

    /**
     * Get the height of a specific column of the grid
     * @param col Index of the column
     * @return Height of the column
     */
    public int getHeight(int col){
        return grid.get(col).size();
    }

    /**
     * Get the width of the grid
     * @return The width of the first row of the grid
     */
    public int getWidth(){
        return grid.size();
    }

    /**
     * Extends a column to the specified index
     * Note that this means the final size is len + 1
     * @param len The index of the last column in the new grid
     * @return true if extension is successful, false otherwise
     */
    public boolean extendCols(int len){
        if(this.getWidth() >= len + 1) return false;
        while(grid.size() < len + 1){ grid.add(new ArrayList<>()); }
        return true;
    }

    /**
     * Extends the size of the specified row to the specified index
     * @param col Index of the column corresponding to the row to be extended
     * @param len Index of the last entry in the new row
     * @return true if extension is successful, false otherwise
     */
    public boolean extendRow(int col, int len){
        if(this.getHeight(col) >= len + 1) return false;
        while(this.getHeight(col) < len + 1){ grid.get(col).add(null); }
        return true;
    }

    // Currently not needed but may be later
    /**
     * Finds a room and returs its coordinates
     * @param room Room object to find in the grid
     * @return DungeonCoords object containing the position of the room if found, null otherwise
     */
    public DungeonCoords find(Room room){
        if(room == null) return null;
        try{
            for(int i = 0; i < grid.size(); i++){
                for(int j = 0; j < grid.get(i).size(); j++){
                    if(room.equals(grid.get(i).get(j))) return new DungeonCoords(i, j);
                }
            }
        }
        catch(IndexOutOfBoundsException e){
            return null;
        }
        return null;
    }

    /**
     * Directional version of the add function, attempts to add a room adjacent to the source room
     * in the specified direction
     * @param dir Direction to add the room in
     * @param room Room to be added
     * @param coords DungeonCoords representing the position of the source room
     * @return true if addition is successful, false otherwise
     */
    public boolean add(DungeonDirections dir, Room room, DungeonCoords coords){
        if(coords == null) return false;
        switch(dir){
            case LEFT:
                if(coords.getX() == 0){
                    grid.add(0, new ArrayList<>());
                    extendRow(0, coords.getY());
                    grid.get(0).set(coords.getY(), room);
                }
                else{
                    extendRow(coords.getX() - 1, coords.getY());
                    grid.get(coords.getX() - 1).set(coords.getY(), room);
                }
                break;
            case RIGHT:
                if(coords.getX() + 1 == grid.size()){
                    grid.add(new ArrayList<>());
                }
                this.extendRow(coords.getX() + 1, coords.getY());
                grid.get(coords.getX() + 1).set(coords.getY(), room);
                break;
            case UP:
                // If attempting to put a room out of bounds shift all other rooms down by 1
                if(coords.getY() == 0){
                    for(ArrayList<Room> ar: grid){
                        ar.add(0, null);
                    }
                    grid.get(coords.getX()).set(0, room);
                }
                else grid.get(coords.getX()).set(coords.getY() - 1, room);
                break;
            case DOWN:
                if(coords.getY() == grid.get(coords.getX()).size() - 1){
                    grid.get(coords.getX()).add(room);
                }
                else grid.get(coords.getX()).set(coords.getY() + 1, room);
                break;
        }
        return true;
    }

}

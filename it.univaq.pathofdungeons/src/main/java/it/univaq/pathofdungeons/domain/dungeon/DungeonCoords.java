package it.univaq.pathofdungeons.domain.dungeon;

/**
 * Class that represents coordinates in the dungeon in the form of a pair of X and Y coordinates
 */
public class DungeonCoords {
    private int x;
    private int y;

    /**
     * Creates a DungeonCoords from the given coordinates
     * @param x Horizontal coordinate
     * @param y Vertical coordinate
     */
    public DungeonCoords(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){ return this.x; }
    public int getY(){ return this.y; }
    public void setX(int x){ this.x = x; }
    public void setY(int y){ this.y = y; }

    /**
     * Creates new DungeonCoords object with coordinates moved based on given diretion, clamped at 0 to avoid out of bounds movement
     * @param coords DungeonCoords object of the original coordinates
     * @param d Direction to move in
     * @return New DungeonCoords object with modified coordinates
     */
    public static DungeonCoords move(DungeonCoords coords, DungeonDirections d){
        return new DungeonCoords(Math.max(coords.getX() + d.offsetX(), 0), Math.max(coords.getY() + d.offsetY(), 0));
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof DungeonCoords)) return false;
        DungeonCoords dc = (DungeonCoords) o;
        if(this.x == dc.x && this.y == dc.y) return true;
        return false;
    }
}

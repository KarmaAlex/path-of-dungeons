package it.univaq.pathofdungeons.domain.dungeon;

/**
 * Enumerator that represents the possible directions that can be taken when moving from (or generating) a room
 */
public enum DungeonDirections {
    LEFT(0, -1, 0),
    RIGHT(1, 1, 0),
    UP(2, 0, -1),
    DOWN(3, 0, 1);

    private final int index;
    private final int offsetX;
    private final int offsetY;

    private DungeonDirections(int index, int offsetX, int offsetY){
        this.index = index;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    /**
     * Get the index corresponding to the entry
     * @return the index of the corresponding entry
     */
    public int index(){ return this.index; }

    public int offsetX(){ return this.offsetX; }
    public int offsetY(){ return this.offsetY; }

    /**
     * Static method to get an entry given a numerical index
     * @param index The index to attempt to map to an entry
     * @return The DungeonDirections object with the requested index, or null if it does not exist
     */
    public static DungeonDirections fromIndex(int index){
        for(DungeonDirections d: DungeonDirections.values()){ if(d.index == index) return d;}
        return null;
    }
    
    /**
     * Get the opposite direction of the entry it is called from.
     * This assumes the values of the enumerator are declared with opposites being next to eachother,
     * for example LEFT has index 0 while RIGHT has index 1
     * @return The index of the opposite direction
     */
    public int getOpposite(){ return this.index % 2 == 0 ? this.index + 1: this.index - 1; }
}
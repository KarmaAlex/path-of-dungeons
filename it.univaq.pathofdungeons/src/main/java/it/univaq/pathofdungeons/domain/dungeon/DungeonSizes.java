package it.univaq.pathofdungeons.domain.dungeon;

/**
 * Enumerator that represents the possible sizes of the dungeon
 */
public enum DungeonSizes {
    SMALL(30),
    MEDIUM(50),
    LARGE(100),
    EXTRA_LARGE(5000);

    private int size;

    private DungeonSizes(int size){
        this.size = size;
    }

    public int size(){ return this.size; }
}

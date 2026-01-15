package it.univaq.pathofdungeons.domain.dungeon.rooms;

public enum GenRooms {
    BASIC(15),
    SHOP(5),
    NPC(10),
    SECRET(10),
    COMBAT(60);

    private int weight;

    private GenRooms(int weight){
        this.weight = weight;
    }

    public int getWeight(){ return this.weight; }
}

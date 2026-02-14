package it.univaq.pathofdungeons.domain.dungeon.rooms;

/**
 * Empty room
 */
public class BasicRoom extends Room{
    public BasicRoom(){
        super(RoomTypes.BASIC, "Just an empty room");
    }

    @Override
    public void onEnter(){
        this.setExplored(true);
    }
}

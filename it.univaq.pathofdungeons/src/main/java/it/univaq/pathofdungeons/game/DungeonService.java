package it.univaq.pathofdungeons.game;

import it.univaq.pathofdungeons.domain.dungeon.Dungeon;
import it.univaq.pathofdungeons.domain.dungeon.DungeonDirections;
import it.univaq.pathofdungeons.domain.dungeon.rooms.Room;
import it.univaq.pathofdungeons.domain.dungeon.rooms.RoomTypes;

public interface DungeonService {
    void setDungeon(Dungeon dungeon);
    RoomTypes[][] getRoomGrid(int size);
    boolean movePlayers(DungeonDirections dir);
    void interact(Room currentRoom);
}

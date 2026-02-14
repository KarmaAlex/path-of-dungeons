package it.univaq.pathofdungeons.game;

import java.io.Serializable;
import java.util.List;

import it.univaq.pathofdungeons.domain.dungeon.Dungeon;
import it.univaq.pathofdungeons.domain.dungeon.DungeonDirections;
import it.univaq.pathofdungeons.domain.dungeon.rooms.RoomTypes;
import it.univaq.pathofdungeons.domain.entity.player.Player;

public interface DungeonService extends Serializable{
    void setDungeon(Dungeon dungeon);
    RoomTypes[][] getRoomGrid(int size);
    boolean movePlayers(DungeonDirections dir);
    void interact(List<Player> players);
}

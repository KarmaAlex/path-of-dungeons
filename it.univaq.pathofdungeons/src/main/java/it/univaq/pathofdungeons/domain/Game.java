package it.univaq.pathofdungeons.domain;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import it.univaq.pathofdungeons.domain.dungeon.Dungeon;
import it.univaq.pathofdungeons.domain.entity.player.Player;
import it.univaq.pathofdungeons.utils.FileLogger;

/**
 * Class that represents the main game with the dungeon and players
 */
public class Game implements Serializable{
    private Dungeon dungeon;
    private LinkedList<Player> players;

    public Game(List<Player> players, Dungeon dungeon){
        this.players = new LinkedList<>(players);
        this.dungeon = dungeon;
        FileLogger.getInstance().info(String.format("Created game with %d players", players.size()));
    }

    public Dungeon getDungeon(){ return this.dungeon; }

    public List<Player> getPlayers(){ return this.players; }
}

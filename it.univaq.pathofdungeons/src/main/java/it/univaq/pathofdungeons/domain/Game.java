package it.univaq.pathofdungeons.domain;

import java.util.LinkedList;

import it.univaq.pathofdungeons.domain.dungeon.Dungeon;
import it.univaq.pathofdungeons.domain.entity.player.Player;
import it.univaq.pathofdungeons.game.BattleService;

public class Game {
    Dungeon dungeon;
    LinkedList<Player> players;
    BattleService battleManager;

    public Game(LinkedList<Player> players, Dungeon dungeon){
        this.players = players;
        this.dungeon = dungeon;
    }

    public Dungeon getDungeon(){ return this.dungeon; }

    public LinkedList<Player> getPlayers(){ return this.players; }
}

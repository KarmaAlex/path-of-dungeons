package it.univaq.pathofdungeons.domain;

import java.util.LinkedList;
import java.util.List;

import it.univaq.pathofdungeons.domain.dungeon.rooms.Room;
import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.domain.entity.enemies.Enemy;
import it.univaq.pathofdungeons.domain.entity.player.Player;
import it.univaq.pathofdungeons.game.BattleService;
import it.univaq.pathofdungeons.game.impl.BattleServiceImpl;
import it.univaq.pathofdungeons.utils.FileLogger;

/**
 * Class that represents a battle, including enemies, players and battleService
 */
public class Battle {
    private LinkedList<Player> players;
    private LinkedList<Enemy> enemies;
    private int turns;
    private BattleService battleService;
    private Room currentRoom;

    public Battle(List<Player> players, Room room){
        this.turns = 0;
        this.players = new LinkedList<>(players);
        this.enemies = new LinkedList<>(room.getEnemies());
        this.battleService = new BattleServiceImpl(this);
        this.currentRoom = room;
        FileLogger.getInstance().info("Starting battle");
    }

    public int getTurns(){ return this.turns; }
    public void addTurn(){ this.turns++; }
    public List<Player> getPlayers(){ return new LinkedList<>(players); }
    public List<Enemy> getEnemies(){ return new LinkedList<>(enemies); }
    public void removePlayer(Player player){ this.players.remove(player); }
    public void addEnemy(Enemy enemy){ this.enemies.add(enemy); }
    public void removeEnemy(Enemy enemy){ this.enemies.remove(enemy); }
    public void removeEntity(Entity entity){ this.enemies.remove(entity); this.players.remove(entity); }
    public BattleService getBattleService(){ return this.battleService; }
    public boolean isFinished(){ return this.enemies.isEmpty() || this.players.isEmpty(); }
    public Room getRoom(){ return this.currentRoom; }
}

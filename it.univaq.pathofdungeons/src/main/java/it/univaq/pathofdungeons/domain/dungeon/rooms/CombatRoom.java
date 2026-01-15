package it.univaq.pathofdungeons.domain.dungeon.rooms;

import java.util.LinkedList;
import java.util.Random;

import it.univaq.pathofdungeons.domain.entity.enemies.Enemy;
import it.univaq.pathofdungeons.domain.entity.player.Player;
import it.univaq.pathofdungeons.game.EntityStatsNotFoundException;
import it.univaq.pathofdungeons.game.impl.EntityFactory;

public class CombatRoom extends Room{

    private static final int MAX_ENEMIES = 4;

    public CombatRoom(){
        super(RoomTypes.COMBAT);
    }

    @Override
    public void onEnter(){
        if(!this.getExplored()){
            Random rand = new Random();
            LinkedList<Enemy> enemies = this.getEnemies();
            for(int i = 0; i < rand.nextInt(MAX_ENEMIES) + 1; i++){
                try{
                    enemies.add(EntityFactory.getEnemy());
                } catch(EntityStatsNotFoundException e){
                    System.out.println(e.getMessage());
                    return;
                }
            }
            this.setExplored(true);
        }
    }

    @Override
    public void onInteract(LinkedList<Player> players) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onInteract'");
    }
    
}

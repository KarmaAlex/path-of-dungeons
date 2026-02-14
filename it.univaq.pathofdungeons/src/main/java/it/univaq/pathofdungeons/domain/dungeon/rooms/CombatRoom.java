package it.univaq.pathofdungeons.domain.dungeon.rooms;

import java.util.List;
import java.util.Random;

import it.univaq.pathofdungeons.domain.entity.enemies.Enemy;
import it.univaq.pathofdungeons.game.EntityStatsNotFoundException;
import it.univaq.pathofdungeons.game.impl.EntityFactory;
import it.univaq.pathofdungeons.utils.FileLogger;

/**
 * Room that contains a random amount of enemies, generated on enter
 */
public class CombatRoom extends Room{

    private static final int MAX_ENEMIES = 4;

    public CombatRoom(){
        super(RoomTypes.COMBAT, "This is a combat room, congrats on defeating the enemies");
    }

    @Override
    public void onEnter(){
        if(!this.getExplored()){
            Random rand = new Random();
            List<Enemy> enemies = this.getEnemies();
            for(int i = 0; i < rand.nextInt(MAX_ENEMIES) + 1; i++){
                try{
                    enemies.add(EntityFactory.getEnemy());
                } catch(EntityStatsNotFoundException e){
                    FileLogger.getInstance().error(e.getMessage());
                    return;
                }
            }
            this.setExplored(true);
        }
    }
}

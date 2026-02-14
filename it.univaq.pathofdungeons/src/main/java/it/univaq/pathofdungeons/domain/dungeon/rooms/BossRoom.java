package it.univaq.pathofdungeons.domain.dungeon.rooms;

import it.univaq.pathofdungeons.game.EntityStatsNotFoundException;
import it.univaq.pathofdungeons.game.impl.EntityFactory;
import it.univaq.pathofdungeons.utils.FileLogger;

/**
 * Room that contains the boss
 */
public class BossRoom extends Room{

    public BossRoom(){
        super(RoomTypes.BOSS, "");
    }

    @Override
    public void onEnter() {
        if(!this.getExplored()){
            try{
                this.getEnemies().add(EntityFactory.getBoss());
            } catch(EntityStatsNotFoundException e){
                FileLogger.getInstance().error(e.getMessage());
            }
            this.setExplored(true);
        }
    }

}

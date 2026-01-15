package it.univaq.pathofdungeons.game.impl;

import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.domain.entity.EntityStats;
import it.univaq.pathofdungeons.game.BattleService;
import it.univaq.pathofdungeons.game.EntityService;

public class PlayerServiceImpl implements EntityService{

    @Override
    public boolean chooseAction(BattleService bs, Entity entity){ return false; }

    @Override
    public void updateHealth(Entity source, int amount){
        source.updateStat(EntityStats.HEALTH, amount);
    }

}

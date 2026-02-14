package it.univaq.pathofdungeons.game;

import java.io.Serializable;

import it.univaq.pathofdungeons.domain.entity.Entity;

public interface EntityService extends Serializable{
    boolean chooseAction(BattleService bs, Entity entity);
    void updateHealth(Entity source, int amount);
}

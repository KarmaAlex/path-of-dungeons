package it.univaq.pathofdungeons.game;

import it.univaq.pathofdungeons.domain.entity.Entity;

public interface EntityService {
    boolean chooseAction(BattleService bs, Entity entity);
    void updateHealth(Entity source, int amount);
}

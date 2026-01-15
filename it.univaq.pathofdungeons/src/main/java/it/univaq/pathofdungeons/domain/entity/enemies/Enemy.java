package it.univaq.pathofdungeons.domain.entity.enemies;

import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.game.EntityService;

public class Enemy extends Entity {

    private EnemyTypes type;

    public Enemy(EntityService characterService, EnemyTypes type){
        super(characterService);
        this.type = type;
        this.setName(type.toString());
    }

    public EnemyTypes getType(){ return this.type; }

}

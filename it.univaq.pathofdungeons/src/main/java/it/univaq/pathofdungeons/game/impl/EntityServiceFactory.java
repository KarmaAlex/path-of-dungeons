package it.univaq.pathofdungeons.game.impl;

import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.domain.entity.enemies.Enemy;
import it.univaq.pathofdungeons.domain.entity.player.Player;
import it.univaq.pathofdungeons.game.EntityService;

/**
 * Factory that returns entity services for the corresponding entity
 */
public class EntityServiceFactory{

    private EntityServiceFactory(){}

    /**
     * Returns an implementation of the EntityService interface based on what class is passed as an argument
     * @param <T> any class that extends Entity
     * @param entityClass class of the entity that needs a service
     * @return an instance of a class that implements EntityService and is appropriate for that entity. null if there is no implementation
     */
    public static <T extends Entity> EntityService getService(Class<T> entityClass){
        if(entityClass == Player.class){
            return new PlayerServiceImpl();
        }
        else if(entityClass == Enemy.class){
            return new EnemyServiceImpl();
        }
        else return null;
    }
}

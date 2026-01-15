package it.univaq.pathofdungeons.game.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.domain.entity.EntityStats;
import it.univaq.pathofdungeons.domain.entity.enemies.Enemy;
import it.univaq.pathofdungeons.domain.entity.enemies.EnemyTypes;
import it.univaq.pathofdungeons.domain.entity.player.Player;
import it.univaq.pathofdungeons.domain.entity.player.PlayerClasses;
import it.univaq.pathofdungeons.domain.spells.Spells;
import it.univaq.pathofdungeons.game.EntityStatsNotFoundException;

/**
 * Factory that returns new instances of entitites of various types.
 */
public class EntityFactory {
    private static final String EXT = ".properties";
    private static final String PLAYER_BASE = "config/player/";
    private static final String ENEMY_BASE = "config/enemy/";
    private static final String STARTING_SPELL = "startingspell";
    private static final int MAX_ENEMY_GOLD = 100;
    private static final int MIN_ENEMY_GOLD = 10;

    /**
     * Generates a new player with the given class and loads its corresponding loadout
     * @param pClass PlayerClasses object of the desired class
     * @return a new Player object with the specified class and associated stats
     * @throws EntityStatsNotFoundException in case the class has no associated stats/the file is not readable
     */
    public static Player getPlayer(PlayerClasses pClass) throws EntityStatsNotFoundException{
        Player player = new Player(EntityServiceFactory.getService(Player.class), pClass);
        getStats(player, pClass.toString(), PLAYER_BASE);
        InventoryServiceImpl.getInstance().loadLoadout(EquippableFactory.getPlayerLoadout(pClass), player);
        return player;
    }

    /**
     * Generates a new enemy of the given type
     * @param type type of enemy to be generated
     * @return a new Enemy with their respective stats
     * @throws EntityStatsNotFoundException if the enemy stats file isn't readable/doesn't exist
     */
    public static Enemy getEnemy(EnemyTypes type) throws EntityStatsNotFoundException{
        Enemy enemy = new Enemy(EntityServiceFactory.getService(Enemy.class), type);
        getStats(enemy, type.toString(), ENEMY_BASE);
        //TODO: change gold given to entities based on their difficulty rating
        enemy.getInventory().addGold(new Random().nextInt(MIN_ENEMY_GOLD, MAX_ENEMY_GOLD + 1));
        return enemy;
    }

    /**
     * Returns a random enemy using a weighted random function based on the weights
     * defined in the EnemyTypes enum
     * @return a new random Enemy with their respective stats
     * @throws EntityStatsNotFoundException if the enemy stats file isn't readable/doesn't exist
     */
    public static Enemy getEnemy() throws EntityStatsNotFoundException{
        Random rand = new Random();
        int i = 0;
        EnemyTypes type = null;
        for(int r = rand.nextInt(100) + 1; i < EnemyTypes.values().length; i++){
            r -= EnemyTypes.values()[i].getWeight();
            if(r <= 0){
                type = EnemyTypes.values()[i];
                break;
            }
        }
        if(type == null){
            throw new EntityStatsNotFoundException("Errore nella generazione di un nemico casuale");
        }
        Enemy enemy = new Enemy(EntityServiceFactory.getService(Enemy.class), type);
        getStats(enemy, type.toString(), ENEMY_BASE);
        return enemy;
    }

    /**
     * Private methond that takes in an entity and loads it's respective stats
     * @param ent Entity instance the stats of which need to be loaded
     * @param type string representation of the entity type, for example for a player it would be their class and for an enemy their type
     * @param base base path where to look for the stats, changes between players and enemies
     * @throws EntityStatsNotFoundException in case the stats are not found
     */
    private static void getStats(Entity ent, String type, String base) throws EntityStatsNotFoundException{
        Properties prop = new Properties();
        try(InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(base + type.toLowerCase() + EXT)){
            prop.load(stream);
            for(EntityStats stat: EntityStats.values()){
                if(stat == EntityStats.HEALTH) ent.setStat(EntityStats.HEALTH, ent.getStat(EntityStats.MAXHEALTH));
                else if(stat == EntityStats.MANA) ent.setStat(EntityStats.MANA, ent.getStat(EntityStats.MAXMANA));
                else ent.setStat(stat, Integer.parseInt(prop.getProperty(stat.name().toLowerCase())));
            }
            String spell = prop.getProperty(STARTING_SPELL);
            if (spell != null) ent.addSpell(SpellFactory.getSpell(Spells.valueOf(spell.toUpperCase())));
        } catch(IOException e){
            throw new EntityStatsNotFoundException("Errore nella lettura del file per l'entità " + type);
        } catch(NullPointerException e){
            e.printStackTrace();
            throw new EntityStatsNotFoundException("Statistiche per l'entità " + type + " non trovate");
        }
    }
}

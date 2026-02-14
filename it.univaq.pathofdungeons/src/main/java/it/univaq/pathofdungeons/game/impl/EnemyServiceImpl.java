package it.univaq.pathofdungeons.game.impl;

import java.util.Random;

import it.univaq.pathofdungeons.domain.BattleActions;
import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.domain.entity.EntityStats;
import it.univaq.pathofdungeons.domain.entity.player.Player;
import it.univaq.pathofdungeons.game.BattleService;
import it.univaq.pathofdungeons.game.EntityService;
import it.univaq.pathofdungeons.game.MissingManaException;
import it.univaq.pathofdungeons.game.MissingTargetException;
import it.univaq.pathofdungeons.utils.FileLogger;

public class EnemyServiceImpl implements EntityService{

    private Random rand = new Random();
    private static final BattleActions[] enemyActions = new BattleActions[] {BattleActions.ATTACK, BattleActions.DEFEND, BattleActions.SPELL};

    //TODO: possibly implement slightly more sophisticated AI
    //TODO: update using new targeting system
    @Override
    public boolean chooseAction(BattleService bs, Entity entity){
        BattleActions ba = enemyActions[new Random().nextInt(enemyActions.length)];
        Player target = bs.getBattle().getPlayers().get(rand.nextInt(bs.getBattle().getPlayers().size()));
        switch (ba) {
            case ATTACK:
            try{
                bs.attack(entity, target);
                break;
            } catch(MissingTargetException e){
                FileLogger.getInstance().error(e.getMessage());
                break;
            }
            case DEFEND:
                bs.defend();
                break;
            case SPELL:
            try{
                if(entity.getSpells().isEmpty()) bs.attack(entity, target);
                else bs.spell(entity.getSpells().get(rand.nextInt(entity.getSpells().size())), entity, target);
                break;
            } catch (MissingTargetException | MissingManaException e){
                FileLogger.getInstance().error(e.getMessage());
                break;
            }
            default:
                break;
        }
        return true;
    }

    @Override
    public void updateHealth(Entity source, int amount){
        source.updateStat(EntityStats.HEALTH, amount);
    }

}

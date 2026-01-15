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

public class EnemyServiceImpl implements EntityService {

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
                bs.attack(target);
                break;
            } catch(MissingTargetException e){
                e.printStackTrace();
                break;
            }
            case DEFEND:
                bs.defend();
                break;
            case SPELL:
            try{
                if(entity.getSpells().size() < 1) bs.attack(target);
                else bs.spell(target, entity.getSpells().get(rand.nextInt(entity.getSpells().size())));
                break;
            } catch (MissingTargetException e){
                e.printStackTrace();
                break;
            } catch(MissingManaException e){
                e.printStackTrace();
                break;
            }
            default:
                break;
        }
        System.out.println(entity + " chose action " + ba + " targeting player: " + target);
        return true;
    }

    @Override
    public void updateHealth(Entity source, int amount){
        source.updateStat(EntityStats.HEALTH, amount);
    }

}

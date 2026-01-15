package it.univaq.pathofdungeons.game.impl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import it.univaq.pathofdungeons.domain.Battle;
import it.univaq.pathofdungeons.domain.effects.Effect;
import it.univaq.pathofdungeons.domain.effects.Effects;
import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.domain.entity.EntityStats;
import it.univaq.pathofdungeons.domain.entity.enemies.Enemy;
import it.univaq.pathofdungeons.domain.entity.player.Player;
import it.univaq.pathofdungeons.domain.items.Item;
import it.univaq.pathofdungeons.domain.spells.Spell;
import it.univaq.pathofdungeons.game.BattleService;
import it.univaq.pathofdungeons.game.MissingManaException;
import it.univaq.pathofdungeons.game.MissingTargetException;

/**
 * Implementation of the BattleService interface that handles actions entities can take in battle,
 * turn orders and the eventual death of entities.
 */
public class BattleServiceImpl implements BattleService {
    private Battle battle;
    private LinkedList<Entity> turnOrder;
    private Entity head;
    private Entity turnHolder;
    private LinkedList<Enemy> initialEnemies;
    private boolean playersWon;

    public BattleServiceImpl(Battle battle){ 
        this.battle = battle;
        this.turnOrder = new LinkedList<>(battle.getPlayers());
        this.turnOrder.addAll(battle.getEnemies());
        Collections.sort(turnOrder);
        this.head = turnOrder.removeFirst();
        this.turnHolder = head;
        this.turnOrder.add(turnHolder);
        this.initialEnemies = new LinkedList<>(battle.getEnemies());
        this.playersWon = false;
    }

    @Override
    public Battle getBattle(){ return this.battle; }

    @Override
    public void attack(Entity target) throws MissingTargetException{
        float mult = 1;
        if(new Random().nextInt(100) < turnHolder.getStat(EntityStats.CRITCHANCE)) mult = 1.5f;
        target.updateStat(EntityStats.HEALTH, Math.min(-((int)(turnHolder.getStat(EntityStats.PHYSATTACK) * mult) - target.getStat(EntityStats.PHYSDEFENCE)), -1));
        for(Effect e: this.turnHolder.getOnHitEffects()){
            e.apply(target);
        }
        this.checkDeath(target);
    }

    @Override
    public void defend(){
        EffectFactory.getEffect(Effects.DEFEND).apply(turnHolder);
    }

    @Override
    public void special(Entity target){
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'special'");
    }

    @Override
    public void item(Item item){
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'item'");
    }

    @Override
    public void spell(Entity target, Spell spell) throws MissingTargetException, MissingManaException{
        //TODO: Handle missing mana
        if(turnHolder.getStat(EntityStats.MANA) < spell.getCost()) throw new MissingManaException();
        spell.applySpell(turnHolder, target);
        this.turnHolder.updateStat(EntityStats.MANA, -spell.getCost());
        this.checkDeath(target);
    }

    @Override
    public boolean turnStart(){
        for(Effect e: turnHolder.getEffects()){
            e.onTurnStart(turnHolder);
        }
        return this.turnHolder.getEntityService().chooseAction(this, turnHolder);
    }

    @Override
    public void turnEnd(){
        for(Effect e: this.turnHolder.getEffects()){
            e.onTurnEnd(this.turnHolder);
        }
        this.checkDeath(turnHolder);
        if(battle.isFinished()){
            this.battleEnd();
            return;
        }
        if(turnOrder.getFirst().equals(this.head)){
            Collections.sort(this.turnOrder);
            this.head = this.turnOrder.removeFirst();
            this.turnHolder = this.head;
            this.turnOrder.add(this.turnHolder);
        }
        else{
            this.turnHolder = this.turnOrder.removeFirst();
            this.turnOrder.add(this.turnHolder);
        }
    }

    @Override
    public void battleEnd(){
        if(this.battle.getEnemies().size() < 1){
            //Battle was won, generate drops
            this.battle.getRoom().getItems().addAll(EquippableFactory.generateDrops(this.initialEnemies));
            System.out.println("Battle finished and players won");
            //Retrieve and give gold to players
            int goldTotal = 0;
            for(Enemy e: this.initialEnemies) goldTotal+=e.getInventory().getGold();
            int playersSize = this.getBattle().getPlayers().size();
            //Split gold evenly, if not divisible by number of alive players give a random player the rest of the gold
            if(goldTotal % playersSize != 0){
                this.battle.getPlayers().get(new Random().nextInt(playersSize)).getInventory().addGold(goldTotal % playersSize);
            }
            for(Player p: this.battle.getPlayers()) p.getInventory().addGold(goldTotal / playersSize);
            this.playersWon = true;
            return;
        }
        this.playersWon = false;
        System.out.println("Battle finished and enemies won");
        //TODO: Battle has been lost
    }

    @Override
    public boolean isPlayerWin(){ return this.playersWon; }

    @Override
    public int diceRoll(int faces){
        return new Random().nextInt(faces) + 1;
    }

    /**
     * Check if the target entity has died, in that case we remove it from the turn order and from the battle
     * @param target Entity to check the state of 
     */
    private void checkDeath(Entity target){
        if(target.getStat(EntityStats.HEALTH) < 1){
            this.battle.removeEntity(target);
            this.turnOrder.remove(target);
        }
    }

    public Entity getTurnHolder(){ return this.turnHolder; }

}

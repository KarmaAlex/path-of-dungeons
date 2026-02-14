package it.univaq.pathofdungeons.game.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import it.univaq.pathofdungeons.domain.Battle;
import it.univaq.pathofdungeons.domain.effects.Effect;
import it.univaq.pathofdungeons.domain.effects.Effects;
import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.domain.entity.EntityStats;
import it.univaq.pathofdungeons.domain.entity.enemies.Enemy;
import it.univaq.pathofdungeons.domain.entity.player.Player;
import it.univaq.pathofdungeons.domain.items.Item;
import it.univaq.pathofdungeons.domain.items.ItemSlot;
import it.univaq.pathofdungeons.domain.spells.Spell;
import it.univaq.pathofdungeons.game.BattleService;
import it.univaq.pathofdungeons.game.MissingManaException;
import it.univaq.pathofdungeons.game.MissingTargetException;
import it.univaq.pathofdungeons.utils.BattleLogger;
import it.univaq.pathofdungeons.utils.FileLogger;

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

    public BattleServiceImpl(Battle battle){ 
        this.battle = battle;
        this.turnOrder = new LinkedList<>(battle.getPlayers());
        this.turnOrder.addAll(battle.getEnemies());
        Collections.sort(turnOrder);
        this.head = turnOrder.removeFirst();
        this.turnHolder = head;
        this.turnOrder.add(turnHolder);
        this.initialEnemies = new LinkedList<>(battle.getEnemies());
    }

    @Override
    public Battle getBattle(){ return this.battle; }

    @Override
    public void attack(Entity source, Entity target) throws MissingTargetException{
        BattleLogger.getInstance().info(String.format("%s attacks %s", source.getName(), target.getName()));
        float mult = 1;
        if(new Random().nextInt(100) < turnHolder.getStat(EntityStats.CRITCHANCE)){
            mult = 1.5f;
            BattleLogger.getInstance().info("Critical hit!");
        }
        int damage = EffectService.applyOnHitEffects(target, (int)(turnHolder.getStat(EntityStats.PHYSATTACK) * mult), true);
        if(damage > 0) target.takeDamage(damage, true);
        for(Effect e: this.turnHolder.getOnHitEffects()){
            EffectService.applyEffect(e, source, target);
        }
        this.checkDeath(target);
    }

    @Override
    public void defend(){
        EffectService.applyEffect(EffectFactory.getEffect(Effects.DEFEND), turnHolder, turnHolder);
    }

    @Override
    public void special(Entity source, Entity target) throws MissingTargetException{
        BattleLogger.getInstance().info(String.format("%s uses their special: %s against %s", source.getName(), source.getSpecial().toString(), target.getName()));
        int damage = EffectService.applyOnHitEffects(target, source.getSpecial().getDamage(), false);
        source.getSpecial().useSpecial(source, target);
        if(damage > 0){
            target.takeDamage(damage, false);
        }
        this.checkDeath(target);
    }

    @Override
    public void special(Entity source, List<Entity> targets) throws MissingTargetException{
        BattleLogger.getInstance().info(String.format("%s uses their special: %s against multiple enemies", source.getName(), source.getSpecial().toString()));
        source.getSpecial().useSpecial(source, targets);
        for(Entity target: targets){
            if(target.isAlive()){
                int damage = EffectService.applyOnHitEffects(target, source.getSpecial().getDamage(), false);
                if(damage > 0){
                    target.takeDamage(damage, false);
                }
                this.checkDeath(target);
            }
        }
    }

    @Override
    public void item(Entity source, ItemSlot slot){
        BattleLogger.getInstance().info(String.format("%s uses %s", source.getName(), slot.getItem().toString()));
        InventoryService.useItem(source, slot);
    }

    @Override
    public void spell(Spell spell, Entity source, Entity target) throws MissingTargetException, MissingManaException{
        if(turnHolder.getStat(EntityStats.MANA) < spell.getCost()) throw new MissingManaException("Not enough mana to use spell");
        BattleLogger.getInstance().info(String.format("%s uses %s against %s", source.getName(), spell.toString(), target.getName()));
        this.turnHolder.updateStat(EntityStats.MANA, -spell.getCost());
        spell.applySpell(turnHolder, target);
        int damage = EffectService.applyOnHitEffects(target, spell.getDamage(), false);
        if(damage > 0) target.takeDamage(damage, false);
        this.checkDeath(target);
    }

    @Override
    public boolean turnStart(){
        EffectService.onTurnStart(turnHolder);
        return this.turnHolder.getEntityService().chooseAction(this, turnHolder);
    }

    @Override
    public void turnEnd(){
        EffectService.onTurnEnd(turnHolder);
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
        if(this.isPlayerWin()){
            //Battle was won, generate drops
            for(Player p: this.battle.getPlayers()){
                Collection<Item> drops = ItemFactory.getLootDrops(initialEnemies);
                for(Item i: drops){
                    InventoryService.addItemToInventory(p, i);
                }
            }
            FileLogger.getInstance().info("Battle finished and players won");
            //Retrieve and give gold to players
            int goldTotal = 0;
            for(Enemy e: this.initialEnemies) goldTotal += e.getInventory().getGold();
            FileLogger.getInstance().info("Recieved " + goldTotal + " gold, split: " + goldTotal / this.battle.getPlayers().size());
            int playersSize = this.getBattle().getPlayers().size();
            //Split gold evenly, if not divisible by number of alive players give a random player the rest of the gold
            if(goldTotal % playersSize != 0){
                Player p = this.battle.getPlayers().get(new Random().nextInt(playersSize));
                InventoryService.addGold(p, goldTotal % playersSize);
            }
            for(Player p: this.battle.getPlayers()) InventoryService.addGold(p, goldTotal / playersSize);
            return;
        }
        FileLogger.getInstance().info("Battle finished and enemies won");
    }

    @Override
    public boolean isPlayerWin(){return this.battle.getEnemies().isEmpty() && this.battle.getPlayers().size() >= 1;}

    /**
     * Check if the target entity has died, in that case we remove it from the turn order and from the battle
     * @param target Entity to check the state of 
     */
    private void checkDeath(Entity target){
        if(target.getStat(EntityStats.HEALTH) < 1){
            BattleLogger.getInstance().info(String.format("%s has died", target.getName()));
            this.battle.removeEntity(target);
            this.turnOrder.remove(target);
        }
    }

    public Entity getTurnHolder(){ return this.turnHolder; }

}

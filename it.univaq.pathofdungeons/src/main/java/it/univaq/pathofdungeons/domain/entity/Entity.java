package it.univaq.pathofdungeons.domain.entity;

import java.util.HashMap;
import java.util.LinkedList;

import it.univaq.pathofdungeons.domain.BasicAttack;
import it.univaq.pathofdungeons.domain.BattleAction;
import it.univaq.pathofdungeons.domain.BattleActions;
import it.univaq.pathofdungeons.domain.Defend;
import it.univaq.pathofdungeons.domain.SpecialAttack;
import it.univaq.pathofdungeons.domain.TargetTypes;
import it.univaq.pathofdungeons.domain.effects.Effect;
import it.univaq.pathofdungeons.domain.effects.Effects;
import it.univaq.pathofdungeons.domain.spells.Spell;
import it.univaq.pathofdungeons.game.EntityService;

/**
 * Class that represents a generic entity with stats, a series of actions they can perform in battle,
 * spells they can use, effects and an associated EntityService.
 */
public abstract class Entity implements Comparable<Entity>{
    private final static int MAX_SPELLS = 5;
    private String name;

    private HashMap<EntityStats, Integer> stats;
    private HashMap<BattleActions, BattleAction> battleActions;
    
    private LinkedList<Effect> onHitEffects;
    private LinkedList<Effect> effects;
    private LinkedList<Spell> spells;

    private EntityService entityService;
    private EntityInventory inventory;

    public Entity(EntityService characterService){
        this.entityService = characterService;
        this.onHitEffects = new LinkedList<>();
        this.effects = new LinkedList<>();
        this.stats = new HashMap<>();
        this.spells = new LinkedList<>();
        this.battleActions = new HashMap<>();
        this.battleActions.put(BattleActions.ATTACK, new BasicAttack());
        this.battleActions.put(BattleActions.DEFEND, new Defend());
        inventory = new EntityInventory();
    }

    public Entity(EntityService characterService, SpecialAttack spA){
        this(characterService);
        this.battleActions.put(BattleActions.SPECIAL, spA);
    }

    public String getName() { return name; }

    /**
     * Updates the entity's stat by adding amount
     * @param stat EntityStats object of the stat that needs to be updated
     * @param amount amount to add to the stat's value
     */
    public void updateStat(EntityStats stat, int amount){
        switch(stat){
            case HEALTH:
                this.updateHealth(amount);
                break;
            case MANA:
                this.updateMana(amount);
                break;
            case MAXHEALTH:
                this.updateMaxHealth(amount);
                break;
            case MAXMANA:
                this.updateMaxMana(amount);
                break;
            default:
                this.stats.put(stat, this.stats.get(stat) + amount);
        }
    }

    /**
     * Sets the entity's stat to a specific amount
     * @param stat stat to be updated
     * @param amount value to set the stat to
     */
    public void setStat(EntityStats stat, int amount){
        this.stats.put(stat, amount);
    }

    public int getStat(EntityStats stat){ return stats.get(stat); }

    private void updateHealth(int amount){ this.stats.put(EntityStats.HEALTH, Math.min(Math.max(this.stats.get(EntityStats.HEALTH) + amount, 0), this.stats.get(EntityStats.MAXHEALTH))); }

    private void updateMana(int amount){ this.stats.put(EntityStats.MANA, Math.min(Math.max(this.stats.get(EntityStats.MANA) + amount, 0), this.stats.get(EntityStats.MAXMANA))); }

    private void updateMaxHealth(int amount){ this.stats.put(EntityStats.MAXHEALTH, this.stats.get(EntityStats.MAXHEALTH) + amount); this.updateHealth(amount); }

    private void updateMaxMana(int amount){ this.stats.put(EntityStats.MAXMANA, this.stats.get(EntityStats.MAXMANA) + amount); this.updateMana(amount); }

    public LinkedList<Effect> getOnHitEffects() { return new LinkedList<>(onHitEffects); }

    public void addOnHitEffect(Effect effect) { 
        if(onHitEffects.contains(effect)) return;
        onHitEffects.add(effect);
    }

    public boolean removeOnHitEffect(Effects effect) { 
        for(Effect e: onHitEffects){
            if(e.getType().equals(effect)) {
                return onHitEffects.remove(e);
            }
        }
        return false;
    }

    public LinkedList<Effect> getEffects() { return new LinkedList<>(effects); }

    public void addEffect(Effect effect) { effects.add(effect); }

    public void removeEffect(Effect effect) { effects.remove(effect); }

    public EntityService getEntityService() { return entityService; }

    public void setName(String name){ this.name = name; }

    public LinkedList<Spell> getSpells(){ return new LinkedList<>(this.spells); }

    /**
     * Adds a spell to the entity's spell list as long as its size does not exceed MAX_SPELLS value
     * @param spell spell to be added
     * @return true if the spell is added, false otherwise
     */
    public boolean addSpell(Spell spell){
        if(this.spells.size() == MAX_SPELLS) return false;
        this.spells.add(spell);
        return true;
    }

    public void removeSpell(Spell spell){ spells.remove(spell); }

    /**
     * Returns the target type of the given action
     * @param ba BattleActions object of the action we want to know the targeting of
     * @return TargetTypes object representing the targeting type of the action
     */
    public TargetTypes getActionTargets(BattleActions ba){ return this.battleActions.get(ba).equals(null) ? null : this.battleActions.get(ba).getTargets(); }

    public EntityInventory getInventory(){ return this.inventory; }

    @Override
    public int compareTo(Entity e){
        return this.getStat(EntityStats.SPEED)-e.getStat(EntityStats.SPEED);
    }

    @Override
    public String toString(){
        return this.name;
    }
}

package it.univaq.pathofdungeons.domain.specials;

import java.util.List;

import it.univaq.pathofdungeons.domain.BattleAction;
import it.univaq.pathofdungeons.domain.TargetTypes;
import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.game.MissingTargetException;

/**
 * Class that represents a special attack
 */
public abstract class SpecialAttack extends BattleAction{
    private String name;
    private int damage;

    protected SpecialAttack(TargetTypes tt, String name, int damage){ 
        super(tt);
        this.name = name;
        this.damage = damage;
    }

    public String getName(){return this.name;}
    public int getDamage(){return this.damage;}

    /**
     * Use the special on the target entity
     * @param source
     * @param target
     * @throws MissingTargetException if target is not valid for the move's targeting type
     */
    public void useSpecial(Entity source, Entity target) throws MissingTargetException{
        throw new MissingTargetException("Cannot use ability on a single target");
    }

    /**
     * Use the special on a list of targets
     * @param source
     * @param targets
     * @throws MissingTargetException if the move does not support multiple targeting
     */
    public void useSpecial(Entity source, List<Entity> targets) throws MissingTargetException{
        throw new MissingTargetException("Cannot use ability on multiple targets");
    }

    @Override
    public String toString(){
        return this.name;
    }
}

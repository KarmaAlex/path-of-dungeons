package it.univaq.pathofdungeons.game;

import it.univaq.pathofdungeons.domain.Battle;
import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.domain.items.Item;
import it.univaq.pathofdungeons.domain.spells.Spell;

public interface BattleService {
    /**
     * Method that handles attacks coming from the current turn holder to a target entity
     * @param target target of the attack
     * @throws MissingTargetException if target is null
     */
    void attack(Entity target) throws MissingTargetException;
    /**
     * Handles an entity defending itself
     */
    void defend();
    /**
     * Handles an entity using a special ability against the target
     * @param target target of the ability
     * @throws MissingTargetException if target is required and null
     */
    void special(Entity target) throws MissingTargetException;
    /**
     * Handles the use of an item by the current turn holder
     * @param item item to be used
     */
    void item(Item item);
    /**
     * Handles the usage of spells against a target
     * @param target target of the spell
     * @param spell spell to be used
     * @throws MissingTargetException if the spell requires a target and none is passed
     * @throws MissingManaException if the user of the spell lacks the mana to use it
     */
    void spell(Entity target, Spell spell) throws MissingTargetException, MissingManaException;
    /**
     * Handles turn start logic.
     * @return true if turn start is successful and an action is selected, false otherwise
     */
    boolean turnStart();
    /**
     * Handles actions to be ran at the end of the current turn
     */
    void turnEnd();
    void battleEnd();
    boolean isPlayerWin();
    int diceRoll(int faces);
    Battle getBattle();
    Entity getTurnHolder();
}

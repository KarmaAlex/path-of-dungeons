package it.univaq.pathofdungeons.controller;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import it.univaq.pathofdungeons.domain.Battle;
import it.univaq.pathofdungeons.domain.BattleActions;
import it.univaq.pathofdungeons.domain.TargetTypes;
import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.domain.entity.EntityStats;
import it.univaq.pathofdungeons.domain.items.ItemSlot;
import it.univaq.pathofdungeons.domain.items.Slot;
import it.univaq.pathofdungeons.domain.items.consumables.HealthPotion;
import it.univaq.pathofdungeons.domain.spells.Spell;
import it.univaq.pathofdungeons.game.MissingManaException;
import it.univaq.pathofdungeons.game.MissingTargetException;
import it.univaq.pathofdungeons.game.impl.InventoryService;
import it.univaq.pathofdungeons.utils.BattleLogger;
import it.univaq.pathofdungeons.utils.FileLogger;
import it.univaq.pathofdungeons.view.ViewDispatcher;
import it.univaq.pathofdungeons.view.ViewException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.scene.input.KeyCode;

/**
 * Controller class that handles the battle view. This takes care of
 * turn representing the current state of the battle and lets the player
 * use actions when it is their turn.
 */
public class BattleViewController implements DataInitializable<Battle>{
    @FXML
    private HBox root;
    @FXML
    private HBox playerStatus, enemyStatus;
    @FXML
    private Button attackButton, spellButton, spellBtn1, spellBtn2, spellBtn3, spellBtn4, spellBtn5, defendButton,
    specialButton, itemButton, spellBack, itemBack, itemBtn1, itemBtn2, itemBtn3, itemBtn4, itemBtn5;
    @FXML
    private GridPane actionsPane, spellsPane, itemPane;
    @FXML
    private TextFlow battleLog;
    @FXML
    private ScrollPane scrollPane;

    private Battle battle;
    private LinkedList<EntityStatus> playerStatuses, enemyStatuses;
    private LinkedList<Button> actionButtons, spellButtons, itemButtons;
    private LinkedList<SelectableEntity> playerSelectors, enemySelectors;
    private Entity turnHolder, lastTurnHolder;

    @Override
    public void initialize(Battle data) {
        this.actionButtons = new LinkedList<>(Arrays.asList(attackButton, spellButton, defendButton, specialButton, itemButton));
        this.spellButtons = new LinkedList<>(Arrays.asList(spellBtn1, spellBtn2, spellBtn3, spellBtn4, spellBtn5));
        this.itemButtons = new LinkedList<>(Arrays.asList(itemBtn1, itemBtn2, itemBtn3, itemBtn4, itemBtn5));
        this.battle = data;
        this.playerStatuses = new LinkedList<>();
        this.enemyStatuses = new LinkedList<>();
        BattleLogger.initPane(this.battleLog);
        
        root.setOnKeyReleased(event->{
            switch(event.getCode()){
                case KeyCode.DIGIT1:
                    attackButton.fire();
                    break;
                case KeyCode.DIGIT2:
                    defendButton.fire();
                    break;
                case KeyCode.DIGIT3:
                    spellButton.fire();
                    break;
                case KeyCode.DIGIT4:
                    specialButton.fire();
                    break;
                case KeyCode.DIGIT5:
                    itemButton.fire();
                    break;
                //debug
                case KeyCode.H:
                    InventoryService.addItemToInventory(turnHolder, new HealthPotion(5));
                    break;
                default:
                    break;
            }
        });

        attackButton.setOnAction(event-> this.useAction(BattleActions.ATTACK));
        defendButton.setOnAction(event-> this.useAction(BattleActions.DEFEND));
        specialButton.setOnAction(event-> this.useAction(BattleActions.SPECIAL));

        spellButton.setOnAction(event->toggleSpellsPane());
        spellBack.setOnAction(event->toggleSpellsPane());

        itemButton.setOnAction(event->toggleItemPane());
        itemBack.setOnAction(event->toggleItemPane());

        this.updateStatuses();
        this.handleTurnStart();
    }

    @Override
    public void postInitialize(){
        root.requestFocus();
    }

    /**
     * Toggles the visibility of the spells pane and main actions pane.
     * If the pane becomes visible it also populates the buttons with the correct spells that belong 
     * to the player who holds the turn
     */
    private void toggleSpellsPane(){
        this.spellsPane.setVisible(!this.spellsPane.visibleProperty().get());
        this.actionsPane.setVisible(!actionsPane.visibleProperty().get());
        //Skip spell setup if the toggle made the panel invisible
        if(!this.spellsPane.isVisible()) return;
        //Slight optimization, check if current turn holder is the same as the last time this was run, in that case don't update spells
        if(this.lastTurnHolder != null && this.lastTurnHolder.equals(this.turnHolder)) return;
        for(Button b: this.spellButtons){ b.setVisible(false); }
        List<Spell> spells = this.turnHolder.getSpells();
        for(int i = 0; i < spells.size(); i++){
            this.spellButtons.get(i).setVisible(true);
            this.spellButtons.get(i).setText(spells.get(i).toString());
            // Workaround for setting the spells without using i as it cannot be final
            int spellId = i;
            this.spellButtons.get(i).setOnAction(event->{ this.useAction(spellId); });
        }
        this.lastTurnHolder = this.turnHolder;
    }

    /**
     * Toggles the visibility of the items pane and if it's made visible
     * populates it taking the first 5 consumable items from the player's inventory
     */
    private void toggleItemPane(){
        this.itemPane.setVisible(!this.itemPane.visibleProperty().get());
        this.actionsPane.setVisible(!this.actionsPane.visibleProperty().get());
        if(!this.itemPane.isVisible()) return;
        for(Button b: this.itemButtons) b.setVisible(false);
        List<Slot> slots = this.turnHolder.getInventory().getItemSlots();
        int i = 0;
        for(Slot s: slots){
            if(s.getItem() != null && !(s.getItem().isEquippable()) && i < 5){
                this.itemButtons.get(i).setVisible(true);
                this.itemButtons.get(i).setText(s.getItem().toString());
                this.itemButtons.get(i).setOnAction(event->{this.useItem((ItemSlot)s); this.toggleItemPane();});
                i++;
            }
        }
    }

    /**
     * Updates the visible statuses for both players and enemies
     */
    private void updateStatuses(){
        updateStatus(new LinkedList<>(battle.getPlayers()), playerStatuses, playerStatus, true);
        updateStatus(new LinkedList<>(battle.getEnemies()), enemyStatuses, enemyStatus, false);
    }

    /**
     * Updates visible statuses of a given list of entities
     * @param entities list of entities to read the statuses from
     * @param statuses list of EntityStatus items to update
     * @param statusElement visible element that contains the statuses that need updating
     * @param isPlayers whether or not we are updating player stats or not. Used to correctly set up selectors
     */
    private void updateStatus(LinkedList<Entity> entities, LinkedList<EntityStatus> statuses, HBox statusElement, boolean isPlayers){
        if(entities.size() < statuses.size() || statuses.isEmpty()){
            if(isPlayers) playerSelectors = new LinkedList<>();
            else enemySelectors = new LinkedList<>();
            LinkedList<LinkedList<Node>> entityInfo = new LinkedList<>();
            // Add nodes to the entityInfo linked list, in order they are Button (name), Health and Mana
            for(Node n: statusElement.getChildren()){
                VBox vb = (VBox)n;
                vb.getChildren().forEach(node -> node.setVisible(false));
                entityInfo.add(new LinkedList<>(vb.getChildren()));
            }
            statuses = new LinkedList<>();
            for(int i = 0; i < entities.size(); i++){
                Entity entity = entities.get(i);
                SelectableEntity se = new SelectableEntity((Button)entityInfo.get(i).get(0), entity);
                if(isPlayers) playerSelectors.add(se);
                else enemySelectors.add(se);
                entityInfo.get(i).forEach(node -> node.setVisible(true));
                statuses.add(new EntityStatus(se, (Labeled)entityInfo.get(i).get(1), (Labeled)entityInfo.get(i).get(2),
                entity.getName(), entity.getStat(EntityStats.HEALTH), entity.getStat(EntityStats.MAXHEALTH),
                entity.getStat(EntityStats.MANA), entity.getStat(EntityStats.MAXMANA)));
            }
        }
        else{
            for(int i = 0; i < entities.size(); i++){
                statuses.get(i).setHealth(entities.get(i).getStat(EntityStats.HEALTH));
                statuses.get(i).setMaxHealth(entities.get(i).getStat(EntityStats.MAXHEALTH));
                statuses.get(i).setMana(entities.get(i).getStat(EntityStats.MANA));
                statuses.get(i).setMaxMana(entities.get(i).getStat(EntityStats.MAXMANA));
            }
        }
    }

    /**
     * Function that is ran at the start of a turn. Makes buttons accessible if the turn holder is a player,
     * otherwise it just skips ahead
     */
    private void handleTurnStart(){
        this.disableButtons();
        if(!battle.getBattleService().turnStart()){
            this.enableButtons();
            this.turnHolder = battle.getBattleService().getTurnHolder();
        }
        else this.handleTurnEnd();
    }

    /**
     * Handles the end of turns, will return to the map view if the battle is over or 
     * update statuses and start the next turn 
     */
    private void handleTurnEnd(){
        if(this.battle.isFinished()){
            this.battle.getBattleService().battleEnd();
            if(this.battle.getBattleService().isPlayerWin()){
                ViewDispatcher.getInstance().previousView();
            }
            else{
                try{
                    ViewDispatcher.getInstance().gameOver();
                } catch(ViewException e){
                    FileLogger.getInstance().error(e.getMessage());
                }
            }
        }
        else{
            if(spellsPane.isVisible()) toggleSpellsPane();
            battle.getBattleService().turnEnd();
            this.updateStatuses();
            this.scrollPane.setVvalue(1.0);
            this.handleTurnStart();
        }
    }

    //TODO: spaghetti methods, at least change names

    /**
     * Setup selectors for entities when performing a battle action on them.
     * For spells use the overloaded method taking in only the spell id
     * @param ba action that is being performed on the selected entity.
     */
    private void setupSelectors(BattleActions ba){
        TargetTypes tt = this.turnHolder.getActionTargets(ba);
        this.setupSelectors(tt, ba, -1);
    }

    /**
     * Setup selectors for entities when using a spell on them.
     * For other battle actions use the overloaded method that takes in the action
     * @param spellId id of the spell to be used on the entity
     */
    private void setupSelectors(int spellId){
        this.setupSelectors(turnHolder.getSpells().get(spellId).getTargets(), BattleActions.SPELL, spellId);
    }

    /**
     * Setup selectors for entities when performing any battle action.
     * @param tt TargetTypes object representing which entities can be targeted by the action
     * @param ba BattleActions object representing the action to be performed
     * @param spellId id of the spell if action is a spell, pass a negative value otherwise
     */
    private void setupSelectors(TargetTypes tt, BattleActions ba, int spellId){
        for(SelectableEntity se: playerSelectors){ se.getButton().setDisable(true); }
        for(SelectableEntity se: enemySelectors){ se.getButton().setDisable(true); }
        if(tt.equals(TargetTypes.ALL) || tt.equals(TargetTypes.ALLY) || tt.equals(TargetTypes.ALL_BUT_SELF) || tt.equals(TargetTypes.ALLY_SELF)){
            for(SelectableEntity se: playerSelectors){
                if(se.getEntity().equals(this.turnHolder) || !se.getEntity().isAlive()) continue;
                this.assingAction(se, ba, spellId);
            }
        }
        if(tt.equals(TargetTypes.ALL) || tt.equals(TargetTypes.ENEMY) || tt.equals(TargetTypes.ALL_BUT_SELF)){
            for(SelectableEntity se: enemySelectors){ this.assingAction(se, ba, spellId); }
        }
        if(tt.equals(TargetTypes.ALL) || tt.equals(TargetTypes.ALLY_SELF)){
            for(SelectableEntity se: playerSelectors){
                if(se.getEntity().equals(this.turnHolder)){
                    this.assingAction(se, ba, spellId);
                    break;
                }
            }
        }
    }

    /**
     * Use a battle action. If the action targets the current player use it directly,
     * otherwise setup the selectors. For spells use the overloaded method that takes the
     * spell id
     * @param ba action to be performed 
     */
    private void useAction(BattleActions ba){
        if(this.turnHolder.getActionTargets(ba).equals(TargetTypes.SELF)){
            useAction(ba, this.turnHolder);
            return;
        }
        else if(this.turnHolder.getActionTargets(ba).equals(TargetTypes.ALL_ENEMIES)){
            useAction(ba, new LinkedList<>(this.battle.getEnemies()));
        }
        this.setupSelectors(ba);
    }

    /**
     * Use a spell. If the spell targets the current player use it directly, otherwise
     * setup selectors. For other actions use the overloaded method that takes in the action
     * @param spellId id of the spell to be used
     */
    private void useAction(int spellId){
        if(this.turnHolder.getSpells().get(spellId).getTargets().equals(TargetTypes.SELF)){
            useSpell(spellId, this.turnHolder);
            return;
        }
        this.setupSelectors(spellId);
    }

    /**
     * Use a battle action. For spells use the useSpell method.
     * @param ba Action to be used
     * @param target target of the action. Should not be null.
     */
    private void useAction(BattleActions ba, Entity target){
        switch (ba){
            case ATTACK:
                try{
                    this.battle.getBattleService().attack(turnHolder, target);
                    this.handleTurnEnd();
                } catch(MissingTargetException e){
                    FileLogger.getInstance().error(e.getMessage());
                }
                break;
            case DEFEND:
                this.battle.getBattleService().defend();
                break;
            case SPECIAL:
                try{
                    this.battle.getBattleService().special(turnHolder, target);
                    this.handleTurnEnd();
                } catch(MissingTargetException e){
                    FileLogger.getInstance().error(e.getMessage());
                }
                break;
            default:
                break;
        }
    }

    /**
     * Method specific to battleActions that target multiple enemies,
     * in this implementation the only supported method is special attacks
     * @param ba battle action to be used
     * @param targets list of targets
     */
    private void useAction(BattleActions ba, List<Entity> targets){
        if(ba.equals(BattleActions.SPECIAL)){
            try{
                this.battle.getBattleService().special(turnHolder, targets);
                this.handleTurnEnd();
            } catch(MissingTargetException e){
                FileLogger.getInstance().error(e.getMessage());
            }
        }
    }

    /**
     * Use a spell on the target entity
     * @param spellId id of the spell to be used
     * @param target entity to use the spell on. Should not be null
     */
    private void useSpell(int spellId, Entity target){
        try{
            battle.getBattleService().spell(turnHolder.getSpells().get(spellId), turnHolder, target);
            this.handleTurnEnd();
        } catch(MissingTargetException | MissingManaException e){
            FileLogger.getInstance().error(e.getMessage());
        }
    }

    /**
     * Internal method that calls the battleservice's item use method and ends the turn
     * @param slot
     */
    private void useItem(ItemSlot slot){
        battle.getBattleService().item(turnHolder, slot);
        this.handleTurnEnd();
    }

    /**
     * Assing a battle action to the button that contains the name of the entity inside
     * the SelectableEntity object
     * @param se SelectableEntity object of the target entity
     * @param ba action to be assigned to the button
     * @param spellId id of the spell to be used, if action is not a spell use a negative value
     */
    private void assingAction(SelectableEntity se, BattleActions ba, int spellId){
        if(spellId < 0){
            se.getButton().setOnAction(event-> useAction(ba, se.getEntity()));
        }
        else{
            se.getButton().setOnAction(event-> useSpell(spellId, se.getEntity()));
        }
        se.getButton().setDisable(false);
    }

    /**
     * Disable the player's action buttons
     */
    private void disableButtons(){
        for(Button b: this.actionButtons){ b.setDisable(true); }
    }

    /**
     * Enable the player's action buttons
     */
    private void enableButtons(){
        for(Button b: this.actionButtons){ b.setDisable(false); }
    }
    
}

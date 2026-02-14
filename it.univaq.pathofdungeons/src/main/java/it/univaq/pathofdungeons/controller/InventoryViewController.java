package it.univaq.pathofdungeons.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.domain.entity.EntityInventory;
import it.univaq.pathofdungeons.domain.entity.EntityStats;
import it.univaq.pathofdungeons.domain.items.EquipSlots;
import it.univaq.pathofdungeons.domain.items.Slot;
import it.univaq.pathofdungeons.view.ViewDispatcher;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Class that handles the controller in the inventory screen.
 * Mainly does initialization, item moving logic is handled by the slots
 */
public class InventoryViewController implements DataInitializable<Entity>{

    private static final String GOLD_PREFIX = "Gold: ";
    private static final String GOLD_SUFFIX = "G";
    private static final Font STATS_FONT = new Font(15.0);
    private static Slot currentSlot;
    private static Entity currentEntity;

    @FXML
    private Pane root;
    @FXML
    private GridPane inventoryGrid;
    @FXML
    private GridPane equipGrid;
    @FXML
    private StackPane helmetSlot, weaponSlot, chestpieceSlot, leftringSlot, rightringSlot, beltSlot, bootsSlot, glovesSlot, trashSlot;
    @FXML
    private Button backBtn;
    @FXML
    private Label gold;
    @FXML
    private VBox statsPane;

    private static Entity data;
    private static VBox stats;

    private HashMap<EquipSlots, InventorySlot> equips;
    private ArrayList<InventorySlot> inventory;
    @SuppressWarnings("unused")
    private TrashSlot trash;

    @Override
    public void initialize(Entity data) {
        currentEntity = data;
        InventoryViewController.stats = this.statsPane;
        InventoryViewController.data = data;
        EntityInventory inv = data.getInventory();
        this.initializeEquips(data);
        this.initializeInventory(data);
        //Load gold
        this.gold.setText(GOLD_PREFIX + inv.getGold() + GOLD_SUFFIX);
        //Set up stats
        InventoryViewController.updateStats();
        backBtn.setOnAction(event-> ViewDispatcher.getInstance().previousView());
    }

    /**
     * Populate equipment slots from the target entity's inventory
     * @param ent target entity
     */
    private void initializeEquips(Entity ent){
        EntityInventory inv = ent.getInventory();
        ArrayList<StackPane> equipSlots = new ArrayList<>(Arrays.asList(helmetSlot, chestpieceSlot, bootsSlot, beltSlot, weaponSlot, glovesSlot, leftringSlot, rightringSlot));
        this.equips = new HashMap<>();
        for(int i = 0; i < EquipSlots.values().length; i++){
            this.equips.put(EquipSlots.values()[i], new InventorySlot(equipSlots.get(i), inv.getEquipSlot(EquipSlots.values()[i])));
        }
    }

    /**
     * Populate inventory slots from the target entity's inventory
     * @param ent target entity
     */
    private void initializeInventory(Entity ent){
        EntityInventory inv = ent.getInventory();
        ArrayList<Node> invSlots = new ArrayList<>(inventoryGrid.getChildren());
        this.inventory = new ArrayList<>();
        for(int i = 0; i < inv.getItemSlots().size(); i++){
            this.inventory.add(new InventorySlot((StackPane)invSlots.get(i), inv.getItemSlots().get(i)));
        }
        trash = new TrashSlot(trashSlot);
    }

    /**
     * Static method used to update stats, mainly called when swapping items from different slots
     */
    public static void updateStats(){
        stats.getChildren().clear();
        for(EntityStats es: EntityStats.values()){
            Label l = new Label();
            l.fontProperty().set(STATS_FONT);
            l.setText(es.toString() + ": " + data.getStat(es));
            stats.getChildren().add(l);
        }
    }

    public static Slot getCurrentSlot(){ return currentSlot; }
    public static void setCurrentSlot(Slot s){ currentSlot = s; }
    public static Entity getCurrentEntity(){ return currentEntity; }
    
}

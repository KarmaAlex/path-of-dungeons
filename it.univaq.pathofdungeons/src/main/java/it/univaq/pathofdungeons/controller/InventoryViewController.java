package it.univaq.pathofdungeons.controller;

import java.util.HashMap;

import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.domain.entity.EntityInventory;
import it.univaq.pathofdungeons.domain.entity.EntityStats;
import it.univaq.pathofdungeons.domain.items.EquipSlots;
import it.univaq.pathofdungeons.view.ViewDispatcher;
import it.univaq.pathofdungeons.view.ViewException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class InventoryViewController implements DataInitializable<Entity>{

    private static final String EQUIPPED_STYLE = "equipped"; 
    private static final String UNEQUIPPED_STYLE = "unequipped";
    private static final String GOLD_PREFIX = "Gold: ";
    private static final String GOLD_SUFFIX = "G";
    private static final Font STATS_FONT = new Font(15.0);

    @FXML
    GridPane inventoryGrid;
    @FXML
    Label helmetSlot;
    @FXML
    Label weaponSlot;
    @FXML
    Label chestpieceSlot;
    @FXML
    Label leftringSlot;
    @FXML
    Label rightringSlot;
    @FXML
    Label beltSlot;
    @FXML
    Label bootsSlot;
    @FXML
    Label glovesSlot;
    @FXML
    Button backBtn;
    @FXML
    Label gold;
    @FXML
    VBox statsPane;

    HashMap<EquipSlots, Label> inventory;

    @Override
    public void initialize(Entity data) {
        EntityInventory inv = data.getInventory();
        this.initializeMap();
        //Reset equipped status
        this.inventory.forEach((key, label) -> {
            label.getStyleClass().clear();
            label.getStyleClass().add(UNEQUIPPED_STYLE);
        });
        //Load equipment
        for(EquipSlots es: EquipSlots.values()){
            if(inv.getItemSlot(es) != null) {
                this.inventory.get(es).setText(inv.getItemSlot(es).getName());
                this.inventory.get(es).getStyleClass().clear();
                this.inventory.get(es).getStyleClass().add(EQUIPPED_STYLE);
            }
        }
        //Load gold
        this.gold.setText(GOLD_PREFIX + String.valueOf(inv.getGold()) + GOLD_SUFFIX);
        //Set up stats
        this.statsPane.getChildren().clear();
        for(EntityStats es: EntityStats.values()){
            Label l = new Label();
            l.fontProperty().set(STATS_FONT);
            l.setText(es.toString() + ": " + String.valueOf(data.getStat(es)));
            this.statsPane.getChildren().add(l);
        }
        backBtn.setOnAction((event)->{
            try{
                ViewDispatcher.getInstance().previousView();
            } catch(ViewException e){
                e.printStackTrace();
            }
            
        });
    }

    private void initializeMap(){
        this.inventory = new HashMap<>();
        this.inventory.put(EquipSlots.HELMET, helmetSlot);
        this.inventory.put(EquipSlots.BELT, beltSlot);
        this.inventory.put(EquipSlots.BOOTS, bootsSlot);
        this.inventory.put(EquipSlots.CHESTPIECE, chestpieceSlot);
        this.inventory.put(EquipSlots.GLOVES, glovesSlot);
        this.inventory.put(EquipSlots.LRING, leftringSlot);
        this.inventory.put(EquipSlots.RRING, rightringSlot);
        this.inventory.put(EquipSlots.WEAPON, weaponSlot);
    }
    
}

package it.univaq.pathofdungeons.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.univaq.pathofdungeons.domain.Battle;
import it.univaq.pathofdungeons.domain.Game;
import it.univaq.pathofdungeons.domain.dungeon.DungeonDirections;
import it.univaq.pathofdungeons.domain.dungeon.rooms.Room;
import it.univaq.pathofdungeons.domain.dungeon.rooms.RoomTypes;
import it.univaq.pathofdungeons.domain.dungeon.rooms.Shop;
import it.univaq.pathofdungeons.domain.entity.EntityStats;
import it.univaq.pathofdungeons.domain.entity.player.Player;
import it.univaq.pathofdungeons.domain.items.Item;
import it.univaq.pathofdungeons.game.impl.InventoryService;
import it.univaq.pathofdungeons.utils.FileLogger;
import it.univaq.pathofdungeons.view.ViewDispatcher;
import it.univaq.pathofdungeons.view.ViewException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
//TODO: update borders using new css from inventory view
/**
 * Class that represents the controller used in the main game view, the one with the dungeon map.
 * Handles moving in the dungeon, interacting within rooms and initiating battles.
 */
public class GameViewController implements DataInitializable<Game>{
    @FXML
    private HBox root, popupMain;
    @FXML
    private VBox playerName, playerHealth, playerMana, playerInventory;
    @FXML
    private GridPane dungeonMap, shopSlotGrid;
    @FXML
    private Button moveLeft, moveRight, moveUp, moveDown, interactBtn, saveBtn, popupConfirm, popupClose;
    @FXML
    private Label saveLabel, roomInfoLabel, popupTitle, shopErrorLabel;
    @FXML
    private ChoiceBox<Player> shopChoiceBox;
    @FXML
    private AnchorPane popup;

    private Game game;
    private Label[][] dungeonGrid;
    private LinkedList<EntityStatus> playerStatuses;
    private FileChooser fileChooser;
    private boolean kbMoveAllowed;
    private Map<Item, Integer> shopItems;

    private static ShopSlot currentShopSlot = null;

    @Override
    public void initialize(Game data){
        this.kbMoveAllowed = true;
        this.game = data;
        playerStatuses = new LinkedList<>();
        dungeonGrid = new Label[dungeonMap.getColumnCount()][dungeonMap.getRowCount()];
        for(int i = 0; i < dungeonGrid.length; i++){
            for(int j = 0; j < dungeonGrid[i].length; j++){
                dungeonGrid[i][j] = new Label();
                dungeonMap.add(dungeonGrid[i][j], i, j);
            }
        }
        renderDungeon();
        moveLeft.setOnAction(event-> this.move(DungeonDirections.LEFT));
        moveUp.setOnAction(event-> this.move(DungeonDirections.UP));
        moveDown.setOnAction(event-> this.move(DungeonDirections.DOWN));
        moveRight.setOnAction(event-> this.move(DungeonDirections.RIGHT));
        interactBtn.setOnAction(event-> this.onInteract());

        //Enable the inventory button in the status grid for each player
        for(int i = 0; i < this.game.getPlayers().size(); i++){
            Button b = (Button)playerInventory.getChildren().get(i);
            int j = i;
            b.setVisible(true);
            b.setOnAction(event->{
                try{
                    ViewDispatcher.getInstance().inventoryMenu(game.getPlayers().get(j));
                } catch(ViewException e){
                    FileLogger.getInstance().error(e.getMessage());
                }
            });
        }
        
        //Add the ability to move using arrow keys or WASD
        root.setOnKeyReleased(event->{
                    if(event.getCode() == KeyCode.A || event.getCode() == KeyCode.LEFT){ if(kbMoveAllowed) moveLeft.fire(); }
                    else if(event.getCode() == KeyCode.D || event.getCode() == KeyCode.RIGHT){ if(kbMoveAllowed) moveRight.fire(); }
                    else if(event.getCode() == KeyCode.W || event.getCode() == KeyCode.UP){ if(kbMoveAllowed) moveUp.fire(); }
                    else if(event.getCode() == KeyCode.S || event.getCode() == KeyCode.DOWN){ if(kbMoveAllowed) moveDown.fire(); }
                    else if(event.getCode() == KeyCode.SPACE || event.getCode() == KeyCode.E){ if(kbMoveAllowed) interactBtn.fire(); }
                });

        fileChooser = new FileChooser();
        //Create file chooser popup for saving when clicking the save button
        saveBtn.setOnAction(event->{
            fileChooser.setInitialFileName(String.format("%s.bin", DateFormat.getDateTimeInstance().format(new Date())));
            File file = fileChooser.showSaveDialog(saveBtn.getScene().getWindow());
            try(FileOutputStream out = new FileOutputStream(file)){
                ObjectOutputStream objos = new ObjectOutputStream(out);
                objos.writeObject(this.game);
                objos.close();
                FileLogger.getInstance().info("Saved game to file");
                saveLabel.setText("Saved");
            } catch(IOException e){
                System.err.println(e);
                FileLogger.getInstance().error("Failed to save game to file");
                saveLabel.setText("Failed to save file");
            }
        });

        //Setup popup button functionality
        this.popupClose.setOnAction(event-> {this.popup.setVisible(false); this.kbMoveAllowed = true;});
        this.popupConfirm.setOnAction(event->{
            if(currentShopSlot != null && this.shopChoiceBox.getValue() != null && this.shopItems != null){
                Player p = shopChoiceBox.getValue();
                if(p.getInventory().getGold() >= this.shopItems.get(currentShopSlot.getItem())){
                    InventoryService.removeGold(p, this.shopItems.get(currentShopSlot.getItem()));
                    this.shopItems.remove(currentShopSlot.getItem());
                    InventoryService.addItemToInventory(p, currentShopSlot.getItem());
                    currentShopSlot.removeItem();
                    this.shopErrorLabel.setText("Item bought");
                }
                else this.shopErrorLabel.setText("Not enough gold to purchase");
            }
        });
    }

    @Override
    public void postInitialize(){
        System.out.println("\nPost initialize\n");
        //Focus the root element so key bindings work, this seems to break sometimes though, unsure why
        root.requestFocus();
        this.updateStatus();
        this.roomInfoLabel.setText(this.game.getDungeon().getCurrentRoom().getInfo());
        System.out.println("\n" + this.game.getDungeon().getCurrentRoom().getRoomType() + "\n");
        if(this.game.getDungeon().getCurrentRoom().getRoomType().equals(RoomTypes.BOSS)){
            //We've just returned to the game view while currently in a boss room, meaning players have won the battle
            try{
                ViewDispatcher.getInstance().winScreen();
            } catch(ViewException e){
                FileLogger.getInstance().error(e.getMessage());
            }
        }
    }

    public void onInteract(){
        if(this.game.getDungeon().getCurrentRoom().getRoomType().equals(RoomTypes.SHOP)){
            //Show shop popup and populate with items
            this.popupTitle.setText("Shop");
            this.kbMoveAllowed = false;
            this.popup.setVisible(true);
            this.shopChoiceBox.getItems().addAll(this.game.getPlayers());
            this.shopChoiceBox.getSelectionModel().clearAndSelect(0);
            Shop shop = (Shop)this.game.getDungeon().getCurrentRoom();
            shopItems = shop.getShopItems();
            LinkedList<ShopSlot> shopSlots = new LinkedList<>();
            this.shopSlotGrid.getChildren().clear();
            int i = 0;
            for(Map.Entry<Item, Integer> e: shopItems.entrySet()){
                shopSlots.add(new ShopSlot(this.shopSlotGrid, e.getKey(), e.getValue(), i));
                i++;
            }
        }
        else this.game.getDungeon().getCurrentRoom().onInteract(this.game.getPlayers());
        this.roomInfoLabel.setText(this.game.getDungeon().getCurrentRoom().getInfo());
    }

    /**
     * Takes a grid representation of the rooms surrounding the current room and
     * uses it to replace objects in the grid with assets corresponding to the room type
     */
    private void renderDungeon(){
        //In case of a rectangular map ignore additional rows/columns and revert to a square
        int size = dungeonMap.getColumnCount() == dungeonMap.getRowCount() ? dungeonMap.getColumnCount() : Math.min(dungeonMap.getColumnCount(), dungeonMap.getRowCount());
        RoomTypes[][] typesMap = game.getDungeon().getDungeonService().getRoomGrid(size);
        for(int i = 0; i < typesMap.length; i++){
            for(int j = 0; j < typesMap[i].length; j++){
                if(typesMap[i][j] == null) {
                    dungeonGrid[i][j].setVisible(false);
                    continue;
                }
                switch(typesMap[i][j]){
                    case BASIC:
                        dungeonGrid[i][j].setVisible(true);
                        dungeonGrid[i][j].setText("[ ]");
                        break;
                    case SECRET:
                        dungeonGrid[i][j].setVisible(true);
                        dungeonGrid[i][j].setText("[S]");
                        break;
                    case SHOP:
                        dungeonGrid[i][j].setVisible(true);
                        dungeonGrid[i][j].setText("[$]");
                        break;
                    case UNEXPLORED:
                        dungeonGrid[i][j].setVisible(true);
                        dungeonGrid[i][j].setText("[?]");
                        break;
                    case NPC:
                        dungeonGrid[i][j].setVisible(true);
                        dungeonGrid[i][j].setText("[N]");
                        break;
                    case COMBAT:
                        dungeonGrid[i][j].setVisible(true);
                        dungeonGrid[i][j].setText("[C]");
                        break;
                    case BOSS:
                        dungeonGrid[i][j].setVisible(true);
                        dungeonGrid[i][j].setText("[B]");
                        break;
                    case CORRIDORH:
                        dungeonGrid[i][j].setVisible(true);
                        dungeonGrid[i][j].setText("=");
                        break;
                    case CORRIDORV:
                        dungeonGrid[i][j].setVisible(true);
                        dungeonGrid[i][j].setText("| |");
                        break;
                    case NONE:
                        dungeonGrid[i][j].setVisible(false);
                        break;
                }
            }
        }
    }

    /**
     * Shifts the grid of the dungeon by one tile in the given direction and renders it
     * @param dir DungeonDirections direction to move the dungeon grid
     */
    private void move(DungeonDirections dir){
        Room lastRoom = game.getDungeon().getCurrentRoom();
        if(!game.getDungeon().getDungeonService().movePlayers(dir) && 
           game.getDungeon().getCurrentRoom() != lastRoom && 
           (game.getDungeon().getCurrentRoom().getRoomType().equals(RoomTypes.COMBAT) || 
           game.getDungeon().getCurrentRoom().getRoomType().equals(RoomTypes.BOSS))){
            try{
                ViewDispatcher.getInstance().battleMenu(new Battle(game.getPlayers(), game.getDungeon().getCurrentRoom()));
            } catch(ViewException e){
                FileLogger.getInstance().error(e.getMessage());
            }
        }
        this.roomInfoLabel.setText(game.getDungeon().getCurrentRoom().getInfo());
        renderDungeon();
    }

    /**
     * Updates the status of players on the left side of the screen in the game view
     */
    private void updateStatus(){
        List<Player> players = game.getPlayers();
        if(players.size() < playerStatuses.size() || playerStatuses.isEmpty()){
            LinkedList<LinkedList<Node>> playerInfo = new LinkedList<>();
            playerInfo.add(new LinkedList<>(playerName.getChildren()));
            playerInfo.add(new LinkedList<>(playerHealth.getChildren()));
            playerInfo.add(new LinkedList<>(playerMana.getChildren()));
            playerStatuses = new LinkedList<>();
            for(int i = 0; i < players.size(); i++){
                Player player = players.get(i);
                playerStatuses.add(new EntityStatus((Label)playerInfo.get(0).get(i), (Label)playerInfo.get(1).get(i), (Label)playerInfo.get(2).get(i),
                player.getName(), player.getStat(EntityStats.HEALTH), player.getStat(EntityStats.MAXHEALTH), 
                player.getStat(EntityStats.MANA), player.getStat(EntityStats.MAXMANA)));
            }
        }
        else{
            for(int i = 0; i < players.size(); i++){
                playerStatuses.get(i).setHealth(players.get(i).getStat(EntityStats.HEALTH));
                playerStatuses.get(i).setMaxHealth(players.get(i).getStat(EntityStats.MAXHEALTH));
                playerStatuses.get(i).setMana(players.get(i).getStat(EntityStats.MANA));
                playerStatuses.get(i).setMaxMana(players.get(i).getStat(EntityStats.MAXMANA));
            }
        }
    }

    public static void setCurrentShopSlot(ShopSlot s){currentShopSlot = s;}
    
    public static ShopSlot getCurrentShopSlot(){ return currentShopSlot; }
}

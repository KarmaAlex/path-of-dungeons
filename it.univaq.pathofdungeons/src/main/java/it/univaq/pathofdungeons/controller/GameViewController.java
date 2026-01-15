package it.univaq.pathofdungeons.controller;

import java.util.LinkedList;

import it.univaq.pathofdungeons.domain.Battle;
import it.univaq.pathofdungeons.domain.Game;
import it.univaq.pathofdungeons.domain.dungeon.DungeonDirections;
import it.univaq.pathofdungeons.domain.dungeon.rooms.Room;
import it.univaq.pathofdungeons.domain.dungeon.rooms.RoomTypes;
import it.univaq.pathofdungeons.domain.entity.EntityStats;
import it.univaq.pathofdungeons.domain.entity.player.Player;
import it.univaq.pathofdungeons.view.ViewDispatcher;
import it.univaq.pathofdungeons.view.ViewException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
//TODO: update stats when switching back from battle
//TODO: update borders using new css from inventory view
public class GameViewController implements DataInitializable<Game>{
    @FXML
    HBox root;
    @FXML
    VBox playerName;
    @FXML
    VBox playerHealth;
    @FXML
    VBox playerMana;
    @FXML
    GridPane dungeonMap;
    @FXML
    Button moveLeft;
    @FXML
    Button moveRight;
    @FXML
    Button moveUp;
    @FXML
    Button moveDown;
    @FXML
    Button interactBtn;
    @FXML
    VBox playerInventory;

    private Game game;
    private Label[][] dungeonGrid;
    LinkedList<EntityStatus> playerStatuses;

    @Override
    public void initialize(Game data){
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
        moveLeft.setOnAction((event)->{ this.move(DungeonDirections.LEFT); });
        moveUp.setOnAction((event)->{ this.move(DungeonDirections.UP); });
        moveDown.setOnAction((event)->{ this.move(DungeonDirections.DOWN); });
        moveRight.setOnAction((event)->{ this.move(DungeonDirections.RIGHT); });
        interactBtn.setOnAction((event)->{ game.getDungeon().getCurrentRoom().onInteract(game.getPlayers()); });

        for(int i = 0; i < this.game.getPlayers().size(); i++){
            Button b = (Button)playerInventory.getChildren().get(i);
            int j = i;
            b.setVisible(true);
            b.setOnAction((event)->{
                try{
                    /* System.out.println(game.getPlayers().get(j));
                    System.out.println(game.getPlayers().get(j).getInventory()); */
                    ViewDispatcher.getInstance().inventoryMenu(game.getPlayers().get(j));
                } catch(ViewException e){
                    e.printStackTrace();
                }
            });
        }

        root.setOnKeyReleased((event)->{
                    if(event.getCode() == KeyCode.A || event.getCode() == KeyCode.LEFT){ moveLeft.fire(); }
                    else if(event.getCode() == KeyCode.D || event.getCode() == KeyCode.RIGHT){ moveRight.fire(); }
                    else if(event.getCode() == KeyCode.W || event.getCode() == KeyCode.UP){ moveUp.fire(); }
                    else if(event.getCode() == KeyCode.S || event.getCode() == KeyCode.DOWN){ moveDown.fire(); }
                    else if(event.getCode() == KeyCode.SPACE || event.getCode() == KeyCode.E){ interactBtn.fire(); }
                });
    }

    public void postInitialize(){
        root.requestFocus();
        this.updateStatus();
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
                        dungeonGrid[i][j].setText("[B]");
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

    private void move(DungeonDirections dir){
        Room lastRoom = game.getDungeon().getCurrentRoom();
        if(!game.getDungeon().getDungeonService().movePlayers(dir) && game.getDungeon().getCurrentRoom() != lastRoom && game.getDungeon().getCurrentRoom().getRoomType() == RoomTypes.COMBAT){
            try{
                ViewDispatcher.getInstance().battleMenu(new Battle(game.getPlayers(), game.getDungeon().getCurrentRoom()));
            } catch(ViewException e){
                e.printStackTrace();
            }
        }
        renderDungeon();
    }

    private void updateStatus(){
        LinkedList<Player> players = game.getPlayers();
        if(players.size() < playerStatuses.size() || playerStatuses.size() == 0){
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
            playerInfo = null;
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
    
}

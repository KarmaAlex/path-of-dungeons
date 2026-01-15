package it.univaq.pathofdungeons.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import it.univaq.pathofdungeons.domain.Game;
import it.univaq.pathofdungeons.domain.dungeon.Dungeon;
import it.univaq.pathofdungeons.domain.dungeon.DungeonSizes;
import it.univaq.pathofdungeons.domain.entity.player.Player;
import it.univaq.pathofdungeons.domain.entity.player.PlayerClasses;
import it.univaq.pathofdungeons.game.EntityStatsNotFoundException;
import it.univaq.pathofdungeons.game.impl.DungeonGenerator;
import it.univaq.pathofdungeons.game.impl.EntityFactory;
import it.univaq.pathofdungeons.view.ViewDispatcher;
import it.univaq.pathofdungeons.view.ViewException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MainMenuController implements DataInitializable<Object>{
    private static final int MAX_PLAYERS = 4;

    @FXML
    private ChoiceBox<Integer> sizeInput;

    @FXML
    private ChoiceBox<Integer> playersInput;

    @FXML
    private Label playerLabel1;
    @FXML
    private Label playerLabel2;
    @FXML
    private Label playerLabel3;
    @FXML
    private Label playerLabel4;

    @FXML
    private TextField playerName1;
    @FXML
    private TextField playerName2;
    @FXML
    private TextField playerName3;
    @FXML
    private TextField playerName4;

    @FXML
    private ChoiceBox<String> classInput1;
    @FXML
    private ChoiceBox<String> classInput2;
    @FXML
    private ChoiceBox<String> classInput3;
    @FXML
    private ChoiceBox<String> classInput4;

    @FXML
    private Button submitBtn;
    @FXML
    private Button playBtn;

    private ArrayList<Label> labels;
    private ArrayList<TextField> names;
    private ArrayList<ChoiceBox<String>> classes;
    private int dungeonSize;
    private int numPlayers;
    
    /*
     * As noted in the javafx documentation implementing the Initializable interface is deprecated,
     * the preferred method is assigning the controller in the FXML file and defining the initialize method
     * with the correct annotation
     */
    @FXML
    public void initialize(){
        labels = new ArrayList<>(Arrays.asList(playerLabel1, playerLabel2, playerLabel3, playerLabel4));
        names = new ArrayList<>(Arrays.asList(playerName1, playerName2, playerName3, playerName4));
        classes = new ArrayList<>(Arrays.asList(classInput1, classInput2, classInput3, classInput4));
        for(DungeonSizes ds: DungeonSizes.values()){
            sizeInput.getItems().add(ds.size());
        }
        for(int i = 0; i < MAX_PLAYERS; i++){
            playersInput.getItems().add(i+1);
        }
        for(ChoiceBox<String> c: classes){
            for(PlayerClasses p: PlayerClasses.values()){
                c.getItems().add(p.toString());
            }
        }
        submitBtn.disableProperty().bind(sizeInput.valueProperty().isNull().or(playersInput.valueProperty().isNull()));

        submitBtn.setOnAction((ActionEvent event) -> {
            dungeonSize = sizeInput.getValue();
            numPlayers = playersInput.getValue();
            for(int i = 0; i < numPlayers; i++){
                labels.get(i).setVisible(true);
                names.get(i).setVisible(true);
                classes.get(i).setVisible(true);
            }
            playBtn.setVisible(true);
            // TODO disable the submit button after it has been clicked once
        });

        playBtn.setOnAction((ActionEvent event) -> {
            LinkedList<Player> players = new LinkedList<>();
            //Check that all names and classes have been input before proceding
            for(int i = 0; i < numPlayers; i++){if((names.get(i).isVisible() && names.get(i).getText() == null) || classes.get(i).valueProperty() == null) return;}
            for(int i = 0; i < numPlayers; i++){
                try{
                        players.add(EntityFactory.getPlayer(PlayerClasses.valueOf(classes.get(i).valueProperty().get().toUpperCase())));
                        players.getLast().setName(names.get(i).getText());
                    } catch(EntityStatsNotFoundException e){
                        //TODO add error label for missing stats
                        System.out.println(e.getMessage());
                        return;
                    }
            }
            Dungeon dungeon = DungeonGenerator.createDungeon(dungeonSize);
            Game game = new Game(players, dungeon);
            try{
                ViewDispatcher.getInstance().gameView(game);
            } catch(ViewException e){
                e.printStackTrace();
            }
            
        });
        //TODO: find a boolean expression that disables the play button untill all selected players have input a name and selected a class
    }

    @Override
    public void initialize(Object o){};
}

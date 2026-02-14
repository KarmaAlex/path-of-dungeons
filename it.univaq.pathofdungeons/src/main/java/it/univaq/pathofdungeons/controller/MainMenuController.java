package it.univaq.pathofdungeons.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import it.univaq.pathofdungeons.domain.Game;
import it.univaq.pathofdungeons.domain.dungeon.Dungeon;
import it.univaq.pathofdungeons.domain.dungeon.DungeonSizes;
import it.univaq.pathofdungeons.domain.entity.player.Player;
import it.univaq.pathofdungeons.domain.entity.player.PlayerClasses;
import it.univaq.pathofdungeons.game.EntityStatsNotFoundException;
import it.univaq.pathofdungeons.game.impl.DungeonGenerator;
import it.univaq.pathofdungeons.game.impl.EntityFactory;
import it.univaq.pathofdungeons.utils.FileLogger;
import it.univaq.pathofdungeons.view.ViewDispatcher;
import it.univaq.pathofdungeons.view.ViewException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * Class that handles control for the main menu, it handles loading save files and creating
 * new games with the given player count and dungeon size 
 */
public class MainMenuController implements DataInitializable<List<String>>{
    private static final int MAX_PLAYERS = 4;

    @FXML
    private ChoiceBox<DungeonSizes> sizeInput;
    @FXML
    private ChoiceBox<Integer> playersInput;
    @FXML
    private Label playerLabel1, playerLabel2, playerLabel3, playerLabel4, selectedFileLabel, loadErrorLabel;
    @FXML
    private TextField playerName1, playerName2, playerName3, playerName4;
    @FXML
    private ChoiceBox<PlayerClasses> classInput1, classInput2, classInput3, classInput4;
    @FXML
    private Button submitBtn, playBtn, selectFileBtn, loadBtn;

    private ArrayList<Label> labels;
    private ArrayList<TextField> names;
    private ArrayList<ChoiceBox<PlayerClasses>> classes;
    private DungeonSizes dungeonSize;
    private int numPlayers;
    private FileChooser fileChooser;
    private File loadedFile;
    
    /*
     * As noted in the javafx documentation implementing the Initializable interface is deprecated,
     * the preferred method is assigning the controller in the FXML file and defining the initialize method
     * with the correct annotation, or in this case calling this methond when switching scene
     */
    @Override
    public void initialize(){
        labels = new ArrayList<>(Arrays.asList(playerLabel1, playerLabel2, playerLabel3, playerLabel4));
        names = new ArrayList<>(Arrays.asList(playerName1, playerName2, playerName3, playerName4));
        classes = new ArrayList<>(Arrays.asList(classInput1, classInput2, classInput3, classInput4));
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Save files", "*.bin"));
        loadBtn.disableProperty().bind(selectedFileLabel.textProperty().isEqualTo(""));

        selectFileBtn.setOnAction(event->{
            File f = fileChooser.showOpenDialog(loadBtn.getScene().getWindow());
            if(f!=null){
                loadedFile = f;
                selectedFileLabel.setText(loadedFile.getName());
            }
            else{
                selectedFileLabel.setText("");
            }
        });

        loadBtn.setOnAction(event->{
            if(loadedFile != null){
                try(FileInputStream fis = new FileInputStream(loadedFile)){
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    Game game = (Game)ois.readObject();
                    try{
                        ViewDispatcher.getInstance().gameView(game);
                    } catch(ViewException e){
                        FileLogger.getInstance().error(e.getMessage());
                    }
                } catch(IOException e){
                    System.err.println(e);
                    FileLogger.getInstance().error("Could not load save from file");
                    loadErrorLabel.setText("Failed to load file");
                } catch(ClassNotFoundException e){
                    System.err.println(e);
                    FileLogger.getInstance().error("Save file is not valid");
                    loadErrorLabel.setText("File is not valid");
                }
            }
        });
        sizeInput.getItems().addAll(DungeonSizes.values());
        for(int i = 0; i < MAX_PLAYERS; i++){
            playersInput.getItems().add(i+1);
        }
        for(ChoiceBox<PlayerClasses> c: classes){
            c.getItems().addAll(PlayerClasses.values());
        }
        submitBtn.disableProperty().bind(sizeInput.valueProperty().isNull().or(playersInput.valueProperty().isNull()));

        submitBtn.setOnAction((ActionEvent event) -> {
            this.dungeonSize = sizeInput.getValue();
            this.numPlayers = playersInput.getValue();
            for(int i = 0; i < numPlayers; i++){
                labels.get(i).setVisible(true);
                names.get(i).setVisible(true);
                classes.get(i).setVisible(true);
            }
            playBtn.setVisible(true);
        });

        playBtn.setOnAction(event -> {
            LinkedList<Player> players = new LinkedList<>();
            //Check that all names and classes have been input before proceding
            for(int i = 0; i < numPlayers; i++){if((names.get(i).isVisible() && names.get(i).getText() == null) || classes.get(i).valueProperty() == null) return;}
            for(int i = 0; i < numPlayers; i++){
                try{
                        players.add(EntityFactory.getPlayer(classes.get(i).getValue(), names.get(i).getText()));
                    } catch(EntityStatsNotFoundException e){
                        //TODO add error label for missing stats
                        FileLogger.getInstance().error(e.getMessage());
                        return;
                    }
            }
            Dungeon dungeon = DungeonGenerator.createDungeon(dungeonSize.size());
            Game game = new Game(players, dungeon);
            try{
                ViewDispatcher.getInstance().gameView(game);
            } catch(ViewException e){
                FileLogger.getInstance().error(e.getMessage());
            }
            
        });
        //TODO: find a boolean expression that disables the play button untill all selected players have input a name and selected a class
    }

    @Override
    public void initialize(List<String> params){
        LinkedList<Player> players = new LinkedList<>();
        for(int i = 0; i < Integer.parseInt(params.get(0)); i++){
            try{
                players.add(EntityFactory.getPlayer(PlayerClasses.valueOf(params.get(1).toUpperCase()), params.get(2)));
            } catch(EntityStatsNotFoundException e){
                FileLogger.getInstance().error(e.getMessage());
                return;
            }    
        }
        Dungeon dungeon = DungeonGenerator.createDungeon(Integer.parseInt(params.get(3)));
        Game game = new Game(players, dungeon);
        try{
            ViewDispatcher.getInstance().gameView(game);
        } catch(ViewException e){
            FileLogger.getInstance().error(e.getMessage());
        }
    }
}

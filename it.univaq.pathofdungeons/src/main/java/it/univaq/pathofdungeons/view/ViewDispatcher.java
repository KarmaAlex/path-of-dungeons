package it.univaq.pathofdungeons.view;

import java.io.IOException;
import java.util.List;

import it.univaq.pathofdungeons.controller.DataInitializable;
import it.univaq.pathofdungeons.domain.Battle;
import it.univaq.pathofdungeons.domain.Game;
import it.univaq.pathofdungeons.domain.entity.Entity;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ViewDispatcher {

    private static final String BASE = "/views/";
    private static final String SUFFIX = ".fxml";
    private static final String MAIN_MENU = "mainMenu";
    private static final String GAME_VIEW = "gameView";
    private static final String BATTLE_VIEW = "battleView";
    private static final String INVENTORY_VIEW = "inventoryView";
    private static final String WIN_SCREEN = "winScreen";
    private static final String LOSE_SCREEN = "loseScreen";

    private Stage stage;
    private static ViewDispatcher viewDispatcher = new ViewDispatcher();
    private Parent lastView;
    private DataInitializable<?> lastController;
    private DataInitializable<?> currentController;

    private ViewDispatcher(){}

    public static ViewDispatcher getInstance(){ return viewDispatcher; }

    /**
     * Switches to the main menu scene
     * @param stage stage to load the scene into
     * @throws ViewException when the .fxml file for the view fails to load
     */
    public void mainMenu(Stage stage) throws ViewException{
        this.stage = stage;
        Parent mainMenu = loadView(MAIN_MENU).getParent();
        Scene scene = new Scene(mainMenu);
        this.stage.setScene(scene);
        this.stage.show();
    }

    /**
     * Switches to the main menu scene and passes the given command line arguments
     * @param stage stage to load the scene into
     * @param args list of command line arguments
     * @throws ViewException when the .fxml file for the view fails to load
     */
    public void mainMenu(Stage stage, List<String> args) throws ViewException{
        this.stage = stage;
        View<Object> view = loadView(MAIN_MENU);
        Parent mainmenu = view.getParent();
        Scene scene = new Scene(mainmenu);
        this.stage.setScene(scene);
        this.stage.show();
        view.getController().initialize(args);
    }

    /**
     * Switches to the main game view that has dungeon map and quick player overview
     * @param game game data used to populate the view
     * @throws ViewException when the .fxml file for the view fails to load
     */
    public void gameView(Game game) throws ViewException{
        View<Game> view = loadView(GAME_VIEW);
        view.getController().initialize(game);
        Parent gameMenu = view.getParent();
        Scene scene = new Scene(gameMenu);
        this.stage.setScene(scene);
        this.stage.show();
        view.getController().postInitialize();
        this.currentController = view.getController();
    }

    /**
     * Switches to the battle view and saves the last view so it can be returned to
     * @param battle battle data used to populate the view
     * @throws ViewException when the .fxml file for the view fails to load
     */
    public void battleMenu(Battle battle) throws ViewException{ changeViewReturnable(battle, BATTLE_VIEW); }

    /**
     * Switches to the inventory view and saves the last view so it can be returned to
     * @param e entity to load the inventory of
     * @throws ViewException when the .fxml file for the view fails to load
     */
    public void inventoryMenu(Entity e) throws ViewException{ changeViewReturnable(e, INVENTORY_VIEW); }

    /**
     * Returns to the previously saved view and runs the postInitialize method
     */
    public void previousView(){
        this.stage.getScene().setRoot(this.lastView);
        DataInitializable<?> c = this.currentController;
        this.currentController = this.lastController;
        this.lastController = c;
        this.currentController.postInitialize();
    }

    /**
     * Go to game over screen
     * @throws ViewException when the .fxml file for the view fails to load
     */
    public void gameOver() throws ViewException{
        this.stage.setScene(new Scene(loadView(LOSE_SCREEN).getParent()));
        this.stage.show();
    }

    /**
     * Go to win screen
     * @throws ViewException when the .fxml file for the view fails to load
     */
    public void winScreen() throws ViewException{
        this.stage.setScene(new Scene(loadView(WIN_SCREEN).getParent()));
        this.stage.show();
    }
    
    /**
     * Changes view and saves the previous view so it can be returned to
     * @param <T> any data type congruent with the view's required data
     * @param data data to be passed to the new view
     * @param viewName name of the view to be loaded
     * @throws ViewException when the .fxml file for the view fails to load
     */
    private <T> void changeViewReturnable(T data, String viewName) throws ViewException{
        View<T> view = loadView(viewName);
        view.getController().initialize(data);
        Parent curView = view.getParent();
        this.lastView = this.stage.getScene().getRoot();
        System.out.println(lastController);
        System.out.println(currentController);
        this.lastController = this.currentController;
        this.currentController = view.getController();
        System.out.println(lastController);
        System.out.println(currentController);
        this.stage.getScene().setRoot(curView);
        view.getController().postInitialize();
    }

    /**
     * Internal method used to load a view using FXMLLoader
     * @param <T> type of the data used in the view initialization
     * @param viewName filename of the view to be loaded
     * @return an instance of the new view
     * @throws ViewException when the .fxml file for the view fails to load
     */
    private <T> View<T> loadView(String viewName) throws ViewException {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(BASE + viewName + SUFFIX));
			Parent parent = (Parent) loader.load();
			return new View<>(parent, loader.getController());

		} catch (IOException ex) {
			throw new ViewException(ex);
		}
	}
}

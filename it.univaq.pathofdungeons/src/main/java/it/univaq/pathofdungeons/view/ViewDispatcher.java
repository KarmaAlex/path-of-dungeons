package it.univaq.pathofdungeons.view;

import java.io.IOException;

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

    private Stage stage;
    private static ViewDispatcher viewDispatcher = new ViewDispatcher();
    private Parent lastView;
    private DataInitializable<?> lastController;

    private ViewDispatcher(){}

    public static ViewDispatcher getInstance(){ return viewDispatcher; }

    public void mainMenu(Stage stage) throws ViewException{
        this.stage = stage;
        Parent mainMenu = loadView(MAIN_MENU).getView();
        Scene scene = new Scene(mainMenu);
        this.stage.setScene(scene);
        this.stage.show();
    }

    public void gameView(Game game) throws ViewException{
        View<Game> view = loadView(GAME_VIEW);
        view.getController().initialize(game);
        Parent gameMenu = view.getView();
        Scene scene = new Scene(gameMenu);
        this.stage.setScene(scene);
        this.stage.show();
        view.getController().postInitialize();
    }

    public void battleMenu(Battle battle) throws ViewException{ changeViewReturnable(battle, BATTLE_VIEW); }

    public void inventoryMenu(Entity e) throws ViewException{ changeViewReturnable(e, INVENTORY_VIEW); }

    public void previousView() throws ViewException{
        this.stage.getScene().setRoot(this.lastView);
        this.lastController.postInitialize();
    }
    
    private <T> void changeViewReturnable(T data, String viewName) throws ViewException{
        View<T> view = loadView(viewName);
        view.getController().initialize(data);
        Parent curView = view.getView();
        this.lastView = this.stage.getScene().getRoot();
        this.lastController = view.getController();
        this.stage.getScene().setRoot(curView);
        view.getController().postInitialize();
    }

    private <T> View<T> loadView(String viewName) throws ViewException {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(BASE + viewName + SUFFIX));
			Parent parent = (Parent) loader.load();
			return new View<T>(parent, loader.getController());

		} catch (IOException ex) {
			throw new ViewException(ex);
		}
	}
}

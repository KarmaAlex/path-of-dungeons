package it.univaq.pathofdungeons;

import it.univaq.pathofdungeons.view.ViewDispatcher;
import it.univaq.pathofdungeons.view.ViewException;
import javafx.application.Application;
import javafx.stage.Stage;

public class Runner extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage){
		try {
			ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();
			viewDispatcher.mainMenu(stage);
		} catch (ViewException e){
			e.printStackTrace();
		}
	}
}

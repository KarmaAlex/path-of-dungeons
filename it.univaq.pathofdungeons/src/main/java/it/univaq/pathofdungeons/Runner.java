package it.univaq.pathofdungeons;

import it.univaq.pathofdungeons.utils.FileLogger;
import it.univaq.pathofdungeons.view.ViewDispatcher;
import it.univaq.pathofdungeons.view.ViewException;
import javafx.application.Application;
import javafx.stage.Stage;

public class Runner extends Application{
	/*TODO:
		add filelogger file management
		visuals for current effects
		add the ability to use items from the inventory outside battles
		edit stats for enemies and players
	 */

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage){
		try {
			ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();
			if(!(this.getParameters().getRaw().isEmpty()) && this.getParameters().getRaw().size() < 5){
				viewDispatcher.mainMenu(stage, this.getParameters().getRaw());
			}
			else {
				viewDispatcher.mainMenu(stage);
			}
		} catch (ViewException e){
			FileLogger.getInstance().error(e.getMessage());
		}
	}
}

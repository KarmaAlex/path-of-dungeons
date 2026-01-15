package it.univaq.pathofdungeons.view;

import it.univaq.pathofdungeons.controller.DataInitializable;
import javafx.scene.Parent;

public class View<T> {
    private Parent view;
    private DataInitializable<T> mainMenuController;

    public View(Parent view, DataInitializable<T> mmc){
        this.view = view;
        this.mainMenuController = mmc;
    }

    public Parent getView(){ return view; }
    public void setView(Parent view){ this.view = view; }
    public DataInitializable<T> getController() { return mainMenuController; }
	public void setController(DataInitializable<T> controller) { this.mainMenuController = controller; }
}

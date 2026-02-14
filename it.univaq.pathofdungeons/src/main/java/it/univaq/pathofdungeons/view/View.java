package it.univaq.pathofdungeons.view;

import it.univaq.pathofdungeons.controller.DataInitializable;
import javafx.scene.Parent;

/**
 * Class that represents a view with its associated controller
 */
public class View<T> {
    private Parent parent;
    private DataInitializable<T> controller;

    public View(Parent view, DataInitializable<T> c){
        this.parent = view;
        this.controller = c;
    }

    public Parent getParent(){ return parent; }
    public void setParent(Parent view){ this.parent = view; }
    public DataInitializable<T> getController() { return controller; }
	public void setController(DataInitializable<T> controller) { this.controller = controller; }
}

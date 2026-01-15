package it.univaq.pathofdungeons.controller;

import it.univaq.pathofdungeons.domain.entity.Entity;
import javafx.scene.control.Button;

public class SelectableEntity {
    private Button button;
    private Entity entity;

    public SelectableEntity(Button b, Entity e){
        this.button = b;
        this.entity = e;
        this.button.setDisable(true);
    }

    public Entity getEntity(){ return this.entity; }
    public Button getButton(){ return this.button; }
}

package it.univaq.pathofdungeons.controller;

import it.univaq.pathofdungeons.domain.items.Item;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * Class that represents a slot in the shop interface
 */
public class ShopSlot {
    private GridPane parent;
    private VBox box;
    private Item item;

    public ShopSlot(GridPane parent, Item item, int price, int pos){
        this.item = item;
        this.parent = parent;
        //Create VBox that contains item and price label
        this.box = new VBox();
        this.box.setAlignment(Pos.TOP_CENTER);
        //Create VBox that contains item
        VBox innerBox = new VBox();
        innerBox.setAlignment(Pos.CENTER);
        Label itemLabel = new Label(item.getName());
        innerBox.getChildren().add(itemLabel);
        this.box.getChildren().add(innerBox);
        Label priceLabel = new Label(String.valueOf(price)+"G");
        this.box.getChildren().add(priceLabel);
        VBox.setMargin(priceLabel, new Insets(10.0, 0, 0, 0));
        VBox.setMargin(itemLabel, new Insets(10.0, 0, 0 , 0));
        this.parent.getChildren().add(this.box);
        GridPane.setConstraints(this.box, pos, 0);

        this.box.setOnMouseClicked(event->{
            if(event.getButton().equals(MouseButton.PRIMARY)){
                if(GameViewController.getCurrentShopSlot() != null){
                    GameViewController.getCurrentShopSlot().toggleSelected();
                }
                GameViewController.setCurrentShopSlot(this);
                this.toggleSelected();
            }
        });
    }

    public void toggleSelected(){
        if(!this.box.getStyle().equals("")){
            this.box.setStyle("");
        }
        else{
            this.box.setStyle("-fx-background-color: blue");
        }
    }

    public Item getItem(){return this.item;}

    public void removeItem(){ this.parent.getChildren().remove(this.box); }
}

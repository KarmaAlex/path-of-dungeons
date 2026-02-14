package it.univaq.pathofdungeons.controller;

import it.univaq.pathofdungeons.domain.items.ItemSlot;
import it.univaq.pathofdungeons.game.impl.InventoryService;
import javafx.scene.input.DataFormat;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;

/**
 * Class that represents the trash slot in the inventory screen
 */
public class TrashSlot {
    private StackPane container;
    private ItemSlot slot;

    public TrashSlot(StackPane sp){
        this.container = sp;
        slot = new ItemSlot();

        this.container.setOnDragEntered(event-> this.container.setStyle("-fx-background-color:blue"));
        this.container.setOnDragExited(event-> this.container.setStyle(""));
        this.container.setOnDragOver(event->{
            if(event.getGestureSource().equals(this.container)) return;
            event.acceptTransferModes(TransferMode.ANY);
        });
        //Swap the item and immediately delete the recieved item
        this.container.setOnDragDropped(event->{
            if(event.getDragboard().hasContent(DataFormat.PLAIN_TEXT)){
                event.setDropCompleted(InventoryService.swapSlots(InventoryViewController.getCurrentEntity(), InventoryViewController.getCurrentSlot(), this.slot));
            }
            this.slot.setItem(null);
            InventoryViewController.updateStats();
        });
    }
}

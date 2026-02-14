package it.univaq.pathofdungeons.controller;

import it.univaq.pathofdungeons.domain.items.Item;
import it.univaq.pathofdungeons.domain.items.Slot;
import it.univaq.pathofdungeons.game.impl.InventoryService;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;

/**
 * Class that represents an inventory or equippable slot in the inventory interface
 */
public class InventorySlot {
    private static final String EQUIPPED_STYLE = "equipped"; 
    private static final String UNEQUIPPED_STYLE = "unequipped";
    private static final String DUMMY = "dummy";
    private static final ClipboardContent dummyCc = new ClipboardContent();
    static{
        dummyCc.putString(DUMMY);
    }

    private StackPane container;
    private Slot slot;
    private final String initalValue;
    
    public InventorySlot(StackPane pane, Slot slot){
        this.container = pane;
        this.slot = slot;
        this.initalValue = ((Label)this.container.getChildren().getFirst()).getText();
        this.updateLabel();
        
        this.container.setOnDragDetected(event -> {
            if(this.getItem() != null){
                //Simple way of doing a drag and drop inventory, use dummy data to initiate drag and drop and swap using a static field
                Dragboard db = this.container.startDragAndDrop(TransferMode.ANY);
                db.setDragView(this.container.snapshot(new SnapshotParameters(), new WritableImage((int)(this.container.widthProperty().get()), (int)this.container.heightProperty().get())));
                InventoryViewController.setCurrentSlot(this.slot);
                db.setContent(dummyCc);
            }
        });
        //Set up the ability to drag items from the slot as long as one is present
        this.container.setOnDragEntered(event->{
            if(event.getGestureSource().equals(this.container)) return;
            Slot s = event.getDragboard().hasContent(DataFormat.PLAIN_TEXT) ? InventoryViewController.getCurrentSlot() : null;
            Item i = s != null ? s.getItem() : null;
            if(i == null) return;
            if(this.getItem() != null && i != null && InventoryService.canSwap(s, this.slot)) this.container.setStyle("-fx-background-color:green");
            else if(i != null && this.slot.isItemValid(i)) this.container.setStyle("-fx-background-color:blue");
        });
        this.container.setOnDragExited(event-> this.container.setStyle(""));
        this.container.setOnDragOver(event->{
            if(event.getGestureSource().equals(this.container)) return;
            event.acceptTransferModes(TransferMode.ANY);
        });
        //When an item is dropped on a different slot swap them with the source slot if it is a valid option
        this.container.setOnDragDropped(event->{
            Slot s = event.getDragboard().hasContent(DataFormat.PLAIN_TEXT) ? InventoryViewController.getCurrentSlot() : null;
            if(s == null) return;
            event.setDropCompleted(InventoryService.swapSlots(InventoryViewController.getCurrentEntity(), InventoryViewController.getCurrentSlot(), this.slot));
            this.updateLabel();
            InventoryViewController.updateStats();
        });
        //Update this object's label when an item is dropped on it
        this.container.setOnDragDone(event-> this.updateLabel());
    }

    public Item getItem(){ return this.slot.getItem(); }
    public Slot getSlot(){ return this.slot; }
    public StackPane getContainer(){ return this.container; }

    private void updateLabel(){
        Label label = (Label)this.container.getChildren().getFirst();
        label.setTooltip(null);
        label.setText(initalValue);
        label.getStyleClass().clear();
        label.getStyleClass().add(UNEQUIPPED_STYLE);
        if(this.getItem() != null){
            label.setTooltip(new Tooltip(this.getItem().toString()));
            label.setText(this.getItem().getName());
            label.getStyleClass().clear();
            label.getStyleClass().add(EQUIPPED_STYLE);
        }
    }
}

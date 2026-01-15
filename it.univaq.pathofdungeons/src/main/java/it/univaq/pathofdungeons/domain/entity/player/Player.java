package it.univaq.pathofdungeons.domain.entity.player;

import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.game.EntityService;

public class Player extends Entity {
    private boolean savingThrow;
    private PlayerClasses pClass;

    public Player(EntityService characterService, PlayerClasses pClass){
        super(characterService);
        this.savingThrow = true;
        this.pClass = pClass;
    }

    public boolean hasSavingThrow(){ return savingThrow; }

    public void resetSavingThrow(){ savingThrow = true; }

    public PlayerClasses getPClass(){ return this.pClass; }
}

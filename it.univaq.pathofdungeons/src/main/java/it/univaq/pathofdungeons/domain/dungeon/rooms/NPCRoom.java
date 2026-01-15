package it.univaq.pathofdungeons.domain.dungeon.rooms;

import java.util.LinkedList;

import it.univaq.pathofdungeons.domain.entity.player.Player;

public class NPCRoom extends Room{

    public NPCRoom(){
        super(RoomTypes.NPC);
    }

    @Override
    public void onEnter(){
        this.setExplored(true);
    }

    @Override
    public void onInteract(LinkedList<Player> players){
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onInteract'");
    }

}

package it.univaq.pathofdungeons.game.impl;

import it.univaq.pathofdungeons.domain.Loadout;
import it.univaq.pathofdungeons.domain.entity.Entity;
import it.univaq.pathofdungeons.domain.entity.EntityInventory;
import it.univaq.pathofdungeons.domain.items.Item;
import it.univaq.pathofdungeons.domain.items.equippable.Equippable;
import it.univaq.pathofdungeons.game.InventoryService;

public class InventoryServiceImpl implements InventoryService{

    private static InventoryServiceImpl inst = new InventoryServiceImpl();;

    private InventoryServiceImpl(){};

    public static InventoryServiceImpl getInstance(){ return inst; }

    public boolean loadLoadout(Loadout l, Entity t){
        EntityInventory ei = t.getInventory();
        for(Equippable e: l.getAllSlots()){
            if(e != null){
                ei.equipItem(e);
                this.updateStatsEquip(e, t);
            }
        }
        return true;
    }

    @Override
    public boolean equipItem(Equippable e, Entity t) {
        EntityInventory inv = t.getInventory();
        if(inv.getItemSlot(e.getSlot()) != null){
            if(inv.isEquipSlotsFull()) return false;
            this.updateStatsUnequip(inv.equipItem(e), t);
            this.updateStatsEquip(e, t);
            return true;
        }
        inv.equipItem(e);
        this.updateStatsEquip(e, t);
        return true;
    }

    @Override
    public boolean addItem(Item i, Entity t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addItem'");
    }

    private void updateStatsEquip(Equippable e, Entity t){
        e.getStats().forEach((stat, amount)->{
            t.updateStat(stat, amount);
        });
        if(e.getEffect() != null) t.addOnHitEffect(EffectFactory.getEffect(e.getEffect()));
    }
    
    private void updateStatsUnequip(Equippable e, Entity t){
        e.getStats().forEach((stat, amount)->{
            t.updateStat(stat, -amount);
        });
        if(e.getEffect() != null) t.removeOnHitEffect(e.getEffect());
    }

}

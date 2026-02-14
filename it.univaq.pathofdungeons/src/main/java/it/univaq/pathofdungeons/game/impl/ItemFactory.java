package it.univaq.pathofdungeons.game.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.alibaba.fastjson2.JSON;

import it.univaq.pathofdungeons.domain.Loadout;
import it.univaq.pathofdungeons.domain.effects.Debuffs;
import it.univaq.pathofdungeons.domain.effects.Effects;
import it.univaq.pathofdungeons.domain.entity.EntityStats;
import it.univaq.pathofdungeons.domain.entity.enemies.Enemy;
import it.univaq.pathofdungeons.domain.entity.player.PlayerClasses;
import it.univaq.pathofdungeons.domain.items.Consumable;
import it.univaq.pathofdungeons.domain.items.EquipSlots;
import it.univaq.pathofdungeons.domain.items.EquippableStatInfo;
import it.univaq.pathofdungeons.domain.items.Item;
import it.univaq.pathofdungeons.domain.items.consumables.HealthPotion;
import it.univaq.pathofdungeons.domain.items.consumables.ManaPotion;
import it.univaq.pathofdungeons.domain.items.equippable.BaseItem;
import it.univaq.pathofdungeons.domain.items.equippable.Belt;
import it.univaq.pathofdungeons.domain.items.equippable.Boots;
import it.univaq.pathofdungeons.domain.items.equippable.Chestpiece;
import it.univaq.pathofdungeons.domain.items.equippable.Equippable;
import it.univaq.pathofdungeons.domain.items.equippable.Gloves;
import it.univaq.pathofdungeons.domain.items.equippable.Helmet;
import it.univaq.pathofdungeons.domain.items.equippable.Rarities;
import it.univaq.pathofdungeons.domain.items.equippable.Ring;
import it.univaq.pathofdungeons.domain.items.equippable.Weapon;
import it.univaq.pathofdungeons.domain.spells.Spells;
import it.univaq.pathofdungeons.utils.FileLogger;

public class ItemFactory {
    private static final String LOADOUTS_BASE = "config/loadouts/";
    private static final String LOADOUTS_EXT = ".json";
    private static final String BASES_BASE = "config/item_bases/";
    private static final String[] BASES_PATHS = new String[] {"belts", "boots", "chestpieces", "gloves", "helmets", "rings", "weapons"};
    private static final HashMap<EquipSlots, ArrayList<BaseItem>> basesMap;
    private static final int EQUIPPABLE_GENERATION_CHANCE = 30;
    private static final int POTION_GENERATION_CHANCE = 100;
    private static final int POTION_STACK_COST = 20;
    private static final int MAX_SHOP_ITEMS = 3;
    private static final int EQUIP_BASE_PRICE = 400;

    static {
        basesMap = new HashMap<>();
        //Load item configuration into static variables to avoid IO calls every time we create an item
        try{
            for(int i = 0; i < BASES_PATHS.length; i++){
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(BASES_BASE + BASES_PATHS[i] + LOADOUTS_EXT);
                ArrayList<BaseItem> bases = new ArrayList<>(JSON.parseArray(is.readAllBytes(), BaseItem.class));
                // Assuming all items in the same file have the same slot
                if(bases.getFirst().getSlot().equals(EquipSlots.LRING)) basesMap.put(EquipSlots.RRING, bases);
                basesMap.put(bases.getFirst().getSlot(), bases);
            }
            
        } catch(IOException e){
            FileLogger.getInstance().error(e.getMessage());
        }
    }

    private ItemFactory(){}

    /**
     * Returns a random equippable item. Item generation happens in phases, first a random base item from the predefined list is chose, 
     * then a rarity which determines the amount of stats it can have, 
     * then the stats are chosen randomly. Finally if the rarity is epic a random effect is chosen and if legendary a random spell is chosen.
     * @return a randomly generated equippable item
     */
    public static Equippable getRandomEquippable(){
        Random rand = new Random();
        EquipSlots slot = EquipSlots.values()[rand.nextInt(EquipSlots.values().length)];
        BaseItem base = basesMap.get(slot).get(rand.nextInt(basesMap.get(slot).size()));
        Rarities rarity = null;
        int r = rand.nextInt(100);
        for(int i = 0; i < Rarities.values().length; i++){
            r-= Rarities.values()[i].getChance();
            if(r <= 0){
                rarity = Rarities.values()[i];
                break;
            }
        }
        HashMap<EntityStats, Integer> stats = new HashMap<>();
        Map<EntityStats, EquippableStatInfo> slotStats = slot.getStatWeights();
        Set<EntityStats> keys = new HashSet<>(slotStats.keySet());
        int range = 100;
        for(int i = 0; i < rarity.getNumStats(); i++){
            int r1 = rand.nextInt(range);
            for(EntityStats key: keys){
                r1-= slotStats.get(key).getWeight();
                if(r1 <= 0){
                    int min = (int)(slotStats.get(key).getMin() * rarity.getStatMult());
                    int max = (int)(slotStats.get(key).getMax() * rarity.getStatMult()) + 1;
                    stats.put(key, rand.nextInt(min, max));
                    // Ensure each stat is only picked once
                    range -= slotStats.get(key).getWeight();
                    keys.remove(key);
                    break;
                }
            }
        }
        Effects effect = null;
        Spells spell = null;
        if(Rarities.EPIC.equals(rarity) || Rarities.LEGENDARY.equals(rarity)){
            // Prevent buffs from being added, items should only give on-hit effects
            effect = Effects.valueOf(Debuffs.values()[rand.nextInt(Debuffs.values().length)].name());
            if(Rarities.LEGENDARY.equals(rarity)){
                spell = Spells.values()[rand.nextInt(Spells.values().length)];
            }
        }
        switch(slot) {
            case BELT:
                return new Belt(base, stats, effect, rarity, spell);
            case BOOTS:
                return new Boots(base, stats, effect, rarity, spell);
            case CHESTPIECE:
                return new Chestpiece(base, stats, effect, rarity, spell);
            case GLOVES:
                return new Gloves(base, stats, effect, rarity, spell);
            case HELMET:
                return new Helmet(base, stats, effect, rarity, spell);
            case LRING:
                return new Ring(base, stats, effect, rarity, spell);
            case RRING:
                return new Ring(base, stats, effect, rarity, spell);
            case WEAPON:
                return new Weapon(base, stats, effect, rarity, spell);
            default:
                return null;
        }    
    }

    /**
     * Loads a player loadout from the ones defined in the properties folder and returns it
     * @param pclass character class of the loadout to be loaded
     * @return the loaded loadout
     */
    public static Loadout getPlayerLoadout(PlayerClasses pclass){
        // Reminder that JSON.parseObject will call constructors with default values if an entry is present but no value is assigned
        try(InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(LOADOUTS_BASE + pclass.toString().toLowerCase() + LOADOUTS_EXT)){
            return JSON.parseObject(is.readAllBytes(), Loadout.class);
        } catch(IOException e){
            FileLogger.getInstance().error(e.getMessage());
        }
        return null;
    }

    public static Consumable getRandomPotionStack(){
        Random rand = new Random();
        if(rand.nextInt(100) < 50){
            return new HealthPotion(rand.nextInt(5)+1);
        }
        return new ManaPotion(rand.nextInt(5)+1);
    }

    /**
     * Has a chance to generate a random equippable item based on the amount of enemies in the room. Each enemy has the same generation chance.
     * This method should be called after a battle room is completed using the initial enemies inside it.
     * @param enemies list of enemies in the room that was just completed
     * @return a possibly empty list of generated items
     */
    public static Collection<Item> getLootDrops(List<Enemy> enemies){
        LinkedList<Item> drops = new LinkedList<>();
        Random rand = new Random();
        for(int i = 0; i < enemies.size(); i++){
            int r = rand.nextInt(100); 
            if(r < EQUIPPABLE_GENERATION_CHANCE) drops.add(ItemFactory.getRandomEquippable());
            if(r < POTION_GENERATION_CHANCE) drops.add(ItemFactory.getRandomPotionStack());
        }
        return drops;
    }

    public static Map<Item, Integer> getShopItems(){
        HashMap<Item, Integer> items = new HashMap<>();
        Consumable potion = ItemFactory.getRandomPotionStack();
        items.put(potion, potion.getAmount() * POTION_STACK_COST);
        int numItems = new Random().nextInt(MAX_SHOP_ITEMS) + 1;
        for(int i = 0; i < numItems; i++){
            Equippable equip = ItemFactory.getRandomEquippable();
            int price = (int)(EQUIP_BASE_PRICE * equip.getRarity().getStatMult());
            items.put(equip, price);
        }
        return items;
    }
}

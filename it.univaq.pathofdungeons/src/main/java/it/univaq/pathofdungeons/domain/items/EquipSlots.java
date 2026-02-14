package it.univaq.pathofdungeons.domain.items;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSON;

import it.univaq.pathofdungeons.domain.entity.EntityStats;
import it.univaq.pathofdungeons.utils.FileLogger;

public enum EquipSlots {
    HELMET,
    CHESTPIECE,
    BOOTS,
    BELT,
    WEAPON,
    GLOVES,
    LRING,
    RRING;

    //Load possible stats for each slot in a map so we don't have to do any IO calls while generating items
    static{
        final String BASE_PATH = "config/item_stats/"; 
        final String EXT = ".json";
        try{
            for(EquipSlots es: EquipSlots.values()){
                String path;
                if(es.equals(LRING) || es.equals(RRING)) path = "ring";
                else path = es.toString().toLowerCase();
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(BASE_PATH + path + EXT);
                JSONObject jobj = JSON.parseObject(is.readAllBytes());
                HashMap<EntityStats, EquippableStatInfo> stats = new HashMap<>();
                for(String key: jobj.keySet()){
                    stats.put(EntityStats.valueOf(key), JSON.parseObject(jobj.getString(key), EquippableStatInfo.class));
                }
                es.setStats(stats);
            }
        } catch(IOException e){
            FileLogger.getInstance().error(e.getMessage());
        }
    }

    HashMap<EntityStats, EquippableStatInfo> possibleStats;

    private void setStats(HashMap<EntityStats, EquippableStatInfo> stats){ this.possibleStats = stats; }

    public Map<EntityStats, EquippableStatInfo> getStatWeights(){ return this.possibleStats; }

    @Override
    public String toString(){
        return this.name().charAt(0) + this.name().substring(1).toLowerCase();
    }
}

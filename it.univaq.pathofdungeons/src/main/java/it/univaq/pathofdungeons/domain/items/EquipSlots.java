package it.univaq.pathofdungeons.domain.items;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSON;

import it.univaq.pathofdungeons.domain.entity.EntityStats;

public enum EquipSlots {
    HELMET,
    CHESTPIECE,
    BOOTS,
    BELT,
    WEAPON,
    GLOVES,
    LRING,
    RRING;

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
            e.printStackTrace();
        }
    }

    HashMap<EntityStats, EquippableStatInfo> possibleStats;

    private void setStats(HashMap<EntityStats, EquippableStatInfo> stats){ this.possibleStats = stats; }

    public HashMap<EntityStats, EquippableStatInfo> getStatWeights(){ return this.possibleStats; }
}

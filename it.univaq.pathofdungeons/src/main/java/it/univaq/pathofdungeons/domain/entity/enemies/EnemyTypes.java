package it.univaq.pathofdungeons.domain.entity.enemies;

import it.univaq.pathofdungeons.utils.StringUtils;

public enum EnemyTypes {
    ZOMBIE(30),
    SKELETON(15),
    SPIDER(20),
    SLIME(20),
    MAGMASLIME(10),
    GOLEM(5),
    BOSS(0);

    private int weight;

    private EnemyTypes(int weight){
        this.weight = weight;
    }

    public int getWeight(){ return this.weight; }

    @Override
    public String toString(){
        return StringUtils.capitalize(this.name());
    }
}

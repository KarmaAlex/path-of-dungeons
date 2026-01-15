package it.univaq.pathofdungeons.domain.entity.enemies;

public enum EnemyTypes {
    ZOMBIE(30),
    SKELETON(15),
    SPIDER(20),
    SLIME(20),
    MAGMASLIME(10),
    GOLEM(5);

    private int weight;

    private EnemyTypes(int weight){
        this.weight = weight;
    }

    public int getWeight(){ return this.weight; }

    @Override
    public String toString(){
        return this.name().substring(0, 1).toUpperCase() + this.name().substring(1).toLowerCase();
    }
}

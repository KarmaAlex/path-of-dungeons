package it.univaq.pathofdungeons.domain.items;

public class EquippableStatInfo {
    private int weight;
    private int min;
    private int max;

    public EquippableStatInfo(int weight, int min, int max){
        this.weight = weight;
        this.min = min;
        this.max = max;
    }

    public int getWeight(){ return this.weight; }
    public int getMin(){ return this.min; }
    public int getMax(){ return this.max; }
}

package it.univaq.pathofdungeons.domain.items;

/**
 * Class for extra info tied to a stat for an equippable item, mostly contains info useful for
 * random generation, like what weight the stat should have and min and max rolls
 */
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

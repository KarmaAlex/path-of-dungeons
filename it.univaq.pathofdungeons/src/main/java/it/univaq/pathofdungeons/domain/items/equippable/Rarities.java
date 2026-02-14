package it.univaq.pathofdungeons.domain.items.equippable;

import it.univaq.pathofdungeons.utils.StringUtils;

/**
 * Possible rarities of an equippable item
 */
public enum Rarities {
    COMMON(40, 1.0f, 1),
    UNCOMMON(30, 1.25f, 1),
    RARE(15, 1.5f, 2),
    EPIC(10, 1.75f, 3),
    LEGENDARY(5, 2.0f, 4);

    private int chance;
    private float statMult;
    private int numStats;

    private Rarities(int chance, float statMult, int numStats){
        this.chance = chance;
        this.statMult = statMult;
        this.numStats = numStats;
    }

    public int getChance(){ return this.chance; }
    public float getStatMult(){ return this.statMult; }
    public int getNumStats(){ return this.numStats; }
    
    @Override
    public String toString(){
        return StringUtils.capitalize(this.name());
    }
}

package it.univaq.pathofdungeons.domain.entity.player;

public enum PlayerClasses {
    WARRIOR,
    MAGE,
    ARCHER,
    PALADIN;

    @Override
    public String toString(){
        return this.name().substring(0, 1).toUpperCase() + this.name().substring(1).toLowerCase();
    }
}

package it.univaq.pathofdungeons.domain.entity.player;

import it.univaq.pathofdungeons.utils.StringUtils;

public enum PlayerClasses {
    WARRIOR,
    MAGE,
    ARCHER,
    PALADIN;

    @Override
    public String toString(){
        return StringUtils.capitalize(this.name());
    }
}

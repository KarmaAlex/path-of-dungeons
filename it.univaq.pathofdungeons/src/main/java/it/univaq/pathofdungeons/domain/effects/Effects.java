package it.univaq.pathofdungeons.domain.effects;

import it.univaq.pathofdungeons.utils.StringUtils;

public enum Effects {
    BURN,
    POISON,
    HASTE,
    DEFEND,
    HEAL,
    PROTECT,
    DRAGON_DANCE;

    @Override
    public String toString(){
        return StringUtils.capitalize(this.name());
    }
}

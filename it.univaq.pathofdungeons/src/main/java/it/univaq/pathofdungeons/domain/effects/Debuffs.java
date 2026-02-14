package it.univaq.pathofdungeons.domain.effects;

import it.univaq.pathofdungeons.utils.StringUtils;

public enum Debuffs {
    BURN,
    POISON;

    @Override
    public String toString(){
        return StringUtils.capitalize(this.name());
    }
}

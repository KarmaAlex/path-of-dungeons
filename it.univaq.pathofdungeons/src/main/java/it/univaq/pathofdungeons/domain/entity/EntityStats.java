package it.univaq.pathofdungeons.domain.entity;

public enum EntityStats {
    MAXHEALTH("Max health"),
    HEALTH("Health"),
    HEALTHREGEN("Health regen"),
    MAXMANA("Max mana"),
    MANA("Mana"),
    MANAREGEN("Mana regen"),
    PHYSATTACK("Phyisical attack"),
    MAGICATTACK("Magic attack"),
    CRITCHANCE("Critical hit chance"),
    SPELLCRITCHANCE("Spell crit chance"),
    PHYSDEFENCE("Physical defence"),
    MAGICDEFENCE("Magic defence"),
    SPEED("Speed");

    private final String name;

    private EntityStats(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return this.name;
    }
}

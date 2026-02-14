package it.univaq.pathofdungeons.domain;

/**
 * Types of targeting of a BattleAction. For example SELF only allows the action
 * to target the source of it, ALL allows for targeting all entities in the battle etc...
 */
public enum TargetTypes {
    SELF,
    ALL,
    ENEMY,
    ALLY,
    ALLY_SELF,
    ALL_BUT_SELF,
    ALL_ENEMIES
}

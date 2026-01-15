package it.univaq.pathofdungeons.domain;

/**
 * Class that represents an action an entity can take in battle. Mainly used for
 * checking what entities an action can target
 */
public abstract class BattleAction {
    TargetTypes targetType;

    public BattleAction(TargetTypes t){ this.targetType = t;}

    public TargetTypes getTargets(){ return this.targetType; }
}

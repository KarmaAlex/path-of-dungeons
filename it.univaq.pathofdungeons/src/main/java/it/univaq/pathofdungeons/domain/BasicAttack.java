package it.univaq.pathofdungeons.domain;

public class BasicAttack extends BattleAction{
    public BasicAttack(){
        super(TargetTypes.ENEMY);
    }
}

package it.univaq.pathofdungeons.domain.effects;

import it.univaq.pathofdungeons.domain.entity.Entity;

public abstract class Effect {
    Effects effectType;

    public Effect(Effects effect){
        this.effectType = effect;
    }

    public void onTurnStart(Entity target){}

    public void onTurnEnd(Entity target){}

    public void apply(Entity target){
        for(Effect e: target.getEffects()){
            if(this.equals(e)) e.addStack();
        }
        target.addEffect(this);
        this.onApply(target);
    }

    public void addStack(){};

    public void onApply(Entity target){}

    public Effects getType(){ return this.effectType; }

    @Override
    public boolean equals(Object e){
        if(!(e instanceof Effect)) return false;
        Effect eff = (Effect)e;
        if (this.effectType.equals(eff.effectType)) return true;
        return false;
    }
}

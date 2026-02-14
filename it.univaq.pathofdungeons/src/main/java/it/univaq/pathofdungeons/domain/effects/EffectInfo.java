package it.univaq.pathofdungeons.domain.effects;

import java.io.Serializable;

/**
 * Class that represents information regarding effects, in particular stack amount and duration
 */
public class EffectInfo implements Serializable{
    private int duration;
    private int stacks;
    private Effect effect;

    public EffectInfo(Effect effect){
        this.duration = 0;
        this.stacks = 0;
        this.effect = effect;
    }

    public void incrDuration(int d){this.duration += d;}
    public void decrDuration(){this.duration--;}
    public int getDuration(){return this.duration;}
    public void incrStacks(){this.stacks++;}
    public void decrStacks(){this.stacks--;}
    public int getStacks(){return this.stacks;}
    public Effect getEffect(){return this.effect;}
}

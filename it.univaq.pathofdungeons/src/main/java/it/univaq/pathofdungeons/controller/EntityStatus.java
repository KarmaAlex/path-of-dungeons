package it.univaq.pathofdungeons.controller;

import javafx.scene.control.Labeled;

/**
 * Class that represents the status of an entity and is used in the interface during battles
 */
public class EntityStatus {
    private Labeled nameLabel;
    private Labeled healthLabel;
    private int health;
    private int maxHealth;
    private Labeled manaLabel;
    private int mana;
    private int maxMana;

    public EntityStatus(SelectableEntity nameL, Labeled healthL, Labeled manaL, String name, int health, int maxHealth, int mana, int maxMana){
        this(nameL.getButton(), healthL, manaL, name, health, maxHealth, mana, maxMana);
    }

    public EntityStatus(Labeled nameL, Labeled healthL, Labeled manaL, String name, int health, int maxHealth, int mana, int maxMana){
        this.nameLabel = nameL;
        this.nameLabel.setVisible(true);
        this.nameLabel.setText(name);
        this.healthLabel = healthL;
        this.healthLabel.setVisible(true);
        this.health = health;
        this.maxHealth = maxHealth;
        this.manaLabel = manaL;
        this.manaLabel.setVisible(true);
        this.mana = mana;
        this.maxMana = maxMana;
        this.updateHealth();
        this.updateMana();
    }

    public void setHealth(int health){ this.health = health; updateHealth(); }
    public void setMaxHealth(int maxHealth){ this.maxHealth = maxHealth; updateHealth(); }
    public void setMana(int mana){ this.mana = mana; updateMana(); }
    public void setMaxMana(int maxMana){ this.maxMana = maxMana; updateMana(); }
    public void setName(String name){ this.nameLabel.setText(name); }

    private void updateHealth(){ this.healthLabel.setText(String.format("%d/%d", health, maxHealth)); }
    private void updateMana(){ this.manaLabel.setText(String.format("%d/%d", mana, maxMana)); }

}

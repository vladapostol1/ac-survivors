package com.acsurvivors.entities.components;

public class StatsComponent {
    public int health;
    public int maxHealth;
    public int armor;
    public int damage;
    public int movementSpeed;
    public int attackSpeed;

    public boolean isAlive = true;

    public StatsComponent(int maxHealth, int armor, int damage, int movementSpeed, int attackSpeed) {
        this.health = maxHealth;
        this.maxHealth = maxHealth;
        this.armor = armor;
        this.damage = damage;
        this.movementSpeed = movementSpeed;
        this.attackSpeed = attackSpeed;
    }

    public void takeDamage(int damage) {
        int effectiveDamage = Math.max(damage - armor, 0);
        health -= effectiveDamage;
        if (health <= 0) {
            health = 0;
            isAlive = false;
        }
    }

    public void heal(int amount) {
        if (isAlive) {
            health = Math.min(health + amount, maxHealth);
        }
    }

    public void increaseStat(int choice)
    {
        switch (choice){
            case 0: maxHealth++;
            break;
            case 1: armor++;
            break;
            case 2: damage++;
            break;
            case 3: movementSpeed++;
            break;
            case 4: attackSpeed++;
            break;
        }
    }
}

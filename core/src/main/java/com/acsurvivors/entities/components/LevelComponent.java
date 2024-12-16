package com.acsurvivors.entities.components;

public class LevelComponent {
    public int level;
    public int experience;
    public int experienceNeeded;
    public int statPoints;

    public LevelComponent() {
        level = 1;
        experience = 0;
        experienceNeeded = 20;
        statPoints = 0;
    }

    public void gainExperience(int gainedExp) {
        experience += gainedExp;
        if(experience > experienceNeeded)
            levelUp();
    }

    public void levelUp() {
        experience = experience - experienceNeeded;
        experienceNeeded = (int)(experienceNeeded * 1.05f);
        statPoints++;
        level++;
    }
}

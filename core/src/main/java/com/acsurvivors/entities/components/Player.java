package com.acsurvivors.entities.components;

import com.acsurvivors.utils.ItemData;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private int gold;
    private List<ItemData> inventory;

    public Player() {
        this.gold = 10; // Example starting gold
        this.inventory = new ArrayList<>();
    }

    public int getGold() {
        return gold;
    }

    public void addGold(int amount) {
        gold += amount;
    }

    public void addItem(ItemData item) {
        inventory.add(item);
    }

    public List<ItemData> getInventory() {
        return inventory;
    }
}


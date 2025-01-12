package com.acsurvivors.utils;

public class ItemData {
    private final String name;
    private final int value;
    private final String stat;
    private final String iconPath;

    public ItemData(String name, int value, String stat, String iconPath) {
        this.name = name;
        this.value = value;
        this.stat = stat;
        this.iconPath = iconPath;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public String getStat() {
        return stat;
    }

    public String getIconPath() {
        return iconPath;
    }
}



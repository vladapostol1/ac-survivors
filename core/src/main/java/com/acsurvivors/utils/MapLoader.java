package com.acsurvivors.utils;
import com.badlogic.gdx.Gdx;

import com.acsurvivors.utils.AssetManager;

public class MapLoader {
    private final AssetManager assetManager;
    private String[][] mapData;

    public MapLoader(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public void loadMap(String filePath) {
        String mapText = Gdx.files.internal(filePath).readString();
        String[] rows = mapText.split("\n");
        mapData = new String[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            mapData[i] = rows[i].trim().split(" ");
        }
    }

    public String[][] getMapData() {
        return mapData;
    }
}

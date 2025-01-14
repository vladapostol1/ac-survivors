package com.acsurvivors.utils;
import com.acsurvivors.entities.components.ColliderComponent;
import com.badlogic.gdx.Gdx;

import com.acsurvivors.utils.AssetManager;

public class MapLoader {
    private final AssetManager assetManager;
    private String[][] mapData;
    public static final int TILE_SIZE = 64;

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

    // Verifica daca un tile este solid (blocant)
    public boolean isTileSolid(int x, int y) {
        String[][] mapData = getMapData();

        if (x < 0 || y < 0 || y >= mapData.length || x >= mapData[0].length) {
            return true;  // Tile-urile din afara hărții sunt solide
        }

        String tile = mapData[y][x];

        return !tile.equals("00");
    }

}

package com.acsurvivors.entities.components;

import com.badlogic.gdx.math.Rectangle;
import com.acsurvivors.utils.MapLoader;

public class ColliderComponent {
    public Rectangle bounds;
    private MapLoader mapLoader;

    public ColliderComponent(float x, float y, float width, float height, MapLoader mapLoader) {
        this.bounds = new Rectangle(x, y, width, height);
        this.mapLoader = mapLoader;
    }

    public void updatePosition(float x, float y) {
        if (!isCollidingWithMap(x, y)) {
            bounds.setPosition(x, y);
        }
    }

    private boolean isCollidingWithMap(float x, float y) {
        float width = bounds.width;
        float height = bounds.height;

        // Verificam coliziunea pentru fiecare margine a coliderului
        return mapLoader.isTileSolid((int) (x / MapLoader.TILE_SIZE), (int) (y / MapLoader.TILE_SIZE)) ||                         // Stanga jos
                mapLoader.isTileSolid((int) ((x + width) / MapLoader.TILE_SIZE), (int) (y / MapLoader.TILE_SIZE)) ||               // Dreapta jos
                mapLoader.isTileSolid((int) (x / MapLoader.TILE_SIZE), (int) ((y + height) / MapLoader.TILE_SIZE)) ||              // Stanga sus
                mapLoader.isTileSolid((int) ((x + width) / MapLoader.TILE_SIZE), (int) ((y + height) / MapLoader.TILE_SIZE));      // Dreapta sus
    }
    public boolean isCollidingWithEntity(ColliderComponent other) {
        return this.bounds.overlaps(other.bounds);
    }
}

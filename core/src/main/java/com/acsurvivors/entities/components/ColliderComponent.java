package com.acsurvivors.entities.components;

import com.badlogic.gdx.math.Rectangle;
import com.acsurvivors.utils.MapLoader;

public class ColliderComponent {
    public Rectangle bounds;
    private MapLoader mapLoader;

    /*
    O sa facem un collider manager clasa astra e strict pentru a declara coliziunea si a o centra pe mij sprite-ului pentru orice entitate
     */
    public ColliderComponent(float x, float y, float width, float height, MapLoader mapLoader) {
        this.bounds = new Rectangle(x, y, width, height);
        this.mapLoader = mapLoader;
    }

    public ColliderComponent(float x, float y, float width, float height) {
        this.bounds = new Rectangle(x, y, width, height);
    }

    public ColliderComponent(float x, float y, float width, float height, float spriteWidth, float spriteHeight) {
        bounds = new Rectangle(x, y, width, height);
        centerOnSprite(x, y, spriteWidth, spriteHeight);
    }

    public void updatePosition(float x, float y) {
        if (!isCollidingWithMap(x, y)) {
            bounds.setPosition(x, y);
        }
    }


    //same o sa le mutamam
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

    //To fix centerOnSprite
    public void centerOnSprite(float spriteX, float spriteY, float spriteWidth, float spriteHeight) {
        float offsetX = (spriteWidth - bounds.width) /2;
        float offsetY = (spriteHeight - bounds.height) / 2;
        updatePosition(spriteX + offsetX, spriteY + offsetY);
    }
}

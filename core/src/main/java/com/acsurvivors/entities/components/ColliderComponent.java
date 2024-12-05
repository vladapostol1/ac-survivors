package com.acsurvivors.entities.components;

import com.badlogic.gdx.math.Rectangle;

public class ColliderComponent {
    public Rectangle bounds;

    public ColliderComponent(float x, float y, float width, float height) {
        bounds = new Rectangle(x, y, width, height);
    }

    public ColliderComponent(float x, float y, float width, float height, float spriteWidth, float spriteHeight) {
        bounds = new Rectangle(x, y, width, height);
        centerOnSprite(x, y, spriteWidth, spriteHeight);
    }

    public void updatePosition(float x, float y) {
        bounds.setPosition(x, y);
    }

    //To fix centerOnSprite
    public void centerOnSprite(float spriteX, float spriteY, float spriteWidth, float spriteHeight) {
        float offsetX = (spriteWidth - bounds.width) /2;
        float offsetY = (spriteHeight - bounds.height) / 2;
        updatePosition(spriteX + offsetX, spriteY + offsetY);
    }
}

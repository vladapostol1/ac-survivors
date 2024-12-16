package com.acsurvivors.entities.components;

import com.badlogic.gdx.math.Rectangle;

public class ColliderComponent {
    public Rectangle bounds;
    public int offsetX, offsetY;


    public ColliderComponent(float x, float y, float width, float height) {
        this.bounds = new Rectangle(x, y, width, height);
    }

    public void updatePosition(float x, float y) {
        bounds.setPosition(x + offsetX, y + offsetY);
    }

    public void changeOffset(int x, int y) {
        offsetX = x;
        offsetY = y;
    }

    public boolean isCollidingWith(ColliderComponent other) {
        return this.bounds.overlaps(other.bounds);
    }
}


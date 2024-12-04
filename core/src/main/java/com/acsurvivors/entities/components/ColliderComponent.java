package com.acsurvivors.entities.components;

import com.badlogic.gdx.math.Rectangle;

public class ColliderComponent {
    public Rectangle bounds;

    public ColliderComponent(float x, float y, float width, float height) {
        bounds = new Rectangle(x, y, width, height);
    }

    public void updatePosition(float x, float y) {
        bounds.setPosition(x, y);
    }
}

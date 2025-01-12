package com.acsurvivors.ui.extended;

import com.acsurvivors.ui.IUIElement;
import com.acsurvivors.ui.Transformable;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class EmptySlot implements IUIElement, Transformable {
    private float x, y, width, height;
    private boolean active = true;
    private ShapeRenderer shapeRenderer;
    private Color backgroundColor;

    public EmptySlot(float x, float y, float width, float height, Color backgroundColor) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.backgroundColor = backgroundColor;
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (!active) return;

        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(backgroundColor);
        shapeRenderer.rect(x, y, width, height);
        shapeRenderer.end();
        batch.begin();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }
}

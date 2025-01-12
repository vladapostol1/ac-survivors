package com.acsurvivors.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

public class GridLayout implements IUIElement, Transformable {
    private final int rows;
    private final int cols;
    private final float elementWidth;
    private final float elementHeight;
    private final float gap;
    private float x, y;
    private boolean active = true;
    private final List<IUIElement> children;
    private Color backgroundColor;

    public GridLayout(float x, float y, int rows, int cols, float elementWidth, float elementHeight, float gap) {
        this.x = x;
        this.y = y;
        this.rows = rows;
        this.cols = cols;
        this.elementWidth = elementWidth;
        this.elementHeight = elementHeight;
        this.gap = gap;
        this.children = new ArrayList<>();
    }

    public void addChild(IUIElement child) {
        children.add(child);
        arrange();
    }

    public void removeChild(IUIElement child) {
        children.remove(child);
        arrange();
    }

    public void clearChildren() {
        children.clear();
    }

    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
    }

    private void arrange() {
        int index = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (index >= children.size()) return;

                IUIElement child = children.get(index);
                if (child instanceof Transformable) {
                    Transformable transformable = (Transformable) child;
                    float childX = x + col * (elementWidth + gap);
                    float childY = y - row * (elementHeight + gap);
                    transformable.setPosition(childX, childY);
                }
                index++;
            }
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (!active) return;

        if (backgroundColor != null) {
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(backgroundColor);
            shapeRenderer.rect(x, y - rows * (elementHeight + gap), cols * (elementWidth + gap), rows * (elementHeight + gap));
            shapeRenderer.end();
        }

        for (IUIElement child : children) {
            child.draw(batch);
        }
    }

    @Override
    public void dispose() {
        for (IUIElement child : children) {
            child.dispose();
        }
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
        for (IUIElement child : children) {
            child.setActive(active);
        }
    }

    @Override
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        arrange();
    }


    @Override
    public float getX(){ return this.x;}

    @Override
    public float getY(){ return this.y;}
}


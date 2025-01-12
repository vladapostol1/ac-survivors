package com.acsurvivors.ui;

import com.acsurvivors.entities.components.TransformComponent;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

public class Panel implements IUIElement, Transformable {
    private boolean active = true;
    private final List<IUIElement> children;
    private TransformComponent transformComponent;
    private Color backgroundColor;
    private float width;
    private float height;
    private ShapeRenderer shapeRenderer;

    public Panel(float x, float y) {
        transformComponent = new TransformComponent();
        transformComponent.x = x;
        transformComponent.y = y;
        this.children = new ArrayList<>();
        this.shapeRenderer = new ShapeRenderer();
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
    }

    public void addChild(IUIElement child) {
        children.add(child);
    }

    public void removeChild(IUIElement child) {
        children.remove(child);
    }

    public void clearChildren() {
        children.clear();
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (!active) return;

        if (backgroundColor != null) {
            batch.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(backgroundColor);
            shapeRenderer.rect(transformComponent.x, transformComponent.y, width, height);
            shapeRenderer.end();
            batch.begin();
        }

        for (IUIElement child : children) {
            if (child instanceof Transformable) {
                Transformable transformable = (Transformable) child;
                float originalX = transformable.getX();
                float originalY = transformable.getY();

                transformable.setPosition(originalX + transformComponent.x, originalY + transformComponent.y);
                child.draw(batch);

                transformable.setPosition(originalX, originalY);
            } else {
                child.draw(batch);
            }
        }
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
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
        transformComponent.x = x;
        transformComponent.y = y;
    }

    @Override
    public float getX(){ return transformComponent.x;}

    @Override
    public float getY(){ return transformComponent.y;}
}

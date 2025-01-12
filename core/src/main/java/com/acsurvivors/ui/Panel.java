package com.acsurvivors.ui;

import com.acsurvivors.entities.components.TransformComponent;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class Panel implements IUIElement {
    private boolean active = true;
    private final List<IUIElement> children;
    private TransformComponent transformComponent;

    public Panel(float x, float y) {
        transformComponent = new TransformComponent();
        transformComponent.x = x;
        transformComponent.y = y;
        this.children = new ArrayList<>();
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
}

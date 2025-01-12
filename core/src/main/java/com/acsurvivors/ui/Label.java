package com.acsurvivors.ui;

import com.acsurvivors.entities.components.TransformComponent;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Label implements  IUIElement, Transformable {
    public String text;
    public Color color;
    public BitmapFont font;
    public TransformComponent transform;
    private boolean active = true;

    public Label(String text, Color color, BitmapFont font, TransformComponent transform) {
        this.text = text;
        this.color = color;
        this.font = font;
        this.transform = transform;
    }

    public void draw(SpriteBatch batch) {
        font.setColor(color);
        font.draw(batch, text, transform.x, transform.y);
    }

    public void dispose() {
        font.dispose();
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active){
        this.active = active;
    }

    @Override
    public void setPosition(float x, float y) {
        this.transform.x = x;
        this.transform.y = y;
    }
}

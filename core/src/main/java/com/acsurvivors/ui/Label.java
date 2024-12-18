package com.acsurvivors.ui;

import com.acsurvivors.entities.components.TransformComponent;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Label {
    public String text;
    public Color color;
    public BitmapFont font;
    public TransformComponent transform;

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

    public void delete() {
        font.dispose();
    }
}

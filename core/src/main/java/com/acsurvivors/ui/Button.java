package com.acsurvivors.ui;

import com.acsurvivors.entities.components.TransformComponent;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Button {
    private Rectangle bounds;
    private ShapeRenderer shapeRenderer;
    private Label label;
    private Color backgroundColor;
    private Color hoverColor;
    private boolean isHovered;
    private Runnable onClick;

    public Button(String text, float x, float y, float width, float height, BitmapFont font, Color textColor, Color backgroundColor, Color hoverColor) {
        this.bounds = new Rectangle(x, y, width, height);
        this.backgroundColor = backgroundColor;
        this.hoverColor = hoverColor;
        this.shapeRenderer = new ShapeRenderer();

        GlyphLayout layout = new GlyphLayout(font, text);
        float labelWidth = layout.width;
        float labelHeight = layout.height;

        TransformComponent labelTransform = new TransformComponent();
        labelTransform.x = x + (width - labelWidth) / 2;
        labelTransform.y = y + (height + labelHeight) / 2;

        this.label = new Label(text, textColor, font, labelTransform);
    }


    public void draw(SpriteBatch batch) {
        Color currentColor = isHovered ? hoverColor : backgroundColor;

        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(currentColor);
        shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        shapeRenderer.end();
        batch.begin();

        label.draw(batch);
    }

    public void setOnClick(Runnable onClick) {
        this.onClick = onClick;
    }

    public void update(float mouseX, float mouseY, boolean isMousePressed) {
        isHovered = bounds.contains(mouseX, mouseY);

        if (isHovered && isMousePressed && onClick != null) {
            onClick.run();
        }
    }

    public void dispose() {
        label.delete();
        shapeRenderer.dispose();
    }
}

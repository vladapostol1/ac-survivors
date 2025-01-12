package com.acsurvivors.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class ProgressBar implements IUIElement {
    private Rectangle boundsOutline;
    private Rectangle bounds;
    private ShapeRenderer shapeRenderer;
    private Color marginColor;
    private Color mainColor;
    private int min;
    private int max;
    private int value;

    public ProgressBar(int x, int y, int width, int height, int min, int max, Color marginColor, Color mainColor, int outlineSize) {
        this.boundsOutline = new Rectangle(x, y, width, height);
        this.bounds = new Rectangle(x + outlineSize, y + outlineSize, width - (outlineSize * 2), height - (outlineSize * 2));
        this.marginColor = marginColor;
        this.mainColor = mainColor;
        this.min = min;
        this.max = max;
        this.value = min;
        this.shapeRenderer = new ShapeRenderer();
    }

    public void setValue(int value) {
        this.value = Math.max(min, Math.min(max, value));
        float progress = (float) (this.value - min) / (max - min);
        this.bounds.width = (boundsOutline.width - 2 * (boundsOutline.x - bounds.x)) * progress;
    }

    public void draw(SpriteBatch batch) {

        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(marginColor);
        shapeRenderer.rect(boundsOutline.x, boundsOutline.y, boundsOutline.width, boundsOutline.height);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(mainColor);
        shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        shapeRenderer.end();

        batch.begin();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }

    @Override
    public boolean isActive() {
        return active;
    }
}

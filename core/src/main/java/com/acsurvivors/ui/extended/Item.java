package com.acsurvivors.ui.extended;

import com.acsurvivors.entities.components.TransformComponent;
import com.acsurvivors.ui.IUIElement;
import com.acsurvivors.ui.Label;
import com.acsurvivors.ui.Transformable;
import com.acsurvivors.utils.ItemData;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Item implements IUIElement, Transformable {
    private final ItemData data;
    private final Texture icon;
    private final Rectangle bounds;
    private final ShapeRenderer shapeRenderer;
    private final Label tooltip;
    private boolean isHovered;
    private boolean active = true;

    public Item(ItemData data, Texture icon, float x, float y, float width, float height, BitmapFont font) {
        this.data = data;
        this.icon = icon;
        this.bounds = new Rectangle(x, y, width, height);
        this.shapeRenderer = new ShapeRenderer();

        String tooltipText = String.format("%s\n+%d %s", data.getName(), data.getValue(), data.getStat());
        GlyphLayout layout = new GlyphLayout(font, tooltipText);

        TransformComponent tooltipTransform = new TransformComponent();
        tooltipTransform.x = x + width + 10;
        tooltipTransform.y = y + height - layout.height / 2;

        this.tooltip = new Label(tooltipText, Color.WHITE, font, tooltipTransform);
        this.tooltip.setActive(false);
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (!active) return;

        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(isHovered ? Color.LIGHT_GRAY : Color.DARK_GRAY);
        shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        shapeRenderer.end();
        batch.begin();

        batch.draw(icon, bounds.x, bounds.y, bounds.width, bounds.height);

        if (isHovered) {
            tooltip.draw(batch);
        }
    }

    public void update(float mouseX, float mouseY) {
        isHovered = bounds.contains(mouseX, mouseY);

        if (isHovered) {
            tooltip.setActive(true);
        } else {
            tooltip.setActive(false);
        }
    }


    @Override
    public void dispose() {
        shapeRenderer.dispose();
        tooltip.dispose();
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
        bounds.setPosition(x, y);

        tooltip.setPosition(x + bounds.width + 10, y + bounds.height);
    }


    @Override
    public float getX(){ return 1.0f;}

    @Override
    public float getY(){ return 1.0f;}
}

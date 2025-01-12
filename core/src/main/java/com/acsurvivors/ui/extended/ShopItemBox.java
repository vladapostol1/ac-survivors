package com.acsurvivors.ui.extended;

import com.acsurvivors.entities.components.Player;
import com.acsurvivors.entities.components.TransformComponent;
import com.acsurvivors.ui.Button;
import com.acsurvivors.ui.IUIElement;
import com.acsurvivors.ui.Label;
import com.acsurvivors.ui.Transformable;
import com.acsurvivors.utils.ItemData;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ShopItemBox implements IUIElement, Transformable {
    private final ItemData itemData;
    private final Texture icon;
    private final Label nameLabel;
    private final Label statLabel;
    private final Button buyButton;
    private boolean active = true;

    private final int cost;
    private final Player player;

    private float x, y;
    private final float width, height;

    public ShopItemBox(ItemData itemData, Texture icon, float x, float y, float width, float height, BitmapFont font, Player player, int cost) {
        this.itemData = itemData;
        this.icon = icon;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.player = player;
        this.cost = cost;

        float iconSize = 48;
        float padding = 12;

        nameLabel = new Label(
            itemData.getName(),
            Color.WHITE,
            font,
            new TransformComponent(x + iconSize + padding * 2, y + height - 20)
        );

        statLabel = new Label(
            "+" + itemData.getValue() + " " + itemData.getStat(),
            Color.LIGHT_GRAY,
            font,
            new TransformComponent(x + iconSize + padding * 2, y + height - 40)
        );

        buyButton = new Button(
            "Buy (" + cost + " Gold)",
            x + width - 190,
            y + (height - 30) / 2,
            120,
            30,
            font,
            Color.WHITE,
            Color.DARK_GRAY,
            Color.LIGHT_GRAY
        );

        buyButton.setOnClick(() -> {
            if (player.getGold() >= cost) {
                player.addGold(-cost);
                player.addItem(itemData);
                System.out.println("Bought item: " + itemData.getName());
            } else {
                System.out.println("Not enough gold!");
            }
        });
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (!active) return;

        float iconSize = 48;
        float iconX = x + 12; // Padding
        float iconY = y + (height - iconSize) / 2;

        batch.draw(icon, iconX, iconY, iconSize, iconSize);

        nameLabel.draw(batch);
        statLabel.draw(batch);
        buyButton.draw(batch);
    }

    public void update(float mouseX, float mouseY, boolean isMousePressed) {
        if (!active) return;

        buyButton.update(mouseX, mouseY, isMousePressed);
    }

    @Override
    public void dispose() {
        nameLabel.dispose();
        statLabel.dispose();
        buyButton.dispose();
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
        nameLabel.setActive(active);
        statLabel.setActive(active);
        buyButton.setActive(active);
    }

    @Override
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;

        float iconSize = 48;
        float padding = 12;

        nameLabel.setPosition(x + iconSize + padding * 2, y + height - 20);
        statLabel.setPosition(x + iconSize + padding * 2, y + height - 40);
        buyButton.setPosition(x + width - 180, y + (height - 120) / 2);
    }

    @Override
    public float getX() {
        return this.x;
    }

    @Override
    public float getY() {
        return this.y;
    }
}

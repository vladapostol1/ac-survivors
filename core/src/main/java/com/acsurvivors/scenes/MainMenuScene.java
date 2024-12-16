package com.acsurvivors.scenes;

import com.acsurvivors.ui.Button;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainMenuScene extends BaseScene {
    private SpriteBatch batch;
    private Button playButton;

    @Override
    public void show() {
        batch = new SpriteBatch();
        BitmapFont font = new BitmapFont();

        playButton = new Button(
            "Play",
            200, 300, 200, 50,
            font, Color.WHITE, Color.DARK_GRAY, Color.GRAY
        );

        playButton.setOnClick(() -> {
            System.out.println("Play button clicked!");
        });
    }

    @Override
    public void render(float delta) {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
        boolean isMousePressed = Gdx.input.justTouched();

        playButton.update(mouseX, mouseY, isMousePressed);

        batch.begin();
        playButton.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        playButton.dispose();
        batch.dispose();
    }
}

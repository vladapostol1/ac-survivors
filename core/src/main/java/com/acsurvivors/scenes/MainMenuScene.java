package com.acsurvivors.scenes;

import com.acsurvivors.entities.components.TransformComponent;
import com.acsurvivors.ui.Button;
import com.acsurvivors.ui.Label;
import com.acsurvivors.utils.AssetManager;
import com.acsurvivors.utils.SceneManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.acsurvivors.utils.Constants.screenWidth;

public class MainMenuScene extends BaseScene {
    private SpriteBatch batch;
    private Label titleLabel;
    private Button playButton;
    private Button settingsButton;
    private Button quitButton;
    private SceneManager sceneManager;
    private AssetManager assetManager;

    public MainMenuScene(SceneManager sceneManager, AssetManager assetManager){
        this.sceneManager = sceneManager;
        this.assetManager = assetManager;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        BitmapFont font = new BitmapFont();

        String titleText = "AC Survivors";
        GlyphLayout layout = new GlyphLayout(assetManager.getFont("titleFont"), titleText);
        float titleWidth = layout.width;

        TransformComponent titleTransform = new TransformComponent();
        titleTransform.x = (screenWidth - titleWidth) / 2;
        titleTransform.y = 400;
        titleLabel = new Label(titleText, Color.WHITE, assetManager.getFont("titleFont"), titleTransform);

        playButton = new Button(
            "Play",
            screenWidth / 2 - 75, 280, 150, 50,
            assetManager.getFont("buttonFont"), Color.WHITE, Color.DARK_GRAY, Color.GRAY
        );

        playButton.setOnClick(() -> {
            sceneManager.setScene("Game");
        });

        settingsButton = new Button(
            "Settings",
            screenWidth / 2 - 75, 220, 150, 50,
            assetManager.getFont("buttonFont"), Color.WHITE, Color.DARK_GRAY, Color.GRAY
        );

        settingsButton.setOnClick(() -> {
            System.out.println("Play button clicked!");
        });

        quitButton = new Button(
            "Quit",
            screenWidth / 2 - 75, 160, 150, 50,
            assetManager.getFont("buttonFont"), Color.WHITE, Color.DARK_GRAY, Color.GRAY
        );

        quitButton.setOnClick(() -> {
            System.out.println("Play button clicked!");
        });
    }

    @Override
    public void render(float delta) {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
        boolean isMousePressed = Gdx.input.justTouched();

        playButton.update(mouseX, mouseY, isMousePressed);
        settingsButton.update(mouseX, mouseY, isMousePressed);
        quitButton.update(mouseX, mouseY, isMousePressed);

        batch.begin();

        Texture background = assetManager.getTexture("backgroundImage");
        float backgroundWidth = background.getWidth();
        float backgroundHeight = background.getHeight();

        float offsetX = (Gdx.graphics.getWidth() - backgroundWidth) / 2;
        float offsetY = (Gdx.graphics.getHeight() - backgroundHeight) / 2;

        batch.draw(background, offsetX, offsetY);

        playButton.draw(batch);
        settingsButton.draw(batch);
        quitButton.draw(batch);
        titleLabel.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        titleLabel.delete();
        playButton.dispose();
        settingsButton.dispose();
        quitButton.dispose();
        assetManager.dispose();
        batch.dispose();
    }
}

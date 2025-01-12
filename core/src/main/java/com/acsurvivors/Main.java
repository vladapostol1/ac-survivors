package com.acsurvivors;

import com.acsurvivors.scenes.GameScene;
import com.acsurvivors.scenes.MainMenuScene;
import com.acsurvivors.utils.AssetManager;
import com.acsurvivors.utils.SceneManager;
import com.badlogic.gdx.Game;

import static com.acsurvivors.utils.Constants.DEBUG_MODE;

public class Main extends Game {
    private SceneManager sceneManager;
    private AssetManager assetManager;

    @Override
    public void create() {
        sceneManager = new SceneManager(this);
        assetManager = new AssetManager();

        loadAssets();
        loadItems();

        //Scenes loader
        MainMenuScene mainMenuScene = new MainMenuScene(sceneManager, assetManager);
        GameScene gameScene = new GameScene(sceneManager, assetManager);

        sceneManager.addScene("MainMenu", mainMenuScene);
        sceneManager.addScene("Game", gameScene);

        //Doar pentru development
        if (DEBUG_MODE == false)
            sceneManager.setScene("MainMenu");
        else
            sceneManager.setScene("Game");
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        getScreen().dispose();
    }

    private void loadAssets() {
        assetManager.loadFont("titleFont", "fonts/PixelifySans-Bold.ttf", 48);
        assetManager.loadFont("buttonFont", "fonts/PixelifySans-Medium.ttf", 24);
        assetManager.loadFont("textFont", "fonts/PixelifySans-Medium.ttf", 16);
        assetManager.loadTexture("backgroundImage", "bg-1.png");
    }

    private void loadItems(){
        assetManager.loadItem("item01", "Cheese", 1, "maxHealth", "items/item01.png");
    }
}

package com.acsurvivors;

import com.acsurvivors.scenes.GameScene;
import com.acsurvivors.scenes.MainMenuScene;
import com.acsurvivors.utils.SceneManager;
import com.badlogic.gdx.Game;

import static com.acsurvivors.utils.Constants.DEBUG_MODE;

public class Main extends Game {
    private SceneManager sceneManager;

    @Override
    public void create() {
        sceneManager = new SceneManager(this);

        //Scenes loader
        MainMenuScene mainMenuScene = new MainMenuScene();
        GameScene gameScene = new GameScene();

        sceneManager.addScene("MainMenu", mainMenuScene);
        sceneManager.addScene("Game", gameScene);

        //Doar pentru development
        if (!DEBUG_MODE)
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
}

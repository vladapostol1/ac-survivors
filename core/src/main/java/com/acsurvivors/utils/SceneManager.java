package com.acsurvivors.utils;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import java.util.HashMap;
import java.util.Map;

public class SceneManager {
    private final Game game;
    private final Map<String,Screen> scenes;

    public SceneManager(Game game) {
        this.game = game;
        this.scenes = new HashMap<>();
    }

    public void addScene(String name, Screen scene) {
        scenes.put(name, scene);
    }

    public void setScene(String name) {
        Screen scene = scenes.get(name);
        if (scene == null) {
            throw new IllegalArgumentException("Nu am gasit ecranul: " + name);
        }

        game.setScreen(scene);
    }

    public void dispose() {
        for (Screen scene : scenes.values()) {
            scene.dispose();
        }
        scenes.clear();
    }
}
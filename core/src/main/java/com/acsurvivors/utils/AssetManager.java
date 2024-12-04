package com.acsurvivors.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;

public class AssetManager {
    private final Map<String, Texture> textures;
    private final Map<String, Sound> sounds;
    private final Map<String, Music> musicTracks;

    public AssetManager() {
        textures = new HashMap<>();
        sounds = new HashMap<>();
        musicTracks = new HashMap<>();
    }

    public void loadTexture(String path) {
        if (!textures.containsKey(path)) {
            Texture texture = new Texture(Gdx.files.internal(path));
            textures.put(path, texture);
        }
    }

    public void loadSound(String path) {
        if (!sounds.containsKey(path)) {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
            sounds.put(path, sound);
        }
    }

    public void loadMusic(String path) {
        if (!musicTracks.containsKey(path)) {
            Music music = Gdx.audio.newMusic(Gdx.files.internal(path));
            musicTracks.put(path, music);
        }
    }

    public Texture getTexture(String path) {
        if (!textures.containsKey(path)) {
            throw new IllegalStateException("Texture not loaded: " + path);
        }
        return textures.get(path);
    }

    public Sound getSound(String path) {
        if (!sounds.containsKey(path)) {
            throw new IllegalStateException("Sound not loaded: " + path);
        }
        return sounds.get(path);
    }

    public Music getMusic(String path) {
        if (!musicTracks.containsKey(path)) {
            throw new IllegalStateException("Music not loaded: " + path);
        }
        return musicTracks.get(path);
    }

    public void dispose() {
        for (Texture texture : textures.values()) {
            texture.dispose();
        }
        textures.clear();

        for (Sound sound : sounds.values()) {
            sound.dispose();
        }
        sounds.clear();

        for (Music music : musicTracks.values()) {
            music.dispose();
        }
        musicTracks.clear();
    }

    public void loadMapTextures(String... paths) {
        for (String path : paths) {
            loadTexture(path + ".png");
        }
    }
}


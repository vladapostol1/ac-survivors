package com.acsurvivors.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.HashMap;
import java.util.Map;


public class AssetManager {
    private final Map<String, Texture> textures;
    private final Map<String, Sound> sounds;
    private final Map<String, Music> musicTracks;
    private final Map<String, BitmapFont> fonts;

    public AssetManager() {
        textures = new HashMap<>();
        sounds = new HashMap<>();
        musicTracks = new HashMap<>();
        fonts = new HashMap<>();
    }

    public void loadTexture(String name, String path) {
        if (!textures.containsKey(name)) {
            Texture texture = new Texture(Gdx.files.internal(path));
            textures.put(name, texture);
        }
    }

    public Texture getTexture(String name) {
        if (!textures.containsKey(name)) {
            throw new IllegalStateException("Texture not loaded: " + name);
        }
        return textures.get(name);
    }

    public void loadSound(String name, String path) {
        if (!sounds.containsKey(name)) {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
            sounds.put(name, sound);
        }
    }

    public Sound getSound(String name) {
        if (!sounds.containsKey(name)) {
            throw new IllegalStateException("Sound not loaded: " + name);
        }
        return sounds.get(name);
    }

    public void loadMusic(String name, String path) {
        if (!musicTracks.containsKey(name)) {
            Music music = Gdx.audio.newMusic(Gdx.files.internal(path));
            musicTracks.put(name, music);
        }
    }

    public Music getMusic(String name) {
        if (!musicTracks.containsKey(name)) {
            throw new IllegalStateException("Music not loaded: " + name);
        }
        return musicTracks.get(name);
    }

    public void loadFont(String name, String ttfPath, int fontSize) {
        if (!fonts.containsKey(name)) {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(ttfPath));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = fontSize;
            BitmapFont font = generator.generateFont(parameter);
            fonts.put(name, font);
            generator.dispose();
        }
    }

    public BitmapFont getFont(String name) {
        if (!fonts.containsKey(name)) {
            throw new IllegalStateException("Font not loaded: " + name);
        }
        return fonts.get(name);
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

        for (BitmapFont font : fonts.values()) {
            font.dispose();
        }
        fonts.clear();
    }

    public void loadMultipleTextures(String[] names, String[] paths) {
        if (names.length != paths.length) {
            throw new IllegalArgumentException("Names and paths arrays must have the same length.");
        }

        for (int i = 0; i < names.length; i++) {
            loadTexture(names[i], paths[i]);
        }
    }
}

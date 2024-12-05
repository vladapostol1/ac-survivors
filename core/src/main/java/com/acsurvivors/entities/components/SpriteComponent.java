package com.acsurvivors.entities.components;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteComponent {
    public TextureRegion textureRegion;
    public float scale = 1.0f;

    public SpriteComponent() {}

    public SpriteComponent(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    public SpriteComponent(TextureRegion textureRegion, float scale) {
        this.textureRegion = textureRegion;
        this.scale = scale;
    }
}

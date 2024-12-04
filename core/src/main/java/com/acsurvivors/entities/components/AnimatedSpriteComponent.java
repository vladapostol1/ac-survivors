package com.acsurvivors.entities.components;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimatedSpriteComponent {
    public Animation<TextureRegion> currentAnimation;
    public float animationTime = 0f;

    public void updateAnimation(float delta) {
        animationTime += delta;
    }

    public TextureRegion getCurrentFrame() {
        return currentAnimation.getKeyFrame(animationTime, true);
    }
}

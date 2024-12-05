package com.acsurvivors.entities.components;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.ArrayList;
import java.util.HashMap;

public class AnimatedSpriteComponent {

    public static class Animation {
        public ArrayList<TextureRegion> frames;
        public float duration;

        public Animation(float duration) {
            this.frames = new ArrayList<>();
            this.duration = duration;
        }

        public void addFrame(TextureRegion frame) {
            frames.add(frame);
        }
    }

    private HashMap<String, Animation> animations;
    private String currentAnimation;
    private float stateTime;
    private int currentFrameIndex;

    public boolean flipX = false;
    public float scaleX = 1.0f;
    public float scaleY = 1.0f;

    public AnimatedSpriteComponent() {
        animations = new HashMap<>();
        stateTime = 0f;
        currentAnimation = null;
    }

    public void addAnimation(String name, Animation animation) {
        animations.put(name, animation);
    }

    public void setAnimation(String name) {
        if (!name.equals(currentAnimation)) {
            currentAnimation = name;
            stateTime = 0f;
            currentFrameIndex = 0;
        }
    }

    public TextureRegion getCurrentFrame(float deltaTime) {
        if (currentAnimation == null || !animations.containsKey(currentAnimation)) {
            return null;
        }

        Animation animation = animations.get(currentAnimation);
        stateTime += deltaTime;

        int totalFrames = animation.frames.size();
        float frameDuration = animation.duration / totalFrames;
        currentFrameIndex = (int) (stateTime / frameDuration) % totalFrames;

        TextureRegion currentFrame = animation.frames.get(currentFrameIndex);
        currentFrame.flip(flipX != currentFrame.isFlipX(), false);
        return currentFrame;
    }

    public HashMap<String, Animation> getAnimations() {
        return animations;
    }
}

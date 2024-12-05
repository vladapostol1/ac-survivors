package com.acsurvivors.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CustomOrthographicCamera {
    private final OrthographicCamera camera;
    private final Vector2 position;
    private float lerpSpeed = 0.1f;
    private float worldWidth, worldHeight;
    private static final float TOLERANCE = 0.1f;

    public CustomOrthographicCamera(float viewportWidth, float viewportHeight) {
        camera = new OrthographicCamera(viewportWidth, viewportHeight);
        this.camera.viewportWidth = 512;  // Dimensiunea camerei pe axa X
        this.camera.viewportHeight = 512; // Dimensiunea camerei pe axa Y
        camera.update();
        position = new Vector2(camera.position.x, camera.position.y);

    }

    public void setWorldBounds(float width, float height) {
        this.worldWidth = width;
        this.worldHeight = height;
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
        updatePosition();
    }

    public void moveTo(float x, float y, float delta) {
        position.lerp(new Vector2(x, y), lerpSpeed * delta);
        updatePosition();
    }

    private void updatePosition() {
        float halfViewportWidth = camera.viewportWidth / 2f;
        float halfViewportHeight = camera.viewportHeight / 2f;

        float buffer = 16f;

        // Limitele hartii
        float minX = halfViewportWidth + buffer;
        float maxX = worldWidth - halfViewportWidth - buffer;
        float minY = halfViewportHeight + buffer;
        float maxY = worldHeight - halfViewportHeight - buffer;


        if (Math.abs(position.x - minX) < TOLERANCE) {
            position.x = minX;
        } else if (Math.abs(position.x - maxX) < TOLERANCE) {
            position.x = maxX;
        } else {
            position.x = Math.max(minX, Math.min(maxX, position.x));
        }

        if (Math.abs(position.y - minY) < TOLERANCE) {
            position.y = minY;
        } else if (Math.abs(position.y - maxY) < TOLERANCE) {
            position.y = maxY;
        } else {
            position.y = Math.max(minY, Math.min(maxY, position.y));
        }


        camera.position.set(position.x, position.y, 0);
        camera.update();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void setLerpSpeed(float speed) {
        this.lerpSpeed = speed;
    }

    public Vector2 worldToScreen(Vector2 worldCoords) {
        Vector3 screenCoords = camera.project(new Vector3(worldCoords.x, worldCoords.y, 0));
        return new Vector2(screenCoords.x, screenCoords.y);
    }

    public Vector2 screenToWorld(Vector2 screenCoords) {
        Vector3 worldCoords = camera.unproject(new Vector3(screenCoords.x, screenCoords.y, 0));
        return new Vector2(worldCoords.x, worldCoords.y);
    }

}


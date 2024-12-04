package com.acsurvivors.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CustomOrthographicCamera {
    private final OrthographicCamera camera;
    private final Vector2 position;
    private float lerpSpeed = 4.0f;
    private float worldWidth, worldHeight;

    public CustomOrthographicCamera(float viewportWidth, float viewportHeight) {
        camera = new OrthographicCamera(viewportWidth, viewportHeight);
        position = new Vector2(camera.position.x, camera.position.y);
        camera.update();
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

        if (worldWidth > 0 && worldHeight > 0) {
            position.x = Math.max(halfViewportWidth, Math.min(worldWidth - halfViewportWidth, position.x));
            position.y = Math.max(halfViewportHeight, Math.min(worldHeight - halfViewportHeight, position.y));
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


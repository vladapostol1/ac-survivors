package com.acsurvivors.entities.systems;

import com.acsurvivors.entities.Entity;
import com.acsurvivors.entities.EntityManager;
import com.acsurvivors.entities.components.ColliderComponent;
import com.acsurvivors.entities.components.TransformComponent;
import com.acsurvivors.utils.CustomOrthographicCamera;
import com.acsurvivors.utils.MapLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class DebugRenderSystem{
    private ShapeRenderer shapeRenderer;
    private MapLoader mapLoader;
    private CustomOrthographicCamera camera;

    public DebugRenderSystem(MapLoader mapLoader, CustomOrthographicCamera camera) {
        this.shapeRenderer = new ShapeRenderer();
        this.mapLoader = mapLoader;
        this.camera = camera;
    }

    public void render(EntityManager entityManager) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // Deseneaza tile-urile solide
        drawSolidTiles();

        for (Entity entity : entityManager.getEntities()) {
            if (entity.hasComponent(ColliderComponent.class) &&
                    entity.hasComponent(TransformComponent.class)) {
                ColliderComponent collider = entity.getComponent(ColliderComponent.class);
                TransformComponent transform = entity.getComponent(TransformComponent.class);

                shapeRenderer.setColor(Color.RED);
                shapeRenderer.rect(collider.bounds.x, collider.bounds.y,
                        collider.bounds.width, collider.bounds.height);
            }
        }

        // Deseneaza limitele camerei
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(camera.getCamera().position.x - camera.getCamera().viewportWidth / 2,
                camera.getCamera().position.y - camera.getCamera().viewportHeight / 2,
                camera.getCamera().viewportWidth, camera.getCamera().viewportHeight);

        shapeRenderer.end();
    }

    private void drawSolidTiles() {
        String[][] mapData = mapLoader.getMapData();

        if (mapData == null) return;

        shapeRenderer.setColor(Color.BLUE);

        // Parcurgem harta și desenam un dreptunghi albastru pentru tile-urile solide
        for (int y = 0; y < mapData.length; y++) {
            for (int x = 0; x < mapData[y].length; x++) {
                if (mapLoader.isTileSolid(x, y)) {
                   shapeRenderer.rect(x * 32, y * 32, 32, 32);  // Tile de 32x32
                }
            }
        }
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}

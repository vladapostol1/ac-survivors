package com.acsurvivors.entities;

import com.acsurvivors.entities.Entity;
import com.acsurvivors.entities.EntityManager;
import com.acsurvivors.entities.components.ColliderComponent;
import com.acsurvivors.entities.components.SpriteComponent;
import com.acsurvivors.entities.components.TransformComponent;
import com.acsurvivors.utils.AssetManager;
import com.acsurvivors.utils.CustomOrthographicCamera;
import com.acsurvivors.utils.MapLoader;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import java.util.List;
import java.util.Random;

import static com.acsurvivors.utils.Constants.TILE_SIZE;

public class EnemySpawner {

    private final EntityManager entityManager;
    private final AssetManager assetManager;
    private final CustomOrthographicCamera camera;
    private final MapLoader mapLoader;
    private static final float SPAWN_DISTANCE = 150f;

    public EnemySpawner(EntityManager entityManager, AssetManager assetManager, CustomOrthographicCamera camera, MapLoader mapLoader) {
        this.entityManager = entityManager;
        this.assetManager = assetManager;
        this.camera = camera;
        this.mapLoader = mapLoader;
    }

    public void spawnEnemyNearPlayer(float playerX, float playerY) {
        Random random = new Random();
        float spawnX, spawnY;

        // Calculam marginile camerei
        float cameraLeft = playerX - camera.getCamera().viewportWidth / 2;
        float cameraRight = playerX + camera.getCamera().viewportWidth / 2;
        float cameraTop = playerY + camera.getCamera().viewportHeight / 2;
        float cameraBottom = playerY - camera.getCamera().viewportHeight / 2;

        // Determinam pozitia de spawn in afara camerei
        if (random.nextBoolean()) {
            spawnX = random.nextBoolean() ? cameraLeft - SPAWN_DISTANCE : cameraRight + SPAWN_DISTANCE;
            spawnY = MathUtils.random(cameraBottom - SPAWN_DISTANCE, cameraTop + SPAWN_DISTANCE);
        } else {
            spawnY = random.nextBoolean() ? cameraBottom - SPAWN_DISTANCE : cameraTop + SPAWN_DISTANCE;
            spawnX = MathUtils.random(cameraLeft - SPAWN_DISTANCE, cameraRight + SPAWN_DISTANCE);
        }

        // Cream inamicul
        Entity enemy = entityManager.createEntity();
        TransformComponent transform = new TransformComponent();
        transform.x = spawnX;
        transform.y = spawnY;
        enemy.addComponent(TransformComponent.class, transform);

        SpriteComponent sprite = new SpriteComponent(new TextureRegion(assetManager.getTexture("player_idle_1")));
        enemy.addComponent(SpriteComponent.class, sprite);

        ColliderComponent collider = new ColliderComponent(transform.x, transform.y, TILE_SIZE, TILE_SIZE, mapLoader);
        enemy.addComponent(ColliderComponent.class, collider);
        collider.updatePosition(transform.x, transform.y);
    }

    private boolean isCollidingWithOtherEnemies(float x, float y) {
        List<Entity> entities = entityManager.getEntities();
        Rectangle newEnemyBounds = new Rectangle(x, y, TILE_SIZE, TILE_SIZE);

        for (Entity entity : entities) {
            if (entity.hasComponent(ColliderComponent.class)) {
                ColliderComponent collider = entity.getComponent(ColliderComponent.class);
                if (collider.bounds.overlaps(newEnemyBounds)) {
                    return true;
                }
            }
        }
        return false;
    }
}

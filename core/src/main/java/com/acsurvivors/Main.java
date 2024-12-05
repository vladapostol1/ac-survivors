package com.acsurvivors;

import com.acsurvivors.entities.EnemySpawner;
import com.acsurvivors.entities.Entity;
import com.acsurvivors.entities.EntityManager;
import com.acsurvivors.entities.components.ColliderComponent;
import com.acsurvivors.entities.components.SpriteComponent;
import com.acsurvivors.entities.components.TransformComponent;
import com.acsurvivors.entities.components.ControlComponent;
import com.acsurvivors.entities.systems.CollisionSystem;
import com.acsurvivors.entities.systems.DebugRenderSystem;
import com.acsurvivors.entities.systems.RenderingSystem;
import com.acsurvivors.entities.systems.ControlSystem;
import com.acsurvivors.utils.AssetManager;
import com.acsurvivors.utils.CustomOrthographicCamera;
import com.acsurvivors.utils.MapLoader;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

import static com.acsurvivors.utils.Constants.TILE_SIZE;

public class Main extends ApplicationAdapter {

    private SpriteBatch batch;
    private EntityManager entityManager;
    private RenderingSystem renderingSystem;
    private MapLoader mapLoader;
    private AssetManager assetManager;
    private ControlSystem controlSystem;
    private CustomOrthographicCamera camera;
    private DebugRenderSystem debugRenderSystem;
    private EnemySpawner enemySpawner;
    private CollisionSystem collisionSystem;
    private float spawnTimer = 0;  // Timer pentru spawnare inamici
    private static final float SPAWN_INTERVAL = 3f;


    @Override
    public void create() {
        batch = new SpriteBatch();
        entityManager = new EntityManager();
        assetManager = new AssetManager();
        mapLoader = new MapLoader(assetManager);
        camera = new CustomOrthographicCamera(3000, 3000);
        collisionSystem = new CollisionSystem();

        //Assets import
        assetManager.loadMapTextures("00");
        assetManager.loadMapTextures("01");
        assetManager.loadMapTextures("02");
        assetManager.loadMapTextures("03");
        assetManager.loadMapTextures("04");
        assetManager.loadMapTextures("05");
        assetManager.loadMapTextures("06");
        assetManager.loadMapTextures("07");
        assetManager.loadMapTextures("08");
        mapLoader.loadMap("maps/test_ground.txt");
        assetManager.loadTexture("player_idle.png");
        //assetManager.loadTexture("enemy1.png");

        //Create player
        int mapWidth = mapLoader.getMapData()[0].length * 32;
        int mapHeight = mapLoader.getMapData().length * 32;
        int centerX = mapWidth / 2;
        int centerY = mapHeight / 2;
        System.out.println("centerX " + centerX);
        System.out.println("centerX " + centerY);

        //Player
        Entity player = entityManager.createEntity();
        TransformComponent transform = new TransformComponent();
        transform.x = centerX;
        transform.y = centerY;
        player.addComponent(TransformComponent.class, transform);

        ControlComponent control = new ControlComponent();
        player.addComponent(ControlComponent.class, control);

        SpriteComponent sprite = new SpriteComponent();
        sprite.texture = assetManager.getTexture("player_idle.png");
        player.addComponent(SpriteComponent.class, sprite);

        ColliderComponent collider = new ColliderComponent(transform.x, transform.y, TILE_SIZE / 2, TILE_SIZE / 2, mapLoader);
        player.addComponent(ColliderComponent.class, collider);


        //Enemy


        camera.setPosition(centerX, centerY);

        renderingSystem = new RenderingSystem(batch, assetManager);

        controlSystem = new ControlSystem(mapLoader);

        debugRenderSystem = new DebugRenderSystem(mapLoader, camera);

        camera.setWorldBounds(mapWidth, mapHeight);
        enemySpawner = new EnemySpawner(entityManager, assetManager, camera, mapLoader);


    }

    @Override
    public void render() {
        ScreenUtils.clear(0f, 0f, 0f, 1f);

        float delta = Gdx.graphics.getDeltaTime();

        //updates
        controlSystem.update(entityManager, delta);
        Entity player = entityManager.getEntities().get(0);
        TransformComponent playerTransform = player.getComponent(TransformComponent.class);
        camera.setPosition(playerTransform.x + TILE_SIZE / 2, playerTransform.y + TILE_SIZE / 2);
        camera.moveTo(playerTransform.x, playerTransform.y, delta);
        camera.getCamera().update();

        renderingSystem.setProjectionMatrix(camera.getCamera().combined);

        // Render the map and entities
        renderingSystem.renderMap(mapLoader.getMapData(), TILE_SIZE);
        renderingSystem.render(entityManager);

        spawnTimer += delta;
        if (spawnTimer >= SPAWN_INTERVAL) {
            enemySpawner.spawnEnemyNearPlayer(playerTransform.x, playerTransform.y);
            spawnTimer = 0;
        }
        //debugRenderSystem.render(entityManager);
    }

    @Override
    public void dispose() {
        batch.dispose();
        for (Entity entity : entityManager.getEntities()) {
            if (entity.hasComponent(SpriteComponent.class)) {
                entity.getComponent(SpriteComponent.class).texture.dispose();
            }
        }
    }
}

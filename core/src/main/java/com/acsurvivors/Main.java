package com.acsurvivors;

import com.acsurvivors.entities.EnemySpawner;
import com.acsurvivors.entities.Entity;
import com.acsurvivors.entities.EntityManager;
import com.acsurvivors.entities.components.*;
import com.acsurvivors.utils.RenderingSystem;
import com.acsurvivors.entities.components.ColliderComponent;
import com.acsurvivors.entities.components.SpriteComponent;
import com.acsurvivors.entities.components.TransformComponent;
import com.acsurvivors.entities.components.ControlComponent;
import com.acsurvivors.entities.systems.CollisionSystem;
import com.acsurvivors.entities.systems.DebugRenderSystem;
import com.acsurvivors.entities.systems.ControlSystem;
import com.acsurvivors.utils.AssetManager;
import com.acsurvivors.utils.CustomOrthographicCamera;
import com.acsurvivors.utils.MapLoader;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.HashSet;

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
        camera = new CustomOrthographicCamera(640, 480);

        //Assets import
        //Map tiles and map plan
        String[] tiles_name = {"00","01","02","03","04","05","06","07","08"};
        String[] tiles_path = {"tiles/00.png","tiles/01.png","tiles/02.png","tiles/03.png","tiles/04.png",
            "tiles/05.png","tiles/06.png","tiles/07.png","tiles/08.png"};
        assetManager.loadMultipleTextures(tiles_name, tiles_path);
        mapLoader.loadMap("maps/test_ground.txt");

        //Sprites
        assetManager.loadTexture("player_idle_1", "sprites/player/player_idle_1.png");
        assetManager.loadTexture("player_idle_2", "sprites/player/player_idle_2.png");
        assetManager.loadTexture("player_idle_3", "sprites/player/player_idle_3.png");
        assetManager.loadTexture("player_idle_4", "sprites/player/player_idle_4.png");
        assetManager.loadTexture("player_walk_1", "sprites/player/player_walk_1.png");
        assetManager.loadTexture("player_walk_2", "sprites/player/player_walk_2.png");
        assetManager.loadTexture("player_walk_3", "sprites/player/player_walk_3.png");

        //calc center of the map
        int mapWidth = mapLoader.getMapData()[0].length * 32;
        int mapHeight = mapLoader.getMapData().length * 32;
        int centerX = mapWidth / 2;
        int centerY = mapHeight / 2;
        System.out.println("centerX " + centerX);
        System.out.println("centerX " + centerY);

        //Create player
        Entity player = entityManager.createEntity();
        TransformComponent transform = new TransformComponent();
        transform.x = centerX;
        transform.y = centerY;
        player.addComponent(TransformComponent.class, transform);

        ControlComponent control = new ControlComponent();
        player.addComponent(ControlComponent.class, control);

        AnimatedSpriteComponent animatedSprite = new AnimatedSpriteComponent();

        AnimatedSpriteComponent.Animation idleAnimation = new AnimatedSpriteComponent.Animation(1f);
        idleAnimation.addFrame(new TextureRegion(assetManager.getTexture("player_idle_1")));
        idleAnimation.addFrame(new TextureRegion(assetManager.getTexture("player_idle_2")));
        idleAnimation.addFrame(new TextureRegion(assetManager.getTexture("player_idle_3")));
        idleAnimation.addFrame(new TextureRegion(assetManager.getTexture("player_idle_4")));
        animatedSprite.addAnimation("idle", idleAnimation);

        AnimatedSpriteComponent.Animation walkAnimation = new AnimatedSpriteComponent.Animation(0.8f);
        walkAnimation.addFrame(new TextureRegion(assetManager.getTexture("player_walk_1")));
        walkAnimation.addFrame(new TextureRegion(assetManager.getTexture("player_walk_2")));
        walkAnimation.addFrame(new TextureRegion(assetManager.getTexture("player_walk_3")));

        animatedSprite.scaleX = 1.5f;
        animatedSprite.scaleY = 1.5f;
        animatedSprite.addAnimation("walk", walkAnimation);

        animatedSprite.setAnimation("idle");

        player.addComponent(AnimatedSpriteComponent.class, animatedSprite);
        ColliderComponent collider = new ColliderComponent(transform.x, transform.y, TILE_SIZE, TILE_SIZE, mapLoader);
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
        HashSet<Texture> disposedTextures = new HashSet<>();

        // Dispose resources for all entities
        for (Entity entity : entityManager.getEntities()) {
            if (entity.hasComponent(SpriteComponent.class)) {
                Texture texture = entity.getComponent(SpriteComponent.class).textureRegion.getTexture();
                if (texture != null && !disposedTextures.contains(texture)) {
                    texture.dispose();
                    disposedTextures.add(texture);
                }
            }

            if (entity.hasComponent(AnimatedSpriteComponent.class)) {
                AnimatedSpriteComponent animComponent = entity.getComponent(AnimatedSpriteComponent.class);
                for (AnimatedSpriteComponent.Animation animation : animComponent.getAnimations().values()) {
                    for (TextureRegion frame : animation.frames) {
                        Texture texture = frame.getTexture();
                        if (texture != null && !disposedTextures.contains(texture)) {
                            texture.dispose();
                            disposedTextures.add(texture);
                        }
                    }
                }
            }
        }

        assetManager.dispose();
    }
}

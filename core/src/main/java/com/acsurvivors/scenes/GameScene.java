package com.acsurvivors.scenes;

import com.acsurvivors.entities.EnemySpawner;
import com.acsurvivors.entities.Entity;
import com.acsurvivors.entities.EntityManager;
import com.acsurvivors.entities.components.*;
import com.acsurvivors.ui.ProgressBar;
import com.acsurvivors.utils.*;
import com.acsurvivors.entities.systems.DebugRenderSystem;
import com.acsurvivors.entities.systems.ControlSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.HashSet;

import static com.acsurvivors.utils.Constants.TILE_SIZE;

public class GameScene extends BaseScene {
    private boolean hidden = true;

    private SpriteBatch batch;
    private EntityManager entityManager;
    private RenderingSystem renderingSystem;
    private MapLoader mapLoader;
    private ColliderManager colliderManager;
    private AssetManager assetManager;
    private SceneManager sceneManager;
    private ControlSystem controlSystem;
    private CustomOrthographicCamera camera;
    private EnemySpawner enemySpawner;
    private float spawnTimer = 0;  // Timer pentru spawnare inamici
    private static final float SPAWN_INTERVAL = 3f;

    //UI Components
    ProgressBar healthBar;

    public GameScene(SceneManager sceneManager, AssetManager assetManager){
        batch = new SpriteBatch();
        entityManager = new EntityManager();
        this.sceneManager = sceneManager;
        this.assetManager = assetManager;
        mapLoader = new MapLoader(assetManager);
        colliderManager = new ColliderManager(mapLoader);
        controlSystem = new ControlSystem(colliderManager);
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
        assetManager.loadTexture("player_idle_1", "sprites/player/player_idle_1.png");
        idleAnimation.addFrame(new TextureRegion(assetManager.getTexture("player_idle_1")));
        idleAnimation.addFrame(new TextureRegion(assetManager.getTexture("player_idle_2")));
        idleAnimation.addFrame(new TextureRegion(assetManager.getTexture("player_idle_3")));
        idleAnimation.addFrame(new TextureRegion(assetManager.getTexture("player_idle_4")));
        animatedSprite.addAnimation("idle", idleAnimation);

        AnimatedSpriteComponent.Animation walkAnimation = new AnimatedSpriteComponent.Animation(0.8f);
        walkAnimation.addFrame(new TextureRegion(assetManager.getTexture("player_walk_1")));
        walkAnimation.addFrame(new TextureRegion(assetManager.getTexture("player_walk_2")));
        walkAnimation.addFrame(new TextureRegion(assetManager.getTexture("player_walk_3")));

        animatedSprite.addAnimation("walk", walkAnimation);

        animatedSprite.setAnimation("idle");

        player.addComponent(AnimatedSpriteComponent.class, animatedSprite);
        ColliderComponent collider = new ColliderComponent(transform.x, transform.y, TILE_SIZE / 2, TILE_SIZE / 2);
        collider.changeOffset(16, 16);
        player.addComponent(ColliderComponent.class, collider);


        //Enemy


        camera.setPosition(centerX, centerY);

        renderingSystem = new RenderingSystem(batch, assetManager);

        camera.setWorldBounds(mapWidth, mapHeight);
        enemySpawner = new EnemySpawner(entityManager, assetManager, camera, mapLoader);

        //
        createUI();
    }

    @Override
    public void show() {
        hidden = false;
    }

    @Override
    public void pause() {
        hidden = true;
    }

    public void render(float delta) {
        if (hidden) {
            return;
        }

        ScreenUtils.clear(0f, 0f, 0f, 1f);

        delta = Gdx.graphics.getDeltaTime();

        //updates
        controlSystem.update(entityManager, delta);
        Entity player = entityManager.getEntities().get(0);
        TransformComponent playerTransform = player.getComponent(TransformComponent.class);
        camera.setPosition(playerTransform.x + TILE_SIZE / 2, playerTransform.y + TILE_SIZE / 2);

        camera.getCamera().update();

        renderingSystem.setProjectionMatrix(camera.getCamera().combined);
        renderingSystem.renderMap(mapLoader.getMapData(), TILE_SIZE);
        renderingSystem.render(entityManager);

        spawnTimer += delta;
        if (spawnTimer >= SPAWN_INTERVAL) {
            enemySpawner.spawnEnemyNearPlayer(playerTransform.x, playerTransform.y);
            spawnTimer = 0;
        }

        healthBar.draw(batch);
        //debugRenderSystem.render(entityManager);
    }

    @Override
    public void dispose() {
        batch.dispose();
        HashSet<Texture> disposedTextures = new HashSet<>();

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
    }

    private void createUI() {
        healthBar = new ProgressBar(20, 420, 80, 20, 0, 100, Color.RED, Color.GREEN, 2);

    }
}

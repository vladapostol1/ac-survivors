package com.acsurvivors.scenes;

import com.acsurvivors.entities.EnemySpawner;
import com.acsurvivors.entities.Entity;
import com.acsurvivors.entities.EntityManager;
import com.acsurvivors.entities.components.*;
import com.acsurvivors.ui.IUIElement;
import com.acsurvivors.ui.Label;
import com.acsurvivors.ui.ProgressBar;
import com.acsurvivors.utils.*;
import com.acsurvivors.entities.systems.DebugRenderSystem;
import com.acsurvivors.entities.systems.ControlSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.HashSet;

import static com.acsurvivors.utils.Constants.*;

public class GameScene extends BaseScene {
    private boolean hidden = true;

    private SpriteBatch batch;
    private SpriteBatch uiBatch;
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
    private Label timeLabel;
    private Label moneyLabel;
    private ProgressBar healthBar;
    private Label playerPos;
    private IUIElement[] uiElements;

    private int money = 0;
    private float timer = 60f;

    public GameScene(SceneManager sceneManager, AssetManager assetManager){
        batch = new SpriteBatch();
        uiBatch = new SpriteBatch();
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
        int mapWidth = TILE_SIZE * 32;
        int mapHeight = TILE_SIZE * 32;
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
        uiBatch.setProjectionMatrix(new Matrix4().idt());
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

    @Override
    public void render(float delta) {
        if (hidden) {
            return;
        }

        ScreenUtils.clear(0f, 0f, 0f, 1f);
        delta = Gdx.graphics.getDeltaTime();

        controlSystem.update(entityManager, delta);

        Entity player = entityManager.getEntities().get(0);
        TransformComponent playerTransform = player.getComponent(TransformComponent.class);
        camera.setPosition(playerTransform.x + TILE_SIZE / 2, playerTransform.y + TILE_SIZE / 2);
        camera.getCamera().update();

        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.X)) {
            playerPos.setActive(!playerPos.isActive());
        }

        if (playerPos.isActive()) {
            playerPos.text = String.format("X: %.0f, Y: %.0f", playerTransform.x, playerTransform.y);
            playerPos.transform.x = screenWidth - 200;
            playerPos.transform.y = screenHeight - 20;
        }

        timer -= delta;
        if (timer < 0) timer = 0;
        timeLabel.text = String.format("%.0f", timer);

        moneyLabel.text = "Gold: " + money;

        renderingSystem.setProjectionMatrix(camera.getCamera().combined);
        renderingSystem.renderMap(mapLoader.getMapData(), TILE_SIZE);
        renderingSystem.render(entityManager);
        renderingSystem.renderUI(uiElements);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    private void createUI() {
        healthBar = new ProgressBar(20, 420, 100, 20, 0, 100, Color.WHITE, Color.GREEN, 1);

        moneyLabel = new Label(
            "Gold: 0",
            Color.WHITE,
            assetManager.getFont("buttonFont"),
            new TransformComponent()
        );
        moneyLabel.transform.x = 20;
        moneyLabel.transform.y = screenHeight - 80;

        timeLabel = new Label(
            "60",
            Color.WHITE,
            assetManager.getFont("titleFont"),
            new TransformComponent()
        );
        float timeLabelWidth = new GlyphLayout(assetManager.getFont("titleFont"), "60").width;
        timeLabel.transform.x = (screenWidth - timeLabelWidth) / 2;
        timeLabel.transform.y = screenHeight - 20;

        playerPos = new Label(
            "X: , Y:",
            Color.WHITE,
            assetManager.getFont("buttonFont"),
            new TransformComponent()
        );
        playerPos.setActive(false);

        uiElements = new IUIElement[] { healthBar, moneyLabel, timeLabel, playerPos };
    }
}

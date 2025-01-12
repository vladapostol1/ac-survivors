package com.acsurvivors.scenes;

import com.acsurvivors.entities.EnemySpawner;
import com.acsurvivors.entities.Entity;
import com.acsurvivors.entities.EntityManager;
import com.acsurvivors.entities.components.*;
import com.acsurvivors.ui.*;
import com.acsurvivors.ui.extended.EmptySlot;
import com.acsurvivors.ui.extended.ShopItemBox;
import com.acsurvivors.utils.*;
import com.acsurvivors.entities.systems.DebugRenderSystem;
import com.acsurvivors.entities.systems.ControlSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.ScreenUtils;
import com.acsurvivors.ui.extended.Item;

import java.util.HashSet;
import java.util.List;

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
    private Panel endRoundMenu;
    private boolean menuActive = false;
    private IUIElement[] uiElements;

    private float timer = 60f;
    private Player playerInv;

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
        playerInv = new Player();

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

        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.Z)) {
            endRoundMenu.setActive(!endRoundMenu.isActive());
        }

        if (menuActive) {
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
            boolean isMousePressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);

            //endRoundMenu.update(mouseX, mouseY, isMousePressed);

            batch.begin();
            endRoundMenu.draw(batch);
            batch.end();

            return; // Skip the rest of the game logic
        }

        if (playerPos.isActive()) {
            playerPos.text = String.format("X: %.0f, Y: %.0f", playerTransform.x, playerTransform.y);
            playerPos.transform.x = screenWidth - 200;
            playerPos.transform.y = screenHeight - 20;
        }

        timer -= delta;
        if (timer < 0) timer = 0;
        timeLabel.text = String.format("%.0f", timer);

        moneyLabel.text = "Gold: " + playerInv.getGold();

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

        createEndRoundMenu(playerInv);
        uiElements = new IUIElement[] { healthBar, moneyLabel, timeLabel, playerPos, endRoundMenu};
    }

    private void createEndRoundMenu(Player player) {
        endRoundMenu = new Panel(0, 0);
        endRoundMenu.setSize(screenWidth, screenHeight);
        endRoundMenu.setBackgroundColor(Color.BLACK);

        Panel leftPanel = new Panel(0, 0);
        String shopTitleText = "SHOP";
        GlyphLayout layout = new GlyphLayout(assetManager.getFont("buttonFont"), shopTitleText);
        float shopTitleWidth = layout.width;
        float shopTitleX = (512 - shopTitleWidth) / 2;

        Label shopTitle = new Label(shopTitleText, Color.WHITE, assetManager.getFont("buttonFont"), new TransformComponent());
        shopTitle.setPosition(shopTitleX, screenHeight - 20);
        GridLayout shopGrid = new GridLayout(8, screenHeight - 120, 3, 2, 200, 80, 12);

        ItemData cheeseData = assetManager.getItem("item01");
        Texture cheeseTexture = assetManager.getTexture("item01");

        for (int i = 1; i <= 6; i++) {
            ShopItemBox cheeseShopItem = new ShopItemBox(
                cheeseData,
                cheeseTexture,
                0, 0, 250, 80,
                assetManager.getFont("textFont"),
                player,
                10
            );
            shopGrid.addChild(cheeseShopItem);
        }

        GridLayout inventoryGrid = new GridLayout(0, 0, 2, 8, 48, 48, 4);
        inventoryGrid.setPosition(8, 64);

        for (int i = 0; i < 16; i++) {
            if (i < player.getInventory().size()) {
                ItemData itemData = player.getInventory().get(i);
                inventoryGrid.addChild(new Item(
                    itemData,
                    assetManager.getTexture(itemData.getIconPath()),
                    0, 0, 48, 48,
                    assetManager.getFont("textFont")
                ));
            } else {
                inventoryGrid.addChild(new EmptySlot(0, 0, 48, 48, Color.DARK_GRAY));
            }
        }
        leftPanel.addChild(shopTitle);
        leftPanel.addChild(shopGrid);
        leftPanel.addChild(inventoryGrid);

        Panel rightPanel = new Panel(512, 0);

        Label statsLabel = new Label("Stats\nHP: 100\nAtk: 20", Color.WHITE, assetManager.getFont("textFont"), new TransformComponent());
        statsLabel.setPosition(10, 400);

        Button nextButton = new Button("Next Round", 10, 20, 108, 40, assetManager.getFont("textFont"), Color.WHITE, Color.DARK_GRAY, Color.LIGHT_GRAY);
        nextButton.setOnClick(() -> {
            System.out.println("Next round started!");
            endRoundMenu.setActive(false);
        });

        rightPanel.addChild(statsLabel);
        rightPanel.addChild(nextButton);

        endRoundMenu.addChild(leftPanel);
        endRoundMenu.addChild(rightPanel);
        endRoundMenu.setActive(false);
    }

}

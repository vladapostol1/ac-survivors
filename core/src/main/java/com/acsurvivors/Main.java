package com.acsurvivors;

import com.acsurvivors.entities.Entity;
import com.acsurvivors.entities.EntityManager;
import com.acsurvivors.entities.components.SpriteComponent;
import com.acsurvivors.entities.components.TransformComponent;
import com.acsurvivors.entities.components.ControlComponent;
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

public class Main extends ApplicationAdapter {
    public static final int TILE_SIZE = 64;

    private SpriteBatch batch;
    private EntityManager entityManager;
    private RenderingSystem renderingSystem;
    private MapLoader mapLoader;
    private AssetManager assetManager;
    private ControlSystem controlSystem;
    private CustomOrthographicCamera camera;


    @Override
    public void create() {
        batch = new SpriteBatch();
        entityManager = new EntityManager();
        assetManager = new AssetManager();
        mapLoader = new MapLoader(assetManager);
        camera = new CustomOrthographicCamera(640, 480);

        //Assets import
        assetManager.loadMapTextures("00");
        mapLoader.loadMap("maps/test_ground.txt");
        assetManager.loadTexture("player_idle.png");

        //Create player
        int mapWidth = mapLoader.getMapData()[0].length * 32;
        int mapHeight = mapLoader.getMapData().length * 32;
        int centerX = mapWidth / 2;
        int centerY = mapHeight / 2;

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

        renderingSystem = new RenderingSystem(batch, assetManager);

        //
        controlSystem = new ControlSystem();

        camera.setWorldBounds(mapWidth, mapHeight);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0f, 0f, 0f, 1f);

        float delta = Gdx.graphics.getDeltaTime();

        //updates
        controlSystem.update(entityManager, delta);
        Entity player = entityManager.getEntities().get(0); // Assuming the player is the first entity
        TransformComponent playerTransform = player.getComponent(TransformComponent.class);
        camera.setPosition(playerTransform.x + TILE_SIZE / 2, playerTransform.y + TILE_SIZE / 2);
        camera.getCamera().update();


        //renders
        renderingSystem.renderMap(mapLoader.getMapData(), TILE_SIZE);
        renderingSystem.render(entityManager);
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

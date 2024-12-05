package com.acsurvivors;

import com.acsurvivors.entities.Entity;
import com.acsurvivors.entities.EntityManager;
import com.acsurvivors.entities.components.*;
import com.acsurvivors.utils.RenderingSystem;
import com.acsurvivors.entities.systems.ControlSystem;
import com.acsurvivors.utils.AssetManager;
import com.acsurvivors.utils.CustomOrthographicCamera;
import com.acsurvivors.utils.MapLoader;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import static com.acsurvivors.utils.Constants.TILE_SIZE;

public class Main extends ApplicationAdapter {

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
        //Map tiles and map plan
        String[] tiles_name = {"00","01","02","03","04","05","06","07","08"};
        String[] tiles_path = {"tiles/00.png","tiles/01.png","tiles/02.png","tiles/03.png","tiles/04.png",
            "tiles/05.png","tiles/06.png","tiles/07.png","tiles/08.png"};
        assetManager.loadMultipleTextures(tiles_name, tiles_path);
        mapLoader.loadMap("maps/test_ground.txt");

        //Sprites
        assetManager.loadTexture("player_idle", "sprites/player_idle.png");

        //calc center of the map
        int mapWidth = mapLoader.getMapData()[0].length * 32;
        int mapHeight = mapLoader.getMapData().length * 32;
        int centerX = mapWidth / 2;
        int centerY = mapHeight / 2;

        //Create player
        Entity player = entityManager.createEntity();
        TransformComponent transform = new TransformComponent();
        transform.x = centerX;
        transform.y = centerY;
        player.addComponent(TransformComponent.class, transform);

        ControlComponent control = new ControlComponent();
        player.addComponent(ControlComponent.class, control);

        SpriteComponent sprite = new SpriteComponent();
        sprite.texture = assetManager.getTexture("player_idle");
        player.addComponent(SpriteComponent.class, sprite);

        ColliderComponent collider = new ColliderComponent(transform.x, transform.y, TILE_SIZE / 2, TILE_SIZE / 2);
        player.addComponent(ColliderComponent.class, collider);

        StatsComponent playerStats = new StatsComponent(10, 0, 1, 1,1);
        player.addComponent(StatsComponent.class, playerStats);

        LevelComponent playerLevel = new LevelComponent();
        player.addComponent(LevelComponent.class, playerLevel);

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
        Entity player = entityManager.getEntities().get(0);
        TransformComponent playerTransform = player.getComponent(TransformComponent.class);
        camera.setPosition(playerTransform.x + TILE_SIZE / 2, playerTransform.y + TILE_SIZE / 2);

        camera.getCamera().update();

        renderingSystem.setProjectionMatrix(camera.getCamera().combined);

        // Render the map and entities
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

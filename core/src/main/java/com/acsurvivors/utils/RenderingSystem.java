package com.acsurvivors.utils;

import com.acsurvivors.entities.Entity;
import com.acsurvivors.entities.EntityManager;
import com.acsurvivors.entities.components.AnimatedSpriteComponent;
import com.acsurvivors.entities.components.ColliderComponent;
import com.acsurvivors.entities.components.SpriteComponent;
import com.acsurvivors.entities.components.TransformComponent;
import com.acsurvivors.ui.IUIElement;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;

import static com.acsurvivors.utils.Constants.DEBUG_MODE;
import static com.acsurvivors.utils.Constants.TILE_SIZE;

public class RenderingSystem {
    private final SpriteBatch batch;
    private final SpriteBatch uiBatch;
    private final AssetManager assetManager;
    private final ShapeRenderer shapeRenderer;

    public RenderingSystem(SpriteBatch batch, AssetManager assetManager) {
        this.batch = batch;
        this.uiBatch = new SpriteBatch();
        this.assetManager = assetManager;
        this.shapeRenderer = new ShapeRenderer();
    }

    public void setProjectionMatrix(Matrix4 projectionMatrix) {
        batch.setProjectionMatrix(projectionMatrix);
        shapeRenderer.setProjectionMatrix(projectionMatrix);
    }

    public void render(EntityManager entityManager) {
        batch.begin();
        for (Entity entity : entityManager.getEntities()) {
            if (entity.hasComponent(TransformComponent.class)) {
                TransformComponent transform = entity.getComponent(TransformComponent.class);

                if (entity.hasComponent(AnimatedSpriteComponent.class)) {
                    AnimatedSpriteComponent animatedSprite = entity.getComponent(AnimatedSpriteComponent.class);
                    TextureRegion currentFrame = animatedSprite.getCurrentFrame(Gdx.graphics.getDeltaTime());

                    if (currentFrame != null) {
                        float width = TILE_SIZE * animatedSprite.scaleX;
                        float height = TILE_SIZE * animatedSprite.scaleY;

                        float x = transform.x + (TILE_SIZE - width) / 2;
                        float y = transform.y + (TILE_SIZE - height) / 2;

                        batch.draw(currentFrame,
                            x, y,
                            width, height);
                    }
                }
                else if (entity.hasComponent(SpriteComponent.class)) {
                    SpriteComponent sprite = entity.getComponent(SpriteComponent.class);

                    float width = TILE_SIZE * sprite.scale;
                    float height = TILE_SIZE * sprite.scale;

                    float x = transform.x + (TILE_SIZE - width) / 2;
                    float y = transform.y + (TILE_SIZE - height) / 2;

                    batch.draw(
                        sprite.textureRegion,
                        x, y,
                        width, height
                    );
                }
            }
        }
        batch.end();

        // Debug Mode for Colliders
        if (DEBUG_MODE) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(0, 1, 0, 1);
            for (Entity entity : entityManager.getEntities()) {
                if (entity.hasComponent(ColliderComponent.class)) {
                    ColliderComponent collider = entity.getComponent(ColliderComponent.class);
                    shapeRenderer.rect(
                        collider.bounds.x,
                        collider.bounds.y,
                        collider.bounds.width,
                        collider.bounds.height
                    );
                }
            }
            shapeRenderer.end();
        }
    }

    public void renderUI(IUIElement[] uiElements){
        for (IUIElement element : uiElements) {
            uiBatch.begin();
            if(element.isActive())
                element.draw(uiBatch);
            uiBatch.end();
        }
    }

    public void renderMap(String[][] mapData, int tileSize) {
        batch.begin();
        for (int row = 0; row < mapData.length; row++) {
            for (int col = 0; col < mapData[row].length; col++) {
                String tileKey = mapData[row][col];
                Texture texture = assetManager.getTexture(tileKey);
                if (texture != null) {
                    batch.draw(texture, col * tileSize, row * tileSize, tileSize, tileSize);
                }
            }
        }
        batch.end();
    }
}

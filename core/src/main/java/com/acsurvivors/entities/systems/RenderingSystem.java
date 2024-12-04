package com.acsurvivors.entities.systems;

import com.acsurvivors.entities.Entity;
import com.acsurvivors.entities.EntityManager;
import com.acsurvivors.entities.components.SpriteComponent;
import com.acsurvivors.entities.components.TransformComponent;
import com.acsurvivors.utils.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RenderingSystem {
    private final SpriteBatch batch;
    private final AssetManager assetManager;

    public RenderingSystem(SpriteBatch batch, AssetManager assetManager) {
        this.batch = batch;
        this.assetManager = assetManager;
    }

    public void render(EntityManager entityManager) {
        batch.begin();
        for (Entity entity : entityManager.getEntities()) {
            if (entity.hasComponent(TransformComponent.class) &&
                entity.hasComponent(SpriteComponent.class)) {

                TransformComponent transform = entity.getComponent(TransformComponent.class);
                SpriteComponent sprite = entity.getComponent(SpriteComponent.class);

                batch.draw(sprite.texture, transform.x, transform.y,
                    sprite.texture.getWidth() * transform.scaleX,
                    sprite.texture.getHeight() * transform.scaleY);
            }
        }
        batch.end();
    }

    public void renderMap(String[][] mapData, int tileSize) {
        batch.begin();
        for (int row = 0; row < mapData.length; row++) {
            for (int col = 0; col < mapData[row].length; col++) {
                String tileKey = mapData[row][col];
                Texture texture = assetManager.getTexture(tileKey + ".png");
                if (texture != null) {
                    batch.draw(texture, col * tileSize, row * tileSize, tileSize, tileSize);
                }
            }
        }
        batch.end();
    }


}


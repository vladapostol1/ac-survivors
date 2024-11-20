package com.acsurvivors.entities.systems;

import com.acsurvivors.entities.Entity;
import com.acsurvivors.entities.EntityManager;
import com.acsurvivors.entities.components.SpriteComponent;
import com.acsurvivors.entities.components.TransformComponent;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RenderingSystem {
    private final SpriteBatch batch;

    public RenderingSystem(SpriteBatch batch) {
        this.batch = batch;
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
}


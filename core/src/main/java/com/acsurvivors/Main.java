package com.acsurvivors;

import com.acsurvivors.entities.Entity;
import com.acsurvivors.entities.EntityManager;
import com.acsurvivors.entities.components.SpriteComponent;
import com.acsurvivors.entities.components.TransformComponent;
import com.acsurvivors.entities.systems.RenderingSystem;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private EntityManager entityManager;
    private RenderingSystem renderingSystem;

    @Override
    public void create() {
        batch = new SpriteBatch();
        entityManager = new EntityManager();
        renderingSystem = new RenderingSystem(batch);

        // Create an entity
        Entity entity = entityManager.createEntity();
        entity.addComponent(TransformComponent.class, new TransformComponent());
        SpriteComponent spriteComponent = new SpriteComponent();
        spriteComponent.texture = new Texture("libgdx.png");
        entity.addComponent(SpriteComponent.class, spriteComponent);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0f, 0f, 0f, 1f);

        // Update and render
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

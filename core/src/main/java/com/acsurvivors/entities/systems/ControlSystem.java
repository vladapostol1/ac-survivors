package com.acsurvivors.entities.systems;

import com.acsurvivors.entities.Entity;
import com.acsurvivors.entities.EntityManager;
import com.acsurvivors.entities.components.ControlComponent;
import com.acsurvivors.entities.components.ColliderComponent;
import com.acsurvivors.entities.components.TransformComponent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class ControlSystem {
    public void update(EntityManager entityManager, float delta) {
        for (Entity entity : entityManager.getEntities()) {
            if (entity.hasComponent(ControlComponent.class) &&
                entity.hasComponent(TransformComponent.class)) {

                ControlComponent control = entity.getComponent(ControlComponent.class);
                TransformComponent transform = entity.getComponent(TransformComponent.class);

                control.velocity.set(0, 0);

                if (Gdx.input.isKeyPressed(Input.Keys.W)) control.velocity.y = control.speed;
                if (Gdx.input.isKeyPressed(Input.Keys.S)) control.velocity.y = -control.speed;
                if (Gdx.input.isKeyPressed(Input.Keys.A)) control.velocity.x = -control.speed;
                if (Gdx.input.isKeyPressed(Input.Keys.D)) control.velocity.x = control.speed;

                transform.x += control.velocity.x * delta;
                transform.y += control.velocity.y * delta;

                if (entity.hasComponent(ColliderComponent.class)) {
                    ColliderComponent collider = entity.getComponent(ColliderComponent.class);
                    collider.updatePosition(transform.x, transform.y);
                }
            }
        }
    }
}

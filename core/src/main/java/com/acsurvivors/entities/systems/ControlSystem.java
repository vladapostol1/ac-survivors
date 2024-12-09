package com.acsurvivors.entities.systems;

import com.acsurvivors.entities.Entity;
import com.acsurvivors.entities.EntityManager;
import com.acsurvivors.entities.components.AnimatedSpriteComponent;
import com.acsurvivors.entities.components.ControlComponent;
import com.acsurvivors.entities.components.ColliderComponent;
import com.acsurvivors.entities.components.TransformComponent;
import com.acsurvivors.utils.ColliderManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class ControlSystem {
    private final ColliderManager colliderManager;

    public ControlSystem(ColliderManager colliderManager) {
        this.colliderManager = colliderManager;
    }

    public void update(EntityManager entityManager, float delta) {
        for (Entity entity : entityManager.getEntities()) {
            if (entity.hasComponent(ControlComponent.class) &&
                entity.hasComponent(TransformComponent.class) &&
                entity.hasComponent(ColliderComponent.class)) {

                ControlComponent control = entity.getComponent(ControlComponent.class);
                TransformComponent transform = entity.getComponent(TransformComponent.class);
                ColliderComponent collider = entity.getComponent(ColliderComponent.class);

                control.velocity.set(0, 0);

                boolean isMoving = false;

                // Detect input for movement
                if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                    control.velocity.y += control.speed;
                    isMoving = true;
                }
                if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                    control.velocity.y -= control.speed;
                    isMoving = true;
                }
                if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                    control.velocity.x -= control.speed;
                    isMoving = true;
                }
                if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                    control.velocity.x += control.speed;
                    isMoving = true;
                }

                // Normalize velocity if exceeding max speed
                if (control.velocity.len() > control.speed) {
                    control.velocity.nor().scl(control.speed);
                }

                float newX = transform.x + control.velocity.x * delta;
                float newY = transform.y + control.velocity.y * delta;

                if (!colliderManager.isCollidingWithMap(collider, newX, newY)) {
                    collider.updatePosition(newX, newY);
                    transform.x = newX;
                    transform.y = newY;
                }

                if (entity.hasComponent(AnimatedSpriteComponent.class)) {
                    AnimatedSpriteComponent animatedSprite = entity.getComponent(AnimatedSpriteComponent.class);
                    if (isMoving) {
                        animatedSprite.setAnimation("walk");
                    } else {
                        animatedSprite.setAnimation("idle");
                    }

                    if (control.velocity.x > 0) animatedSprite.flipX = false;
                    else if (control.velocity.x < 0) animatedSprite.flipX = true;
                }
            }
        }
    }
}

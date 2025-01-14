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
        TransformComponent transform = null;
        ColliderComponent collider = null;
        for (Entity entity : entityManager.getEntities()) {
            if (entity.hasComponent(ControlComponent.class) &&
                entity.hasComponent(TransformComponent.class) &&
                entity.hasComponent(ColliderComponent.class)) {

                ControlComponent control = entity.getComponent(ControlComponent.class);
                transform = entity.getComponent(TransformComponent.class);
                collider = entity.getComponent(ColliderComponent.class);

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
        if (transform == null) return;

        // Actualizam inamicii pentru a urmari player-ul
        for (Entity entity : entityManager.getEntities()) {
            if (entity.hasComponent(TransformComponent.class) &&
                    !entity.hasComponent(ControlComponent.class) && entity.hasComponent(ColliderComponent.class)) {
                TransformComponent enemyTransform = entity.getComponent(TransformComponent.class);
                ColliderComponent enemyCollider = entity.getComponent((ColliderComponent.class));

                TransformComponent enemyTransform1 = entity.getComponent(TransformComponent.class);
                ColliderComponent enemyCollider1 = entity.getComponent(ColliderComponent.class);

                float deltaX = transform.x - enemyTransform.x;
                float deltaY = transform.y - enemyTransform.y;

                float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                float speed = 100 * delta;

                // Muta inamicul catre player
                if (distance > 0 && !enemyCollider.isCollidingWith(collider)) {
                    float proposedX = enemyTransform.x + (deltaX / distance) * speed;
                    float proposedY = enemyTransform.y + (deltaY / distance) * speed;

                    if (!colliderManager.isCollidingWithMap(enemyCollider, proposedX, enemyTransform.y)) {
                        enemyCollider.updatePosition(proposedX, enemyTransform.y);
                        enemyTransform.x = proposedX;
                    }
                    if (!colliderManager.isCollidingWithMap(enemyCollider, enemyTransform.x, proposedY)) {
                        enemyCollider.updatePosition(enemyTransform.x, proposedY);
                        enemyTransform.y = proposedY;
                    }
                }

                if(enemyCollider.isCollidingWith(collider))
                {
                    // Resolve collision by pushing the enemy away from the player
                    float overlapX = enemyTransform.x - transform.x;
                    float overlapY = enemyTransform.y - transform.y;

                    float overlapDistance = (float) Math.sqrt(overlapX * overlapX + overlapY * overlapY);

                    if (overlapDistance > 0) {
                        float pushFactor = 2.0f; // Player fully pushes the enemy
                        float separationX = (overlapX / overlapDistance) * pushFactor;
                        float separationY = (overlapY / overlapDistance) * pushFactor;

                        enemyTransform.x += separationX;
                        enemyTransform.y += separationY;
                        enemyCollider.updatePosition(enemyTransform.x, enemyTransform.y);
                    }

                    // Stop enemy from actively moving toward the player
                    speed = 0;
                }

                // Handle enemy collisions with other enemies
                for (Entity otherEntity : entityManager.getEntities()) {
                    if (otherEntity != entity &&
                            otherEntity.hasComponent(ColliderComponent.class) &&
                            otherEntity.hasComponent(TransformComponent.class) &&
                            !otherEntity.hasComponent(ControlComponent.class)) {

                        ColliderComponent otherCollider = otherEntity.getComponent(ColliderComponent.class);
                        TransformComponent otherTransform = otherEntity.getComponent(TransformComponent.class);

                        if (enemyCollider.isCollidingWith(otherCollider)) {
                            // Resolve collision by pushing enemies apart
                            float overlapX = (enemyTransform.x - otherTransform.x);
                            float overlapY = (enemyTransform.y - otherTransform.y);

                            float overlapDistance = (float) Math.sqrt(overlapX * overlapX + overlapY * overlapY);

                            if (overlapDistance > 0) {
                                float resolveFactor = 0.5f; // Distribute movement equally between the two colliding enemies
                                float separationX = (overlapX / overlapDistance) * resolveFactor;
                                float separationY = (overlapY / overlapDistance) * resolveFactor;

                                // Adjust positions to resolve collision
                                enemyTransform.x += separationX;
                                enemyTransform.y += separationY;
                                enemyCollider.updatePosition(enemyTransform.x, enemyTransform.y);

                                otherTransform.x -= separationX;
                                otherTransform.y -= separationY;
                                otherCollider.updatePosition(otherTransform.x, otherTransform.y);
                            }
                        }
                    }
                }

            }
        }



    }
}

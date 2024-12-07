package com.acsurvivors.entities.systems;

import com.acsurvivors.entities.Entity;
import com.acsurvivors.entities.EntityManager;
import com.acsurvivors.entities.components.AnimatedSpriteComponent;
import com.acsurvivors.entities.components.ControlComponent;
import com.acsurvivors.entities.components.ColliderComponent;
import com.acsurvivors.entities.components.TransformComponent;
import com.acsurvivors.utils.MapLoader; // Import MapLoader pentru verificarea tile-urilor
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;

public class ControlSystem {
    private MapLoader mapLoader;

    public ControlSystem(MapLoader mapLoader) {
        this.mapLoader = mapLoader;
    }

    public void update(EntityManager entityManager, float delta) {
        for (Entity entity : entityManager.getEntities()) {
            if (entity.hasComponent(ControlComponent.class) &&
                entity.hasComponent(TransformComponent.class)) {

                ControlComponent control = entity.getComponent(ControlComponent.class);
                TransformComponent transform = entity.getComponent(TransformComponent.class);

                control.velocity.set(0, 0);

                boolean isMoving = false;

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

                if (control.velocity.len() > control.speed) {
                    control.velocity.nor().scl(control.speed);
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

                if (entity.hasComponent(ColliderComponent.class)) {
                    ColliderComponent collider = entity.getComponent(ColliderComponent.class);
                    collider.updatePosition(transform.x, transform.y);

                    float proposedX = transform.x + control.velocity.x * delta;
                    float proposedY = transform.y + control.velocity.y * delta;

                    if (!isCollidingWithMap(proposedX, transform.y, collider)) {
                        transform.x = proposedX;
                    }
                    if (!isCollidingWithMap(transform.x, proposedY, collider)) {
                        transform.y = proposedY;
                    }
                }

            }
        }
        /*
        if (transform == null) return;

        // Actualizam inamicii pentru a urmari player-ul
        for (Entity entity : entityManager.getEntities()) {
            if (entity.hasComponent(TransformComponent.class) &&
                    !entity.hasComponent(ControlComponent.class)) {
                TransformComponent enemyTransform = entity.getComponent(TransformComponent.class);

                float deltaX = transform.x - enemyTransform.x;
                float deltaY = transform.y - enemyTransform.y;

                float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                float speed = 100 * delta;

                // Muta inamicul catre player
                if (distance > 0) {
                    float proposedX = enemyTransform.x + (deltaX / distance) * speed;
                    float proposedY = enemyTransform.y + (deltaY / distance) * speed;

                    if (!isCollidingWithMap(proposedX, enemyTransform.y, null)) {
                        enemyTransform.x = proposedX;
                    }
                    if (!isCollidingWithMap(enemyTransform.x, proposedY, null)) {
                        enemyTransform.y = proposedY;
                    }
                }
            }
        }*/
    }

    private boolean isCollidingWithMap(float x, float y, ColliderComponent collider) {
        // Calculează poziția în tile-uri
        int tileX = (int) (x / 64);
        int tileY = (int) (y / 64);

        // Verifică coliziunea cu fiecare colț al dreptunghiului de coliziune
        return mapLoader.isTileSolid(tileX, tileY) ||                         // Stânga-sus
                mapLoader.isTileSolid(tileX + 1, tileY) ||                     // Dreapta-sus
                mapLoader.isTileSolid(tileX, tileY + 1) ||                     // Stânga-jos
                mapLoader.isTileSolid(tileX + 1, tileY + 1);                   // Dreapta-jos
    }

}

package com.acsurvivors.entities.systems;

import com.acsurvivors.entities.Entity;
import com.acsurvivors.entities.EntityManager;
import com.acsurvivors.entities.components.ColliderComponent;
import com.acsurvivors.entities.components.ControlComponent;
import com.acsurvivors.entities.components.TransformComponent;
import java.util.List;

public class CollisionSystem {

    public void update(EntityManager entityManager) {
        List<Entity> entities = entityManager.getEntities();

        for (int i = 0; i < entities.size(); i++) {
            Entity entityA = entities.get(i);

            if (!entityA.hasComponent(ColliderComponent.class)) continue;

            ColliderComponent colliderA = entityA.getComponent(ColliderComponent.class);

            for (int j = i + 1; j < entities.size(); j++) {
                Entity entityB = entities.get(j);

                if (!entityB.hasComponent(ColliderComponent.class)) continue;

                ColliderComponent colliderB = entityB.getComponent(ColliderComponent.class);

                // Verificam coliziunea dintre doua entitati
                if (colliderA.isCollidingWithEntity(colliderB)) {
                    handleCollision(entityA, entityB);
                }
            }
        }
    }

    private void handleCollision(Entity entityA, Entity entityB) {
        // Coliziune intre Player si Inamic
        if (entityA.hasComponent(ControlComponent.class) || entityB.hasComponent(ControlComponent.class)) {
            System.out.println("Player colideaza cu un inamic");
            // De exemplu: reduce viata playerului
        }

        // Coliziune intre doi inamici
        if (!entityA.hasComponent(ControlComponent.class) && !entityB.hasComponent(ControlComponent.class)) {
            System.out.println("Inamic colideaza cu alt inamic");

        }
    }
}

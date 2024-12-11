package com.acsurvivors.utils;

import com.acsurvivors.entities.Entity;
import com.acsurvivors.entities.components.ColliderComponent;
import com.acsurvivors.utils.MapLoader;

import java.util.List;

import static com.acsurvivors.utils.Constants.TILE_SIZE;

public class ColliderManager {
    private final MapLoader mapLoader;

    public ColliderManager(MapLoader mapLoader) {
        this.mapLoader = mapLoader;
    }

    public boolean isCollidingWithMap(ColliderComponent collider, float x, float y) {
        // Calculează poziția în tile-uri

        // Trb sa cautam o solutie mai dinamica sau schimbat tile-urile de tot
        int tileX = (int) (x / (TILE_SIZE / 1.5f));
        int tileY = (int) (y / (TILE_SIZE / 1.5f));

        // Verifică coliziunea cu fiecare colț al dreptunghiului de coliziune
        return mapLoader.isTileSolid(tileX, tileY) ||                         // Stânga-sus
            mapLoader.isTileSolid(tileX + 1, tileY) ||                     // Dreapta-sus
            mapLoader.isTileSolid(tileX, tileY + 1) ||                     // Stânga-jos
            mapLoader.isTileSolid(tileX + 1, tileY + 1);                   // Dreapta-jos
    }

    public Entity getCollidingEntity(Entity source, List<Entity> entities) {
        ColliderComponent sourceCollider = source.getComponent(ColliderComponent.class);
        if (sourceCollider == null) return null;

        for (Entity entity : entities) {
            if (entity == source) continue; // Skip self
            if (entity.hasComponent(ColliderComponent.class)) {
                ColliderComponent otherCollider = entity.getComponent(ColliderComponent.class);
                if (sourceCollider.isCollidingWith(otherCollider)) {
                    return entity;
                }
            }
        }
        return null; // No collision
    }
    public boolean isCollidingWithAny(Entity source, List<Entity> entities) {
        return getCollidingEntity(source, entities) != null;
    }
}
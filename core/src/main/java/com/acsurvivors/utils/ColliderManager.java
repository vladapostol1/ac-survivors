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

    public boolean isCollidingWithMap(ColliderComponent collider, float futureX, float futureY) {
        float newX = futureX + collider.offsetX;
        float newY = futureY + collider.offsetY;

        float width = collider.bounds.width;
        float height = collider.bounds.height;

        return mapLoader.isTileSolid((int) (newX / TILE_SIZE), (int) (newY / TILE_SIZE)) ||
            mapLoader.isTileSolid((int) ((newX + width) / TILE_SIZE), (int) (newY / TILE_SIZE)) ||
            mapLoader.isTileSolid((int) (newX / TILE_SIZE), (int) ((newY + height) / TILE_SIZE)) ||
            mapLoader.isTileSolid((int) ((newX + width) / TILE_SIZE), (int) ((newY + height) / TILE_SIZE));
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

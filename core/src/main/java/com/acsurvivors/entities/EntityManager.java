package com.acsurvivors.entities;
import java.util.ArrayList;
import java.util.List;

public class EntityManager {
    private final List<Entity> entities = new ArrayList<>();
    private int nextId = 0;

    public Entity createEntity() {
        Entity entity = new Entity(nextId++);
        entities.add(entity);
        return entity;
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public List<Entity> getEntities() {
        return entities;
    }
}


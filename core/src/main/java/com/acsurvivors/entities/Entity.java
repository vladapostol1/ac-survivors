package com.acsurvivors.entities;
import java.util.HashMap;
import java.util.Map;

public class Entity {
    private final int id;
    private final Map<Class<?>, Object> components = new HashMap<>();

    public Entity(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public <T> void addComponent(Class<T> componentClass, T component) {
        components.put(componentClass, component);
    }

    public <T> T getComponent(Class<T> componentClass) {
        return componentClass.cast(components.get(componentClass));
    }

    public <T> boolean hasComponent(Class<T> componentClass) {
        return components.containsKey(componentClass);
    }
}

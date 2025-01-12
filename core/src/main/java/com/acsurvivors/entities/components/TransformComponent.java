package com.acsurvivors.entities.components;

public class TransformComponent {
    public float x, y;
    public float scaleX = 1f, scaleY = 1f;
    public float rotation = 0f;

    public TransformComponent(){}
    public TransformComponent(float x, float y){
        this.x = x;
        this.y = y;
    }
}

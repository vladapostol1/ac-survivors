package com.acsurvivors.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface IUIElement {
    void draw(SpriteBatch batch);
    void dispose();
}

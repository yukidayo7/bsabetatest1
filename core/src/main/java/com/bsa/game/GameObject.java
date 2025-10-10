package com.bsa.game;

import com.badlogic.gdx.math.Vector2;

public abstract class GameObject {
    protected Vector2 pos;
    protected String name;

    public GameObject(float x, float y, String name) {
        this.pos = new Vector2(x, y);
        this.name = name;
    }

    public Vector2 getPos() {
        return pos;
    }

    public String getName() {
        return name;
    }

    public abstract void update(float delta);
}

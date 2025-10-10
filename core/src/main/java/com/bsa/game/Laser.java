package com.bsa.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Color;

public class Laser {
    private Vector2 start;
    private Vector2 end;
    private float lifeTime;
    private float duration = 0.2f; // durará 0.2 segundos (frecuencia visual)
    private Color color;

    public Laser(Vector2 start, Vector2 end, Color color) {
        this.start = new Vector2(start);
        this.end = new Vector2(end);
        this.color = color;
        this.lifeTime = 0f;
    }

    public void update(float delta) {
        lifeTime += delta;
    }

    public boolean isAlive() {
        return lifeTime < duration;
    }

    public Vector2 getStart() { return start; }
    public Vector2 getEnd() { return end; }
    public Color getColor() { return color; }
}

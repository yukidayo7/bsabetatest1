package com.bsa.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Projectile {
    private final Vector2 pos;
    private final Vector2 target;
    private final Vector2 velocity;
    private final float speed = 1200f; // px/s
    private float life = 0f;
    private final float maxLife = 3f;
    private final Color color;
    private final float radius = 2.5f;

    public Projectile(Vector2 start, Vector2 target, Color color) {
        this.pos = new Vector2(start);
        this.target = new Vector2(target);
        Vector2 dir = new Vector2(this.target).sub(this.pos);
        float len = dir.len();
        if (len == 0) {
            this.velocity = new Vector2(0, 0);
        } else {
            this.velocity = dir.scl(1f / len).scl(speed);
        }
        this.color = color;
    }

    public void update(float delta) {
        pos.mulAdd(velocity, delta);
        life += delta;
    }

    public boolean isAlive() {
        if (life > maxLife) return false;
        float hitDistSq = (radius + 4f) * (radius + 4f);
        return pos.dst2(target) > hitDistSq;
    }

    public Vector2 getPos() { return pos; }
    public Color getColor() { return color; }
    public float getRadius() { return radius; }
}

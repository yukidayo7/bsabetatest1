package com.bsa.game;

import com.badlogic.gdx.math.Vector2;
import com.bsa.game.data.GameContentLoader;
import com.bsa.game.data.ShipType;

/**
 * Player (nave) - movimiento inmediato al target.
 * Soporta bloqueo de rotación (para mantener orientación cuando atacas).
 */
public class Player {
    private Vector2 pos;
    private Vector2 target;
    private Vector2 direction;

    // datos del tipo de nave
    private ShipType shipType;

    private float speed; // velocidad en px/s
    private float size = 20f;
    private float rotation; // grados
    private boolean moving;
    private boolean rotationLocked = false;

    public Player(float x, float y) {
        pos = new Vector2(x, y);
        target = new Vector2(x, y);
        direction = new Vector2(0, 1);
        rotation = 0f;
        moving = false;

        // Cargar el tipo de nave desde JSON
        shipType = GameContentLoader.getShip("goliath"); // tipo por defecto
        if (shipType != null) {
            this.speed = shipType.speed;
        } else {
            this.speed = 400f; // valor de respaldo
        }
    }

    public void update(float delta) {
        if (!moving) return;

        float distance = pos.dst(target);
        float step = speed * delta;

        if (distance <= step) {
            pos.set(target);
            moving = false;
        } else {
            pos.x += direction.x * step;
            pos.y += direction.y * step;
        }

        if (!rotationLocked) {
            rotation = direction.angleDeg() - 90f;
        }
    }

    public void setTarget(float x, float y) {
        target.set(x, y);
        direction.set(target).sub(pos).nor();
        moving = true;
        rotationLocked = false;
    }

    public void setRotation(float degrees) {
        rotation = degrees;
        rotationLocked = true;
    }

    public void clearRotationLock() {
        rotationLocked = false;
    }

    public Vector2 getPos() {
        return pos;
    }

    public float getRotation() {
        return rotation;
    }

    public float getSize() {
        return size;
    }

    public boolean isMoving() {
        return moving;
    }

    public ShipType getShipType() {
        return shipType;
    }
}

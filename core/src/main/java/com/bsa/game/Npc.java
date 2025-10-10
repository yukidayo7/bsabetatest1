package com.bsa.game;

import com.badlogic.gdx.math.Vector2;

public class Npc extends GameObject {
    private float health;
    private float maxHealth;
    private float size;
    private boolean dead;

    // Recompensas
    private int rewardXP;
    private int rewardCredits;
    private int rewardUridium;
    private int rewardHonor;

    public Npc(float x, float y, String name) {
        super(x, y, name);
        this.size = 40f;
        this.dead = false;

        switch (name.toLowerCase()) {
            case "streuner":
                this.maxHealth = 400;
                this.rewardXP = 100;
                this.rewardCredits = 100;
                this.rewardUridium = 5;
                this.rewardHonor = 2;
                break;
            case "lordakia":
                this.maxHealth = 800;
                this.rewardXP = 200;
                this.rewardCredits = 300;
                this.rewardUridium = 10;
                this.rewardHonor = 4;
                break;
            case "sibelon":
                this.maxHealth = 5000;
                this.rewardXP = 1000;
                this.rewardCredits = 1500;
                this.rewardUridium = 25;
                this.rewardHonor = 10;
                break;
            default:
                this.maxHealth = 1000;
                this.rewardXP = 100;
                this.rewardCredits = 100;
                this.rewardUridium = 5;
                this.rewardHonor = 2;
                break;
        }

        this.health = this.maxHealth;
    }

    @Override
    public void update(float delta) {
        // NPC estático por ahora
    }

    public boolean isClicked(float x, float y) {
        float dx = x - pos.x;
        float dy = y - pos.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance <= size / 2f;
    }

    public void takeDamage(int amount) {
        if (dead) return;
        health -= amount;
        if (health <= 0) {
            health = 0;
            dead = true;
        }
    }

    public boolean isDead() {
        return dead;
    }

    public float getHealth() {
        return health;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public float getSize() {
        return size;
    }

    public int getRewardXP() {
        return rewardXP;
    }

    public int getRewardCredits() {
        return rewardCredits;
    }

    public int getRewardUridium() {
        return rewardUridium;
    }

    public int getRewardHonor() {
        return rewardHonor;
    }
}

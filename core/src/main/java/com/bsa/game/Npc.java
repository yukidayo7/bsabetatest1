package com.bsa.game;

import com.badlogic.gdx.math.Vector2;
import com.bsa.game.data.GameContentLoader;
import com.bsa.game.data.NpcType;

/**
 * NPC con soporte para:
 * - Datos desde JSON (hp, recompensas, etc.)
 * - Respawn (controlado desde NpcController)
 * - Patrulla (controlado desde NpcController)
 *
 * Mantiene compatibilidad con:
 * - isClicked(...) para selección
 * - takeDamage(...) para el sistema de ataque
 * - getters usados por la UI
 */
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

    // ====== Campos internos usados por la IA / respawn (NpcController / MapManager) ======
    // zona de patrulla en la que respawnea
    public float __zoneX, __zoneY, __zoneR;
    // patrulla
    public boolean __hasPatrolTarget = false;
    public Vector2 __patrolTarget = new Vector2();
    // respawn
    public float __deadTimeSec = 0f;
    public float __respawnDelaySec = 10f; // se sobreescribe desde NpcType.respawnDelaySec

    public Npc(float x, float y, String name) {
        super(x, y, name);
        this.size = 40f;
        this.dead = false;

        // Cargar datos desde JSON
        NpcType npcType = GameContentLoader.getNpc(name.toLowerCase());
        if (npcType != null) {
            this.maxHealth = npcType.hp;
            this.rewardXP = npcType.rewardXP;
            this.rewardCredits = npcType.rewardCredits;
            this.rewardUridium = npcType.rewardUridium;
            this.rewardHonor = npcType.rewardHonor;
            this.__respawnDelaySec = npcType.respawnDelaySec; // nuevo
        } else {
            // Fallback legacy (por si falte en JSON)
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
        }
        this.health = this.maxHealth;
    }

    @Override
    public void update(float delta) {
        // La IA y el respawn se gestionan desde NpcController.
        // Dejamos este método vacío para mantener compatibilidad con tu loop actual.
    }

    /** Click seleccionable para tu sistema de ataque */
    public boolean isClicked(float x, float y) {
        float dx = x - pos.x;
        float dy = y - pos.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance <= size / 2f;
    }

    /** Daño desde tu sistema de disparo/ataque */
    public void takeDamage(int amount) {
        if (dead) return;
        health -= amount;
        if (health <= 0) {
            health = 0;
            dead = true;
            __deadTimeSec = 0f; // comienza el timer de respawn
        }
    }

    // ===== Getters usados por UI y lógica existente =====
    public boolean isDead() { return dead; }
    public float getHealth() { return health; }
    public float getMaxHealth() { return maxHealth; }
    public float getSize() { return size; }
    public int getRewardXP() { return rewardXP; }
    public int getRewardCredits() { return rewardCredits; }
    public int getRewardUridium() { return rewardUridium; }
    public int getRewardHonor() { return rewardHonor; }

    // ===== Helpers para la IA (invocados por NpcController) =====
    public Vector2 getPos() { return pos; }

    public void __setPatrolTarget(float x, float y) {
        __patrolTarget.set(x, y);
        __hasPatrolTarget = true;
    }

    public void __moveBy(float dx, float dy) {
        pos.x += dx;
        pos.y += dy;
    }

    public void __clampTo(float minX, float minY, float maxX, float maxY) {
        pos.x = Math.max(minX, Math.min(pos.x, maxX));
        pos.y = Math.max(minY, Math.min(pos.y, maxY));
    }

    public void __respawnAt(float x, float y) {
        pos.set(x, y);
        health = maxHealth;
        dead = false;
        __deadTimeSec = 0f;
        __hasPatrolTarget = false; // elegirá nuevo destino de patrulla
    }
}


package com.bsa.game.world;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.bsa.game.Npc;
import com.bsa.game.data.GameContentLoader;
import com.bsa.game.data.NpcType;

/**
 * Controlador de IA para NPCs:
 * - Patrulla en su zona (puntos aleatorios, movimiento en línea recta)
 * - Persecución si es agresivo y el jugador está en rango
 * - Respawn tras morir, con delay configurado por tipo
 */
public class NpcController {

    public void updateAI(Npc n, float delta, Vector2 playerPos, int mapW, int mapH) {
        // 1) Respawn por tiempo
        if (n.isDead()) {
            if (n.__deadTimeSec >= n.__respawnDelaySec) {
                float rx = n.__zoneX + MathUtils.randomTriangular(-n.__zoneR, n.__zoneR);
                float ry = n.__zoneY + MathUtils.randomTriangular(-n.__zoneR, n.__zoneR);
                n.__respawnAt(rx, ry);
            } else {
                n.__deadTimeSec += delta;
            }
            return;
        }

        // 2) Datos del tipo de NPC
        NpcType t = GameContentLoader.getNpc(n.getName().toLowerCase());
        if (t == null) return;

        // 3) Elegir destino de patrulla si no tiene
        if (!n.__hasPatrolTarget) {
            float rx = n.__zoneX + MathUtils.randomTriangular(-n.__zoneR, n.__zoneR);
            float ry = n.__zoneY + MathUtils.randomTriangular(-n.__zoneR, n.__zoneR);
            n.__setPatrolTarget(rx, ry);
        }

        // 4) ¿Perseguir al jugador?
        boolean chase = false;
        if (t.aggressive && playerPos != null) {
            float dist2 = playerPos.dst2(n.getPos());
            chase = dist2 <= (t.attackRange * t.attackRange);
        }

        Vector2 dest = chase ? playerPos : n.__patrolTarget;
        Vector2 dir = new Vector2(dest).sub(n.getPos());
        float dist = dir.len();
        if (dist > 1f) {
            dir.scl(1f / dist);
            float step = t.speed * delta;
            n.__moveBy(dir.x * step, dir.y * step);
        } else {
            // Llegó al punto de patrulla → elegir otro
            if (!chase) n.__hasPatrolTarget = false;
        }

        // 5) Limitar a los bordes del mapa
        n.__clampTo(0, 0, mapW, mapH);
    }
}

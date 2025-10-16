package com.bsa.game.world;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.bsa.game.Npc;
import com.bsa.game.data.GameMap;
import com.bsa.game.data.NpcSpawnZone;
import com.bsa.game.data.NpcSpawnZone.NpcSpawnType;

import java.util.ArrayList;
import java.util.List;

/**
 * Administra un mapa específico:
 * - Crea NPCs iniciales según zonas (maps.json)
 * - Actualiza IA de todos sus NPCs
 */
public class MapManager {
    private final GameMap map;
    private final List<Npc> npcs = new ArrayList<>();
    private final NpcController npcController = new NpcController();

    public MapManager(GameMap map) {
        this.map = map;
    }

    /** Genera TODOS los NPCs según las zonas y cantidades del JSON */
    public void spawnAll() {
        npcs.clear();

        for (NpcSpawnZone zone : map.npcZones) {
            for (NpcSpawnType type : zone.types) {
                for (int i = 0; i < type.count; i++) {
                    float rx = zone.x + MathUtils.random(zone.width);
                    float ry = zone.y + MathUtils.random(zone.height);
                    Npc n = new Npc(rx, ry, type.id);

                    // Guardar información de la zona (para respawn y patrulla)
                    n.__zoneX = zone.x + zone.width / 2f;
                    n.__zoneY = zone.y + zone.height / 2f;
                    n.__zoneR = Math.max(zone.width, zone.height) / 2f; // radio de patrulla aproximado

                    npcs.add(n);
                }
            }
        }
    }

    /** Actualiza IA de NPCs (patrulla, persecución, respawn) */
    public void update(float delta, Vector2 playerPos) {
        for (Npc n : npcs) {
            npcController.updateAI(n, delta, playerPos, map.width, map.height);
        }
    }

    public List<Npc> getNpcs() {
        return npcs;
    }

    public GameMap getMap() {
        return map;
    }
}

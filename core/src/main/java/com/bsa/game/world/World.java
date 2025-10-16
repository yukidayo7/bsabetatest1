package com.bsa.game.world;

import com.badlogic.gdx.math.Vector2;
import com.bsa.game.Npc;
import com.bsa.game.data.GameContentLoader;
import com.bsa.game.data.GameMap;

import java.util.*;

/**
 * Mantiene TODOS los mapas coexistiendo.
 * Crea un MapManager por mapa y permite consultar los NPCs del mapa actual.
 */
public class World {
    private final Map<String, MapManager> maps = new LinkedHashMap<>();

    /** Crea un MapManager por cada GameMap cargado desde JSON y spawnea sus NPCs */
    public void initFromContent() {
        for (String id : GameContentLoader.MAPS.keySet()) {
            GameMap gm = GameContentLoader.getMap(id);
            MapManager mm = new MapManager(gm);
            mm.spawnAll();
            maps.put(id, mm);
        }
    }

    /** Actualiza SOLO el mapa actual del jugador (cliente) */
    public void update(float delta, Vector2 playerPos, String currentMapId) {
        MapManager mm = maps.get(currentMapId);
        if (mm != null) {
            mm.update(delta, playerPos);
        }
    }

    /** Devuelve los NPCs del mapa indicado (para render, selección, ataque, etc.) */
    public List<Npc> getNpcsFor(String mapId) {
        MapManager mm = maps.get(mapId);
        return (mm != null) ? mm.getNpcs() : Collections.emptyList();
    }

    public MapManager getManager(String mapId) { return maps.get(mapId); }
}

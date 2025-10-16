package com.bsa.game.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;
import java.util.Map;

public class GameContentLoader {
    public static final Map<String, ShipType> SHIPS = new HashMap<>();
    public static final Map<String, NpcType> NPCS = new HashMap<>();
    public static final Map<String, GameMap> MAPS = new HashMap<>();

    public static void loadAll() {
        loadShips("data/ships.json");
        loadNpcs("data/npcs.json");
        loadMaps("data/maps.json");
    }

    public static void loadShips(String path) {
        SHIPS.clear();
        FileHandle fh = Gdx.files.internal(path);
        if (!fh.exists()) {
            Gdx.app.log("GameContentLoader", "ships.json not found at " + path);
            return;
        }
        JsonValue root = new JsonReader().parse(fh);
        for (JsonValue entry = root.child; entry != null; entry = entry.next) {
            ShipType s = new ShipType();
            s.id = entry.name();
            s.name = entry.getString("name", entry.name());
            s.hp = entry.getInt("hp", 1000);
            s.speed = entry.getFloat("speed", 200f);
            s.laserSlots = entry.getInt("laserSlots", 0);
            s.specialAbility = entry.getString("specialAbility", null);
            SHIPS.put(s.id, s);
        }
        Gdx.app.log("GameContentLoader", "Loaded ships: " + SHIPS.size());
    }

    public static void loadNpcs(String path) {
        NPCS.clear();
        FileHandle fh = Gdx.files.internal(path);
        if (!fh.exists()) {
            Gdx.app.log("GameContentLoader", "npcs.json not found at " + path);
            return;
        }

        JsonValue root = new JsonReader().parse(fh);
        for (JsonValue entry = root.child; entry != null; entry = entry.next) {
            NpcType n = new NpcType();
            n.id = entry.name();
            n.hp = entry.getInt("hp", 100);
            n.rewardXP = entry.getInt("rewardXP", 0);
            n.rewardCredits = entry.getInt("rewardCredits", 0);
            n.rewardUridium = entry.getInt("rewardUridium", 0);
            n.rewardHonor = entry.getInt("rewardHonor", 0);
            n.aggressive = entry.getBoolean("aggressive", false);
            n.speed = entry.getFloat("speed", 100f);
            n.attackRange = entry.getInt("attackRange", 300);
            n.damage = entry.getInt("damage", 50);
            n.onAttackChase = entry.getBoolean("onAttackChase", true);
            NPCS.put(n.id, n);
        }

        Gdx.app.log("GameContentLoader", "Loaded npcs: " + NPCS.size());
    }


    public static void loadMaps(String path) {
        MAPS.clear();
        FileHandle fh = Gdx.files.internal(path);
        if (!fh.exists()) {
            Gdx.app.log("GameContentLoader", "maps.json not found at " + path);
            return;
        }

        JsonValue root = new JsonReader().parse(fh);
        for (JsonValue entry = root.child; entry != null; entry = entry.next) {
            GameMap map = new GameMap();
            map.id = entry.name();
            map.faction = entry.getString("faction", null);
            map.background = entry.getString("background", null);
            map.width = entry.getInt("width", 6000);
            map.height = entry.getInt("height", 4000);

            // Leer zonas de spawn
            JsonValue zonesArray = entry.get("npcZones");
            if (zonesArray != null) {
                for (JsonValue zoneEntry = zonesArray.child; zoneEntry != null; zoneEntry = zoneEntry.next) {
                    NpcSpawnZone zone = new NpcSpawnZone();
                    zone.x = zoneEntry.getInt("x", 0);
                    zone.y = zoneEntry.getInt("y", 0);
                    zone.width = zoneEntry.getInt("width", 500);
                    zone.height = zoneEntry.getInt("height", 500);

                    JsonValue typesArray = zoneEntry.get("types");
                    if (typesArray != null) {
                        for (JsonValue typeEntry = typesArray.child; typeEntry != null; typeEntry = typeEntry.next) {
                            NpcSpawnZone.NpcSpawnType t = new NpcSpawnZone.NpcSpawnType();
                            t.id = typeEntry.getString("id", null);
                            t.count = typeEntry.getInt("count", 0);
                            zone.types.add(t);
                        }
                    }
                    map.npcZones.add(zone);
                }
            }

            // Leer portales
            JsonValue portalsArray = entry.get("portals");
            if (portalsArray != null) {
                for (JsonValue p = portalsArray.child; p != null; p = p.next) {
                    Portal portal = new Portal();
                    portal.to = p.getString("to", null);
                    portal.x = p.getInt("x", 0);
                    portal.y = p.getInt("y", 0);
                    map.portals.add(portal);
                }
            }

            MAPS.put(map.id, map);
        }

        Gdx.app.log("GameContentLoader", "Loaded maps: " + MAPS.size());
    }
// ==================== GETTERS ====================

    public static ShipType getShip(String id) {
        return SHIPS.get(id);
    }

    public static NpcType getNpc(String id) {
        return NPCS.get(id);
    }

    public static GameMap getMap(String id) {
        return MAPS.get(id);
    }
}


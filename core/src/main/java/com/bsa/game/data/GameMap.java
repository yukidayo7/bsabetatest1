package com.bsa.game.data;

import java.util.ArrayList;
import java.util.List;

public class GameMap {
    public String id;
    public String faction;
    public String background;
    public int width;
    public int height;
    public List<NpcSpawnZone> npcZones = new ArrayList<>();
    public List<Portal> portals = new ArrayList<>();
}

package com.bsa.game.data;

import java.util.ArrayList;
import java.util.List;

public class NpcSpawnZone {
    public int x;
    public int y;
    public int width;
    public int height;
    public List<NpcSpawnType> types = new ArrayList<>();

    public static class NpcSpawnType {
        public String id;
        public int count;
    }
}

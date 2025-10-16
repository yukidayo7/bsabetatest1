package com.bsa.game.data;

public class ShipType {
    public String id;
    public String name;
    public int hp;
    public float speed;
    public int laserSlots;
    public String specialAbility;

    public ShipType() {}

    public ShipType(String id, String name, int hp, float speed, int laserSlots, String specialAbility) {
        this.id = id;
        this.name = name;
        this.hp = hp;
        this.speed = speed;
        this.laserSlots = laserSlots;
        this.specialAbility = specialAbility;
    }
}

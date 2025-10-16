package com.bsa.game.data;

public class NpcType {
    public String id;
    public int hp;
    public int rewardXP;
    public int rewardCredits;
    public int rewardUridium;
    public int rewardHonor;
    public boolean aggressive;
    public float speed;
    public int attackRange;
    public int damage;
    public boolean onAttackChase;

    public NpcType() {}

    public NpcType(String id, int hp, int rewardXP, int rewardCredits,
                   int rewardUridium, int rewardHonor, boolean aggressive,
                   float speed, int attackRange, int damage, boolean onAttackChase) {
        this.id = id;
        this.hp = hp;
        this.rewardXP = rewardXP;
        this.rewardCredits = rewardCredits;
        this.rewardUridium = rewardUridium;
        this.rewardHonor = rewardHonor;
        this.aggressive = aggressive;
        this.speed = speed;
        this.attackRange = attackRange;
        this.damage = damage;
        this.onAttackChase = onAttackChase;
    }
}

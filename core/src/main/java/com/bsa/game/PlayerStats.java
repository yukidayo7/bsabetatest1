package com.bsa.game;

public class PlayerStats {

    private int level;
    private long experience;
    private long honor;
    private long credits;
    private long uridium;

    public PlayerStats() {
        this.level = 1;
        this.experience = 0;
        this.honor = 0;
        this.credits = 10000;
        this.uridium = 0;
    }

    public void addExperience(long amount) {
        this.experience += amount;
        checkLevelUp();
    }

    public void addHonor(long amount) {
        this.honor += amount;
    }

    public void addCredits(long amount) {
        this.credits += amount;
    }

    public void addUridium(long amount) {
        this.uridium += amount;
    }

    private void checkLevelUp() {
        long requiredXP = 5000L * level;
        while (experience >= requiredXP) {
            experience -= requiredXP;
            level++;
            requiredXP = 5000L * level;
        }
    }

    public int getLevel() { return level; }
    public long getExperience() { return experience; }
    public long getHonor() { return honor; }
    public long getCredits() { return credits; }
    public long getUridium() { return uridium; }
}

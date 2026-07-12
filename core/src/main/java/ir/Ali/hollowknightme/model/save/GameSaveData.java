package ir.Ali.hollowknightme.model.save;

import com.badlogic.gdx.utils.ObjectMap;

public class GameSaveData {
    private int slotNumber;
    private String currentRoomPath;

    private int knightHealth;
    private int knightSoul;

    private long playTimeSeconds;
    private int totalDeaths;
    private int totalEnemiesKilled;

    private ObjectMap<String, Integer> enemyKilledByType = new ObjectMap<>();

    private boolean achCompletion;
    private boolean achSpeedrun;
    private boolean achTrueHunter;
    private boolean achFalseKnight;
    private boolean achProPlayer;

    public GameSaveData() {}

    public int getSlotNumber() { return slotNumber; }
    public void setSlotNumber(int slotNumber) { this.slotNumber = slotNumber; }

    public String getCurrentRoomPath() { return currentRoomPath; }
    public void setCurrentRoomPath(String currentRoomPath) { this.currentRoomPath = currentRoomPath; }

    public int getKnightHealth() { return knightHealth; }
    public void setKnightHealth(int knightHealth) { this.knightHealth = knightHealth; }

    public int getKnightSoul() { return knightSoul; }
    public void setKnightSoul(int knightSoul) { this.knightSoul = knightSoul; }

    public long getPlayTimeSeconds() { return playTimeSeconds; }
    public void setPlayTimeSeconds(long playTimeSeconds) { this.playTimeSeconds = playTimeSeconds; }

    public int getTotalDeaths() { return totalDeaths; }
    public void setTotalDeaths(int totalDeaths) { this.totalDeaths = totalDeaths; }

    public int getTotalEnemiesKilled() { return totalEnemiesKilled; }
    public void setTotalEnemiesKilled(int totalEnemiesKilled) { this.totalEnemiesKilled = totalEnemiesKilled; }

    public ObjectMap<String, Integer> getEnemyKilledByType() { return enemyKilledByType; }
    public void setEnemyKilledByType(ObjectMap<String, Integer> enemyKilledByType) { this.enemyKilledByType = enemyKilledByType; }

    public boolean isAchCompletion() { return achCompletion; }
    public void setAchCompletion(boolean achCompletion) { this.achCompletion = achCompletion; }

    public boolean isAchSpeedrun() { return achSpeedrun; }
    public void setAchSpeedrun(boolean achSpeedrun) { this.achSpeedrun = achSpeedrun; }

    public boolean isAchTrueHunter() { return achTrueHunter; }
    public void setAchTrueHunter(boolean achTrueHunter) { this.achTrueHunter = achTrueHunter; }

    public boolean isAchFalseKnight() { return achFalseKnight; }
    public void setAchFalseKnight(boolean achFalseKnight) { this.achFalseKnight = achFalseKnight; }

    public boolean isAchProPlayer() { return achProPlayer; }
    public void setAchProPlayer(boolean achProPlayer) { this.achProPlayer = achProPlayer; }
}

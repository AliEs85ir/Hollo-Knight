package ir.Ali.hollowknightme.model.save;

public class SaveModel {
    private final int slotNumber;
    private boolean isEmpty;
    private String locationName;
    private int progressPercentage;
    private String playTime;

    private int deathCount;
    private int enemiesKilled;
    private int currentHealth;
    private int currentSoul;

    public SaveModel(int slotNumber) {
        this.slotNumber = slotNumber;
        this.isEmpty = true;
        this.locationName = "";
        this.progressPercentage = 0;
        this.playTime = "0m 0s";
        this.deathCount = 0;
        this.enemiesKilled = 0;
        this.currentHealth = 0;
        this.currentSoul = 0;
    }

    public SaveModel(int slotNumber, String locationName, int progressPercentage, String playTime,
                     int deathCount, int enemiesKilled, int currentHealth, int currentSoul) {
        this.slotNumber = slotNumber;
        this.isEmpty = false;
        this.locationName = locationName;
        this.progressPercentage = progressPercentage;
        this.playTime = playTime;
        this.deathCount = deathCount;
        this.enemiesKilled = enemiesKilled;
        this.currentHealth = currentHealth;
        this.currentSoul = currentSoul;
    }

    public int getSlotNumber() { return slotNumber; }
    public boolean isEmpty() { return isEmpty; }
    public String getLocationName() { return locationName; }
    public int getProgressPercentage() { return progressPercentage; }
    public String getPlayTime() { return playTime; }
    public int getDeathCount() { return deathCount; }
    public int getEnemiesKilled() { return enemiesKilled; }
    public int getCurrentHealth() { return currentHealth; }
    public int getCurrentSoul() { return currentSoul; }

    public void clear() {
        this.isEmpty = true;
        this.locationName = "";
        this.progressPercentage = 0;
        this.playTime = "0m 0s";
        this.deathCount = 0;
        this.enemiesKilled = 0;
        this.currentHealth = 0;
        this.currentSoul = 0;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setProgressPercentage(int progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public void setPlayTime(String playTime) {
        this.playTime = playTime;
    }

    public void setDeathCount(int deathCount) {
        this.deathCount = deathCount;
    }

    public void setEnemiesKilled(int enemiesKilled) {
        this.enemiesKilled = enemiesKilled;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public void setCurrentSoul(int currentSoul) {
        this.currentSoul = currentSoul;
    }
}

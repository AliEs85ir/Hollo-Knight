package ir.Ali.hollowknightme.controller.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import ir.Ali.hollowknightme.controller.sound.AudioManager;
import ir.Ali.hollowknightme.enums.enemy.EnemyType;
import ir.Ali.hollowknightme.model.knight.Knight;
import ir.Ali.hollowknightme.model.save.GameSaveData;
import ir.Ali.hollowknightme.view.popup.AchievementPopup;

public class GameProgressManager {
    private static GameProgressManager instance;

    private int activeSlot;
    private String currentRoomPath;
    private int currentHealth;
    private int currentSoul;

    private float timeAccumulator = 0f;
    private long playTimeSeconds = 0;
    private int totalDeaths = 0;
    private int totalEnemiesKilled = 0;
    private final ObjectMap<EnemyType, Integer> enemyKilledByType = new ObjectMap<>();

    private boolean achCompletion = false;
    private boolean achSpeedrun = false;
    private boolean achTrueHunter = false;
    private boolean achFalseKnight = false;
    private boolean achProPlayer = false;

    private GameProgressManager() {}

    public static GameProgressManager getInstance() {
        if (instance == null) instance = new GameProgressManager();
        return instance;
    }

    public void startNewGame(int slot) {
        this.activeSlot = slot;
        this.currentRoomPath = "room1.tmx";
        this.currentHealth = Knight.maxHealth;
        this.currentSoul = 0;
        this.playTimeSeconds = 0;
        this.totalDeaths = 0;
        this.totalEnemiesKilled = 0;
        this.enemyKilledByType.clear();
        this.timeAccumulator = 0f;

        this.achCompletion = false;
        this.achSpeedrun = false;
        this.achTrueHunter = false;
        this.achFalseKnight = false;
        this.achProPlayer = false;
    }

    public void loadFromSave(GameSaveData save) {
        this.activeSlot = save.getSlotNumber();
        this.currentRoomPath = save.getCurrentRoomPath();
        this.currentHealth = save.getKnightHealth();
        this.currentSoul = save.getKnightSoul();
        this.playTimeSeconds = save.getPlayTimeSeconds();
        this.totalDeaths = save.getTotalDeaths();
        this.totalEnemiesKilled = save.getTotalEnemiesKilled();

        this.enemyKilledByType.clear();
        if (save.getEnemyKilledByType() != null) {
            for (var entry : save.getEnemyKilledByType().entries()) {
                try {
                    EnemyType type = EnemyType.valueOf(entry.key);
                    this.enemyKilledByType.put(type, entry.value);
                } catch (IllegalArgumentException | NullPointerException e) {
                }
            }
        }
        this.timeAccumulator = 0f;

        this.achCompletion = save.isAchCompletion();
        this.achSpeedrun = save.isAchSpeedrun();
        this.achTrueHunter = save.isAchTrueHunter();
        this.achFalseKnight = save.isAchFalseKnight();
        this.achProPlayer = save.isAchProPlayer();
    }

    public GameSaveData generateSaveData() {
        GameSaveData save = new GameSaveData();
        save.setSlotNumber(activeSlot);
        save.setCurrentRoomPath(currentRoomPath);
        save.setKnightHealth(currentHealth);
        save.setKnightSoul(currentSoul);
        save.setPlayTimeSeconds(playTimeSeconds);
        save.setTotalDeaths(totalDeaths);
        save.setTotalEnemiesKilled(totalEnemiesKilled);

        ObjectMap<String, Integer> stringKeyMap = new ObjectMap<>();
        for (var entry : enemyKilledByType.entries()) {
            if (entry.key != null) {
                stringKeyMap.put(entry.key.name(), entry.value);
            }
        }
        save.setEnemyKilledByType(stringKeyMap);

        save.setAchCompletion(achCompletion);
        save.setAchSpeedrun(achSpeedrun);
        save.setAchTrueHunter(achTrueHunter);
        save.setAchFalseKnight(achFalseKnight);
        save.setAchProPlayer(achProPlayer);

        return save;
    }

    public void updateTime(float delta) {
        timeAccumulator += delta;
        if (timeAccumulator >= 1f) {
            playTimeSeconds += (long) timeAccumulator;
            timeAccumulator %= 1f;
        }
    }

    public void addDeath() {
        totalDeaths++;
    }

    public void addEnemyKill(EnemyType enemyTypeName) {
        totalEnemiesKilled++;
        if (enemyTypeName == EnemyType.FALSE_KNIGHT) triggerGameCompletion();
        int count = enemyKilledByType.get(enemyTypeName, 0);
        enemyKilledByType.put(enemyTypeName, count + 1);

        checkTrueHunterAchievement();
    }

    private void checkTrueHunterAchievement() {
        if (achTrueHunter) return;

        boolean killedAll = true;
        for (EnemyType type : EnemyType.values()) {
            if (type == EnemyType.FALSE_KNIGHT) continue;
            if (enemyKilledByType.get(type, 0) == 0) {
                killedAll = false;
                break;
            }
        }

        if (killedAll) {
            achTrueHunter = true;
            AchievementPopup.show("True Hunter", "Killed every type of enemy!");
        }
    }

    public void setCurrentRoomPath(String path) {
        this.currentRoomPath = path;

        if (path != null) {
            if (path.equals("room1.tmx") || path.equals("room2.tmx") || path.equals("room3.tmx")) {
                AudioManager.getInstance().playMusic(ir.Ali.hollowknightme.enums.sound.MusicType.FORGOTTEN_CROSSROADS);
            } else if (path.equals("room4.tmx") || path.equals("room5.tmx")) {
                AudioManager.getInstance().playMusic(ir.Ali.hollowknightme.enums.sound.MusicType.CITY_OF_TEARS);
            } else if (path.equals("bossRoom.tmx")) {
                AudioManager.getInstance().playMusic(ir.Ali.hollowknightme.enums.sound.MusicType.BOSS);
            }
        }

        if ("room5.tmx".equals(path) && totalDeaths == 0 && !achProPlayer) {
            achProPlayer = true;
            AchievementPopup.show("Professional Player", "Reached the boss room without dying!");
        }
    }

    public void triggerGameCompletion() {
        if (!achCompletion) {
            achCompletion = true;
            AchievementPopup.show("Completion", "Finished the game!");
        }

        if (!achFalseKnight) {
            achFalseKnight = true;
            AchievementPopup.show("Defeat False Knight", "Defeated the False Knight!");
        }

        if (playTimeSeconds <= 120 && !achSpeedrun) {
            achSpeedrun = true;
            AchievementPopup.show("Speedrun", "Finished the game under 2 minutes!");
        }
    }

    // Getters
    public String getCurrentRoomPath() { return currentRoomPath; }
    public int getCurrentHealth() { return currentHealth; }
    public void setCurrentHealth(int hp) { this.currentHealth = hp; }
    public int getCurrentSoul() { return currentSoul; }
    public void setCurrentSoul(int soul) { this.currentSoul = soul; }
    public boolean isAchCompletion() { return achCompletion; }
    public boolean isAchSpeedrun() { return achSpeedrun; }
    public boolean isAchTrueHunter() { return achTrueHunter; }
    public boolean isAchFalseKnight() { return achFalseKnight; }
    public boolean isAchProPlayer() { return achProPlayer; }

    public int getActiveSlot() {
        return activeSlot;
    }

    public float getTimeAccumulator() {
        return timeAccumulator;
    }

    public long getPlayTimeSeconds() {
        return playTimeSeconds;
    }

    public int getTotalDeaths() {
        return totalDeaths;
    }

    public int getTotalEnemiesKilled() {
        return totalEnemiesKilled;
    }

    public ObjectMap<EnemyType, Integer> getEnemyKilledByType() {
        return enemyKilledByType;
    }
}

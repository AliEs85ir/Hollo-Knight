package ir.Ali.hollowknightme.model.enviroment;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import ir.Ali.hollowknightme.controller.map.MapManager;
import ir.Ali.hollowknightme.enums.enemy.EnemyType;
import ir.Ali.hollowknightme.model.map.MapData;
import ir.Ali.hollowknightme.model.map.SpawnData;
import ir.Ali.hollowknightme.model.map.SpawnType;

public class RoomState {
    private final Array<EnemyPersistentData> enemies;
    private final Array<String> destroyedObjects;
    private boolean bossDefeated;
    private boolean visited;
    private Vector2 safePosition;

    public RoomState(MapData mapData) {
        this.enemies = new Array<>();
        this.destroyedObjects = new Array<>();
        this.bossDefeated = false;
        this.visited = false;

        if (mapData != null) {
            int enemyCounter = 0;
            for (SpawnData spawn : mapData.getSpawns()) {
                if (spawn.getType() == SpawnType.ENEMY) {
                    EnemyType type = spawn.getEnemyType();
                    float defaultHp = getDefaultHp(type);

                    String uniqueId = type.name() + "_" + (++enemyCounter);
                    EnemyPersistentData data = new EnemyPersistentData(
                        uniqueId,
                        type,
                        spawn.getPosition(),
                        spawn.isFacingRight(),
                        spawn.getRangeId(),
                        defaultHp,
                        defaultHp
                    );
                    enemies.add(data);
                }
            }
            this.safePosition = spawnPosition(mapData);
        }
    }
    private Vector2 spawnPosition(MapData mapData) {
        Vector2 vector2 = new Vector2(0 , 0);
        for (SpawnData spawn : mapData.getSpawns()) {

            if (spawn.getType() == SpawnType.PLAYER && spawn.isStart()) {
                vector2 = spawn.getPosition().cpy().scl(1f / MapManager.PPM);
                break; // نقطه پیدا شد، از حلقه خارج می‌شویم
            }
        }
        return vector2;
    }

    private float getDefaultHp(EnemyType type) {
        return switch (type) {
            case CRAWLID -> 10f;
            case WINGED_SENTRY -> 20f;
            case HUSK_HORNHEAD -> 40f;
            case CRYSTAL_GUARDIAN -> 100f;
            case FALSE_KNIGHT ->  300f;
        };
    }

    public Array<EnemyPersistentData> getEnemies() {
        return enemies;
    }

    public EnemyPersistentData getEnemyDataById(String id) {
        for (int i = 0; i < enemies.size; i++) {
            if (enemies.get(i).getId().equals(id)) {
                return enemies.get(i);
            }
        }
        return null;
    }

    public void updateEnemyState(String id, boolean alive, float hp) {
        EnemyPersistentData data = getEnemyDataById(id);
        if (data != null) {
            data.setAlive(alive);
            data.setHp(hp);
        }
    }

    public Array<String> getDestroyedObjects() { return destroyedObjects; }
    public boolean isBossDefeated() { return bossDefeated; }
    public void setBossDefeated(boolean bossDefeated) { this.bossDefeated = bossDefeated; }
    public boolean isVisited() { return visited; }
    public void setVisited(boolean visited) { this.visited = visited; }


    public boolean isObjectDestroyed(String objectId) {
        return destroyedObjects.contains(objectId, false);
    }

    public void destroyObject(String objectId) {
        if (!destroyedObjects.contains(objectId, false)) {
            destroyedObjects.add(objectId);
        }
    }

    public void reset() {
        for (int i = 0; i < enemies.size; i++) {
            EnemyPersistentData data = enemies.get(i);
            data.setAlive(true);
            data.setHp(data.getMaxHp());
        }
        destroyedObjects.clear();
        bossDefeated = false;
        visited = false;
    }

    public Vector2 getSafePosition() {
        return safePosition;
    }

    public void setSafePosition(Vector2 safePosition) {
        this.safePosition = safePosition;
    }
}

package ir.Ali.hollowknightme.controller.map;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import ir.Ali.hollowknightme.factory.enemy.EnemyFactory;
import ir.Ali.hollowknightme.model.enviroment.EnemyPersistentData;
import ir.Ali.hollowknightme.model.enviroment.RoomState;
import ir.Ali.hollowknightme.model.map.MapData;
import ir.Ali.hollowknightme.model.map.SpawnData;
import ir.Ali.hollowknightme.model.map.SpawnType;

public class SpawnManager {

    private final EnemyFactory enemyFactory;

    public SpawnManager(EnemyFactory enemyFactory) {
        this.enemyFactory = enemyFactory;
    }

    public void spawnEntities(MapData mapData, RoomState roomState) {
        if (mapData == null || roomState == null) return;

        int enemyIndex = 0;
        for (SpawnData spawn : mapData.getSpawns()) {
            if (spawn.getType() == SpawnType.ENEMY) {
                if (enemyIndex < roomState.getEnemies().size) {
                    EnemyPersistentData enemyData = roomState.getEnemies().get(enemyIndex);
                    if (enemyData.isAlive()) {
                        spawnEnemy(spawn);
                    }
                }
                enemyIndex++;
            }
        }
    }

    private void spawnEnemy(SpawnData spawn) {
        Vector2 pos = spawn.getPosition().cpy().scl(1f / MapManager.PPM);
        float x = pos.x;
        float y = pos.y;

        switch (spawn.getEnemyType()) {
            case CRAWLID -> enemyFactory.createCrawlid(x, y, 1f, 0.6f, 22f, 1f, 1f);
            case HUSK_HORNHEAD -> enemyFactory.createHuskHornhead(x, y, 1.2f, 1.5f, 25f, 1f,
                1.5f, 4f, 1f, 0.5f, 0.5f,
                new Rectangle(x - 4f, y - 1f, 7f, 2f));
            case CRYSTAL_GUARDIAN -> enemyFactory.createCrystalGuardian(x, y, 1.5f, 2.2f, 40f,
                1f, 2.5f, new Rectangle(x - 6f, y - 2f, 12f, 3f), 1.4f,
                1.5f, spawn.isFacingRight());
            case WINGED_SENTRY -> enemyFactory.createWingedSentry(x, y, 1.0f, 1.0f, 25f, 1f,
                2.5f, 5f, new Rectangle(0, 0, 10f, 3f), 0.9f,
                3f, spawn.isFacingRight());
            case FALSE_KNIGHT ->  enemyFactory.createFalseKnight(x, y, 4f, 4.0f, 120f, 1f, 4f);
        }
    }

    public Vector2 getPlayerSpawn(MapData mapData, boolean isInput) {
        Vector2 exactMatch = null;
        Vector2 fallbackSpawn = null;

        for (SpawnData spawn : mapData.getSpawns()) {
            if (spawn.getType() == SpawnType.PLAYER) {
                if (fallbackSpawn == null) {
                    fallbackSpawn = spawn.getPosition().cpy().scl(1f / MapManager.PPM);
                }

                if (spawn.isStart() && !isInput) {
                    exactMatch = spawn.getPosition().cpy().scl(1f / MapManager.PPM);
                    break;
                } else if (!spawn.isStart() && isInput) {
                    exactMatch = spawn.getPosition().cpy().scl(1f / MapManager.PPM);
                    break;
                }
            }
        }

        if (exactMatch != null) return exactMatch;
        if (fallbackSpawn != null) return fallbackSpawn;

        return new Vector2(5, 5);
    }
}

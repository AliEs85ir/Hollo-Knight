package ir.Ali.hollowknightme.controller.knight;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import ir.Ali.hollowknightme.enums.knight.KnightState;
import ir.Ali.hollowknightme.controller.game.GameStateManager;
import ir.Ali.hollowknightme.controller.map.MapManager;
import ir.Ali.hollowknightme.controller.map.SpawnManager;
import ir.Ali.hollowknightme.model.knight.Knight;
import ir.Ali.hollowknightme.model.map.MapData;
import ir.Ali.hollowknightme.model.map.SpawnData;
import ir.Ali.hollowknightme.model.map.SpawnType;

public class KnightManager {

    private final Knight knight;
    private final SpawnManager spawnManager;
    private boolean respawnPending = false;
    private MapData pendingRespawnData = null;
    private boolean shouldHealOnRespawn = false;
    private KnightController knightController;

    public KnightManager(Knight knight, SpawnManager spawnManager, MapManager mapManager) {
        this.knight = knight;
        this.spawnManager = spawnManager;
    }

    public void requestRespawn(MapData mapData, boolean shouldHeal) {
        this.pendingRespawnData = mapData;
        this.shouldHealOnRespawn = shouldHeal;
        this.respawnPending = true;
    }

    public void processPendingRespawn() {
        if (respawnPending) {
            respawn(pendingRespawnData, shouldHealOnRespawn);
            respawnPending = false;
            pendingRespawnData = null;
            shouldHealOnRespawn = false;
        }
    }

    public void setKnightController(KnightController controller) {
        this.knightController = controller;
    }

    private void respawn(MapData mapData, boolean shouldHeal) {
        if (knightController != null) {
            knightController.resetAfterRespawn();
        }

        Vector2 targetPosition = null;

        if (mapData != null) {
            targetPosition = getSpawnPositionFromMap(mapData);
        }

        if (targetPosition == null) {
            targetPosition = GameStateManager.getInstance().getSafePosition();
        }

        if (targetPosition != null) {
            teleport(targetPosition);
        }

        resetKnight();

        if (shouldHeal) {
            healFull();
        }
    }

    private Vector2 getSpawnPositionFromMap(MapData mapData) {
        for (SpawnData spawn : mapData.getSpawns()) {
            if (spawn.getType() == SpawnType.PLAYER && spawn.isStart()) {
                return spawn.getPosition().cpy().scl(1f / MapManager.PPM);
            }
        }

        for (SpawnData spawn : mapData.getSpawns()) {
            if (spawn.getType() == SpawnType.PLAYER) {
                return spawn.getPosition().cpy().scl(1f / MapManager.PPM);
            }
        }
        return new Vector2(5, 5);
    }

    public void teleport(Vector2 position) {
        Body body = knight.getBody();
        if (body != null) {
            body.setTransform(position, 0);
            body.setLinearVelocity(0, 0);
            body.setAngularVelocity(0);
        }
    }

    private void resetKnight() {
        knight.setGrounded(false);
        knight.setOnWall(false);
        knight.setWallDirection(0);
        knight.setCanDoubleJump(false);
        knight.setLocked(false);
        knight.setDashing(false);
        knight.setCurrentState(KnightState.IDLE);
        knight.setInvincibleTimer(0);
    }

    public void healFull() {
        if (knight != null) {
            knight.setCurrentHealth(knight.getMaxHealth());
        }
    }

    public Body getBody() { return knight.getBody(); }
    public Knight getKnight() { return knight; }
}

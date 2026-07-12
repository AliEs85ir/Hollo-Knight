package ir.Ali.hollowknightme.controller.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import ir.Ali.hollowknightme.controller.knight.KnightManager;
import ir.Ali.hollowknightme.controller.sound.AudioManager;
import ir.Ali.hollowknightme.enums.knight.KnightState;
import ir.Ali.hollowknightme.controller.enemy.EnemyManager;
import ir.Ali.hollowknightme.controller.game.GameProgressManager;
import ir.Ali.hollowknightme.controller.game.GameStateManager;
import ir.Ali.hollowknightme.model.enviroment.Room;
import ir.Ali.hollowknightme.model.knight.Knight;
import ir.Ali.hollowknightme.model.map.MapData;
import ir.Ali.hollowknightme.model.map.TransitionData;

import java.util.ArrayList;
import java.util.List;

public class TransitionManager implements ContactListener {

    private final KnightManager knightManager;
    private final MapManager mapManager;
    private final World world;
    private final EnemyManager enemyManager;
    private final SpawnManager spawnManager;
    private final FadeManager fadeManager;

    private boolean isTransitioning = false;
    private boolean pendingTransition = false;
    private Room targetRoomForTransition = null;
    private List<Body> worldBodies = new ArrayList<>();
    private final DegradableWallManager degradableWallManager;

    private Result result = null;

    public TransitionManager(KnightManager knightManager, MapManager mapManager, World world,
                             EnemyManager enemyManager, SpawnManager spawnManager, FadeManager fadeManager ,
                             DegradableWallManager degradableWallManager) {
        this.knightManager = knightManager;
        this.mapManager = mapManager;
        this.world = world;
        this.enemyManager = enemyManager;
        this.spawnManager = spawnManager;
        this.fadeManager = fadeManager;
        this.degradableWallManager = degradableWallManager;
    }


    public void update() {
        boolean ctrlPressed = Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT);
        if (ctrlPressed && Gdx.input.isKeyJustPressed(Input.Keys.S) && !isTransitioning && !pendingTransition && !fadeManager.isBusy()) {
            GameStateManager gsm = GameStateManager.getInstance();
            if (gsm.getCurrentEnvironment() != null) {
                List<Room> rooms = gsm.getCurrentEnvironment().getRooms();
                if (rooms != null && !rooms.isEmpty()) {
                    Room bossRoom = null;
                    for (Room room : rooms) {
                        if (room.getMapPath() != null && room.getMapPath().toLowerCase().contains("boss")) {
                            bossRoom = room;
                            break;
                        }
                    }
                    if (bossRoom == null) {
                        bossRoom = rooms.get(rooms.size() - 1);
                    }
                    targetRoomForTransition = bossRoom;
                    pendingTransition = true;
                    result = new Result(bossRoom, false);
                }
            }
        }

        if (pendingTransition && targetRoomForTransition != null) {
            pendingTransition = false;
            AudioManager.getInstance().playSfx(ir.Ali.hollowknightme.enums.sound.SfxType.TRANSITION);
            fadeManager.start(() -> {
                performTransition(result);
                targetRoomForTransition = null;
            });
        }
    }

    @Override
    public void beginContact(Contact contact) {
        if (isTransitioning || pendingTransition || fadeManager.isBusy()) return;

        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA.getBody() != knightManager.getBody() && fixtureB.getBody() != knightManager.getBody()) return;

        TransitionData transition = getTransitionData(fixtureA, fixtureB);
        if (transition == null) return;

        GameStateManager gsm = GameStateManager.getInstance();
        if (transition.isInput()) {
            targetRoomForTransition = gsm.getPreviousRoom();
            pendingTransition = targetRoomForTransition != null;
            result = new Result(targetRoomForTransition, true);
        } else if (transition.isOutput()) {
            targetRoomForTransition = gsm.getNextRoom();
            pendingTransition = targetRoomForTransition != null;
            result = new Result(targetRoomForTransition, false);
        }
    }

    @Override
    public void endContact(Contact contact) {}

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}

    private void performTransition(Result result) {
        Room targetRoom = result.getTargetRoomForTransition();
        boolean isInput = result.isInput();
        Knight knight = knightManager.getKnight();
        isTransitioning = true;
        knight.setCurrentState(KnightState.TRANSITION);
        knight.setLocked(true);

        String targetMap = targetRoom.getMapPath();
        if (targetMap == null || targetMap.isEmpty()) {
            completeTransition();
            return;
        }

        enemyManager.clear();

        mapManager.loadMap(targetMap);
        rebuildWorld();
        placeKnightInNewMap(isInput);
        updateGameState(targetMap);

        GameProgressManager.getInstance().setCurrentRoomPath(targetMap);

        MapData newMapData = mapManager.getMapData();
        if (newMapData != null) {
            spawnManager.spawnEntities(newMapData, GameStateManager.getInstance().getCurrentRoomState());
        }

        completeTransition();
    }

    private void completeTransition() {
        Knight knight = knightManager.getKnight();
        knight.setLocked(false);
        knight.setCurrentState(KnightState.IDLE);
        isTransitioning = false;
    }

    private void rebuildWorld() {
        for (Body body : worldBodies) {
            world.destroyBody(body);
        }
        worldBodies.clear();

        if (degradableWallManager != null) {
            degradableWallManager.clear();
        }

        MapData mapData = mapManager.getMapData();
        if (mapData != null) {
            worldBodies.addAll(PhysicsBuilder.buildPhysics(world, mapData , degradableWallManager));
        }
    }

    private void placeKnightInNewMap(boolean isInput) {
        MapData mapData = mapManager.getMapData();
        if (mapData == null) return;

        Vector2 spawnPosition = spawnManager.getPlayerSpawn(mapData , isInput);
        GameStateManager.getInstance().updateSafePosition(spawnPosition.x, spawnPosition.y);
        knightManager.teleport(spawnPosition);
    }

    private void updateGameState(String targetMap) {
        GameStateManager gsm = GameStateManager.getInstance();
        var rooms = gsm.getCurrentEnvironment().getRooms();
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getMapPath().equals(targetMap)) {
                gsm.moveToRoom(i);
                break;
            }
        }
    }

    private TransitionData getTransitionData(Fixture fixtureA, Fixture fixtureB) {
        Object dataA = fixtureA.getBody().getUserData();
        if (dataA instanceof TransitionData) return (TransitionData) dataA;

        Object dataB = fixtureB.getBody().getUserData();
        if (dataB instanceof TransitionData) return (TransitionData) dataB;

        return null;
    }

    public void setWorldBodies(List<Body> bodies) {
        this.worldBodies = bodies;
    }
}

class Result {
    private Room targetRoomForTransition = null;
    private boolean isInput = false;

    public Result(Room targetRoomForTransition, boolean isInput) {
        this.targetRoomForTransition = targetRoomForTransition;
        this.isInput = isInput;
    }

    public Room getTargetRoomForTransition() {
        return targetRoomForTransition;
    }

    public boolean isInput() {
        return isInput;
    }
}

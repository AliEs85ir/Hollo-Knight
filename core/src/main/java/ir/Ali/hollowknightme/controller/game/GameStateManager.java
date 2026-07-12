package ir.Ali.hollowknightme.controller.game;

import com.badlogic.gdx.math.Vector2;
import ir.Ali.hollowknightme.enums.game.GameStatus;
import ir.Ali.hollowknightme.model.enviroment.Environment;
import ir.Ali.hollowknightme.model.enviroment.Room;
import ir.Ali.hollowknightme.model.enviroment.RoomState;

public class GameStateManager {

    private Environment currentEnvironment;
    private GameStatus gameStatus = GameStatus.PLAYING;

    private GameStateManager() {}

    private static class Holder {
        private static final GameStateManager INSTANCE = new GameStateManager();
    }

    public static GameStateManager getInstance() {
        return Holder.INSTANCE;
    }

    public void setCurrentEnvironment(Environment environment) {
        this.currentEnvironment = environment;
        markCurrentRoomAsVisited();
    }

    public Environment getCurrentEnvironment() {
        return currentEnvironment;
    }

    public Room getCurrentRoom() {
        return currentEnvironment != null ? currentEnvironment.getCurrentRoom() : null;
    }

    public Room getNextRoom() {
        return currentEnvironment != null ? currentEnvironment.getNextRoom() : null;
    }

    public Room getPreviousRoom() {
        return currentEnvironment != null ? currentEnvironment.getPreviousRoom() : null;
    }

    public Room getRoomAt(int index) {
        if (currentEnvironment != null && index >= 0 && index < currentEnvironment.getRooms().size()) {
            return currentEnvironment.getRooms().get(index);
        }
        return null;
    }

    public String getCurrentRoomPath() {
        Room room = getCurrentRoom();
        return room != null ? room.getMapPath() : null;
    }

    public void moveToNextRoom() {
        if (currentEnvironment != null) {
            currentEnvironment.moveToNextRoom();
            markCurrentRoomAsVisited();
        }
    }

    public void moveToPreviousRoom() {
        if (currentEnvironment != null) {
            currentEnvironment.moveToPreviousRoom();
            markCurrentRoomAsVisited();
        }
    }

    public void moveToRoom(int index) {
        if (currentEnvironment != null) {
            currentEnvironment.moveToRoom(index);
            markCurrentRoomAsVisited();
        }
    }

    public RoomState getCurrentRoomState() {
        Room room = getCurrentRoom();
        return room != null ? room.getState() : null;
    }

    public RoomState getRoomState(String mapPath) {
        if (currentEnvironment != null && mapPath != null) {
            for (Room room : currentEnvironment.getRooms()) {
                if (mapPath.equals(room.getMapPath())) {
                    return room.getState();
                }
            }
        }
        return null;
    }

    private void markCurrentRoomAsVisited() {
        RoomState state = getCurrentRoomState();
        if (state != null) {
            state.setVisited(true);
        }
    }

    public void updateSafePosition(float x, float y) {
        RoomState state = getCurrentRoomState();
        if (state != null) {
            state.setSafePosition(new Vector2(x , y));
        }
    }

    public Vector2 getSafePosition() {
        RoomState state = getCurrentRoomState();
        return state != null ? state.getSafePosition() : null;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }
}

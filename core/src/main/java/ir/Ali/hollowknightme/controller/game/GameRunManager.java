package ir.Ali.hollowknightme.controller.game;

import ir.Ali.hollowknightme.controller.sound.AudioManager;
import ir.Ali.hollowknightme.enums.game.GameStatus;
import ir.Ali.hollowknightme.controller.knight.KnightManager;
import ir.Ali.hollowknightme.model.enviroment.Room;
import ir.Ali.hollowknightme.model.map.MapData;

public class GameRunManager {

    private final KnightManager knightManager;
    private int deathCount;

    public GameRunManager(KnightManager knightManager) {
        this.knightManager = knightManager;
        this.deathCount = 0;
    }

    public void playerDied() {
        Room currentRoom = GameStateManager.getInstance().getCurrentRoom();
        if (currentRoom != null && currentRoom.hasBoss()) {
            GameStateManager.getInstance().setGameStatus(GameStatus.GAME_OVER);

            AudioManager.getInstance().playMusic(ir.Ali.hollowknightme.enums.sound.MusicType.GAME_OVER);
        } else {
            deathCount++;
            if (currentRoom != null) {
                MapData currentMapData = currentRoom.getMapData();
                knightManager.requestRespawn(currentMapData, true);
            } else {
                knightManager.requestRespawn(null, true);
            }
        }
        GameProgressManager.getInstance().addDeath();
    }

    public void bossDefeated() {
        Room currentRoom = GameStateManager.getInstance().getCurrentRoom();
        if (currentRoom != null) {
            currentRoom.getState().setBossDefeated(true);
        }
        GameStateManager.getInstance().setGameStatus(GameStatus.WIN);
        AudioManager.getInstance().playMusic(ir.Ali.hollowknightme.enums.sound.MusicType.WIN);
    }

    public int getDeathCount() { return deathCount; }
}

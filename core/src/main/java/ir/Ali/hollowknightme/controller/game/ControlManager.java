package ir.Ali.hollowknightme.controller.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.ObjectMap;
import ir.Ali.hollowknightme.enums.knight.MovementStatus;

public class ControlManager {

    private static volatile ControlManager instance;
    private final ObjectMap<MovementStatus, Integer> keybinds;

    private ControlManager() {
        this.keybinds = new ObjectMap<>();
        loadDefaultKeybinds();
    }

    public static ControlManager getInstance() {
        if (instance == null) {
            synchronized (ControlManager.class) {
                if (instance == null) {
                    instance = new ControlManager();
                }
            }
        }
        return instance;
    }

    private void loadDefaultKeybinds() {
        keybinds.put(MovementStatus.MOVE_LEFT, Keys.LEFT);
        keybinds.put(MovementStatus.MOVE_RIGHT, Keys.RIGHT);
        keybinds.put(MovementStatus.JUMP, Keys.UP);
        keybinds.put(MovementStatus.LOOK_UP, Keys.W);
        keybinds.put(MovementStatus.LOOK_DOWN, Keys.S);
        keybinds.put(MovementStatus.DASH, Keys.SHIFT_LEFT);
        keybinds.put(MovementStatus.NAIL_ATTACK, Keys.X);
        keybinds.put(MovementStatus.FOCUS, Keys.A);
    }

    public boolean isActionPressed(MovementStatus action) {
        Integer keyCode = keybinds.get(action);
        return keyCode != null && Gdx.input.isKeyPressed(keyCode);
    }

    public boolean isActionJustPressed(MovementStatus action) {
        Integer keyCode = keybinds.get(action);
        return keyCode != null && Gdx.input.isKeyJustPressed(keyCode);
    }

    public void updateKeybind(MovementStatus action, int newKeyCode) {
        if (keybinds.containsKey(action)) {
            keybinds.put(action, newKeyCode);
        }
    }

    public String getKeyDisplayName(MovementStatus action) {
        Integer keyCode = keybinds.get(action);
        return (keyCode != null) ? Keys.toString(keyCode) : "NONE";
    }
}

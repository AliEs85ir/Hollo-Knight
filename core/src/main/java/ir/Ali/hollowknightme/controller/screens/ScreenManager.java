package ir.Ali.hollowknightme.controller.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.Array;
import ir.Ali.hollowknightme.view.screens.BaseScreen;

public class ScreenManager {
    private static ScreenManager instance;
    private Game game;
    private final Array<BaseScreen> screenHistory = new Array<>();

    private ScreenManager() {}

    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    public void initialize(Game game) {
        this.game = game;
    }

    public void setScreen(BaseScreen screen) {
        if (game != null) {
            clearHistory();
            game.setScreen(screen);
        }
    }

    public void pushScreen(BaseScreen subScreen) {
        if (game != null) {
            BaseScreen currentScreen = (BaseScreen) game.getScreen();
            if (currentScreen != null) {
                screenHistory.add(currentScreen);
            }
            game.setScreen(subScreen);
        }
    }

    public void popScreen() {
        if (game != null && screenHistory.size > 0) {
            BaseScreen currentScreen = (BaseScreen) game.getScreen();
            BaseScreen previousScreen = screenHistory.pop();

            game.setScreen(previousScreen);

            if (currentScreen != null) {
                currentScreen.dispose();
            }
        }
    }

    public void clearHistory() {
        screenHistory.clear();
    }
}

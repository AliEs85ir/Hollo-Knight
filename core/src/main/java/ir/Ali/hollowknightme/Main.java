package ir.Ali.hollowknightme;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import ir.Ali.hollowknightme.controller.screens.ScreenManager;
import ir.Ali.hollowknightme.controller.sound.AudioManager;
import ir.Ali.hollowknightme.controller.ui.UIManager;
import ir.Ali.hollowknightme.enums.sound.MusicType;
import ir.Ali.hollowknightme.view.screens.MainMenuScreen;

public class Main extends Game {
    @Override
    public void create() {
        UIManager.load();
        ScreenManager.getInstance().initialize(this);
        ScreenManager.getInstance().setScreen(new MainMenuScreen());
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        UIManager.dispose();
    }
}

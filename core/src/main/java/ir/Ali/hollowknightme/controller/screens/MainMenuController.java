package ir.Ali.hollowknightme.controller.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import ir.Ali.hollowknightme.controller.ui.PanelManager;
import ir.Ali.hollowknightme.view.screens.MainMenuScreen;
import ir.Ali.hollowknightme.view.screens.StartGameScreen;

public class MainMenuController {
    private final MainMenuScreen view;

    public MainMenuController(MainMenuScreen view) {
        this.view = view;
        bindEvents();
    }

    private void bindEvents() {
        view.getStartGameBtn().addClickListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().pushScreen(new StartGameScreen());
            }
        });

        view.getOptionsBtn().addClickListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PanelManager.getInstance().getSettingsPanel().show(view.getStage());
            }
        });

        view.getAchievementsBtn().addClickListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PanelManager.getInstance().getAchievementPanel().show(view.getStage());
            }
        });

        view.getGiudBtn().addClickListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PanelManager.getInstance().getGuidePanel().show(view.getStage());
            }
        });

        view.getQuitGameBtn().addClickListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }
}

package ir.Ali.hollowknightme.controller.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import ir.Ali.hollowknightme.view.popup.AchievementPanel;

public class AchievementController {
    private final AchievementPanel view;

    public AchievementController(AchievementPanel view) {
        this.view = view;
        bindEvents();
    }

    private void bindEvents() {
        view.getMainPanel().getBackButton().addClickListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                view.hide(null);
            }
        });
    }
}

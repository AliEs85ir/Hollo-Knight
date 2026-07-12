package ir.Ali.hollowknightme.view.popup;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import ir.Ali.hollowknightme.controller.ui.UIManager;
import ir.Ali.hollowknightme.view.screens.BaseScreen;

public class AchievementPopup extends Table {

    private AchievementPopup(String title, String description) {
        super(UIManager.getSkin());

        this.setBackground((com.badlogic.gdx.scenes.scene2d.utils.Drawable) null);

        Label titleLabel = new Label("ACHIEVEMENT UNLOCKED", UIManager.getSkin(), "Title");
        titleLabel.setColor(Color.WHITE);
        //titleLabel.setFontScale(1.4f);

        Label detailLabel = new Label(title + " : " + description, UIManager.getSkin(), "Text");
        detailLabel.setColor(Color.WHITE);
        //detailLabel.setFontScale(1.1f);


        this.add(titleLabel).left().padBottom(4f).row();
        this.add(detailLabel).left();
        this.pack();

        float posX = 40f;
        float posY = 40f;
        this.setPosition(posX, posY);

        this.setColor(1f, 1f, 1f, 0f);
        this.addAction(Actions.sequence(
            Actions.fadeIn(0.4f, com.badlogic.gdx.math.Interpolation.fade),
            Actions.delay(3.0f),
            Actions.fadeOut(0.4f, com.badlogic.gdx.math.Interpolation.fade),
            Actions.removeActor()
        ));
    }



    public static void show(String title, String description) {
        if (com.badlogic.gdx.Gdx.app.getApplicationListener() instanceof com.badlogic.gdx.Game) {
            com.badlogic.gdx.Game game = (com.badlogic.gdx.Game) com.badlogic.gdx.Gdx.app.getApplicationListener();
            if (game.getScreen() instanceof BaseScreen) {
                Stage currentStage = ((BaseScreen) game.getScreen()).getStage();
                if (currentStage != null) {
                    AchievementPopup popup = new AchievementPopup(title, description);
                    currentStage.addActor(popup);
                }
            }
        }
    }
}

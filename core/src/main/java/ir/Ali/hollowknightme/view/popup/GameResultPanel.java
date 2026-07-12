package ir.Ali.hollowknightme.view.popup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import ir.Ali.hollowknightme.controller.game.GameProgressManager;
import ir.Ali.hollowknightme.controller.ui.UIManager;
import ir.Ali.hollowknightme.model.ui.TextBtn;

public class GameResultPanel extends Table {
    private final TextBtn mainMenuBtn;
    private InputProcessor previousInputProcessor;

    public GameResultPanel(boolean isWin) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0f, 0f, 0f, 0.85f));
        pixmap.fill();
        Texture bgTexture = new Texture(pixmap);
        pixmap.dispose();
        this.setBackground(new TextureRegionDrawable(bgTexture));

        this.setTouchable(Touchable.enabled);
        this.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        String titleText = isWin ? "VICTORY" : "GAME OVER";
        Label titleLabel = new Label(titleText, UIManager.getSkin(), "Text");
        titleLabel.setColor(isWin ? Color.GREEN : Color.RED);

        GameProgressManager progress = GameProgressManager.getInstance();
        int deaths = progress.getTotalDeaths();
        int kills = progress.getTotalEnemiesKilled();

        long totalSeconds = progress.getPlayTimeSeconds();
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        String timeFormatted = minutes + " Min, " + seconds + " Sec";

        Label deathsLabel = new Label("Total Deaths: " + deaths, UIManager.getSkin(), "Text");
        Label killsLabel = new Label("Enemies Defeated: " + kills, UIManager.getSkin(), "Text");
        Label timeLabel = new Label("Play Time: " + timeFormatted, UIManager.getSkin(), "Text");

        mainMenuBtn = new TextBtn("MAIN MENU", -100f);

        this.add(titleLabel).padBottom(50f).row();
        this.add(deathsLabel).padBottom(15f).row();
        this.add(killsLabel).padBottom(15f).row();
        this.add(timeLabel).padBottom(50f).row();
        this.add(mainMenuBtn);
    }

    public void show(Stage stage) {
        this.previousInputProcessor = Gdx.input.getInputProcessor();
        stage.addActor(this);
        this.setSize(stage.getWidth(), stage.getHeight());
        this.setPosition(0, 0);
        this.getColor().a = 0;
        this.addAction(Actions.fadeIn(0.3f));
        Gdx.input.setInputProcessor(stage);
    }

    public void hide(Runnable onComplete) {
        this.addAction(Actions.sequence(
            Actions.fadeOut(0.2f),
            Actions.run(() -> {
                if (previousInputProcessor != null) {
                    Gdx.input.setInputProcessor(previousInputProcessor);
                }
                if (onComplete != null) {
                    onComplete.run();
                }
            }),
            Actions.removeActor()
        ));
    }

    public TextBtn getMainMenuBtn() { return mainMenuBtn; }
}

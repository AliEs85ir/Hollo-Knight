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
import ir.Ali.hollowknightme.controller.ui.UIManager;
import ir.Ali.hollowknightme.model.ui.TextBtn;

public class PauseMenuPanel extends Table {
    private final TextBtn resumeBtn;
    private final TextBtn settingsBtn;
    private final TextBtn saveExitBtn;
    private final TextBtn cheatCodesBtn;
    private InputProcessor previousInputProcessor;

    public PauseMenuPanel() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0f, 0f, 0f, 0.8f));
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

        Label titleLabel = new Label("GAME PAUSED", UIManager.getSkin(), "Text");

        resumeBtn = new TextBtn("RESUME", -100f);
        settingsBtn = new TextBtn("SETTINGS", -100f);
        cheatCodesBtn = new TextBtn("CHEAT CODES", -100f);
        saveExitBtn = new TextBtn("SAVE & EXIT", -100f);

        this.add(titleLabel).padBottom(60f).row();
        this.add(resumeBtn).padBottom(20f).row();
        this.add(settingsBtn).padBottom(20f).row();
        this.add(cheatCodesBtn).padBottom(20f).row();
        this.add(saveExitBtn);
    }

    public void show(Stage stage) {
        this.previousInputProcessor = Gdx.input.getInputProcessor();
        stage.addActor(this);
        this.setSize(stage.getWidth(), stage.getHeight());
        this.setPosition(0, 0);
        this.getColor().a = 0;
        this.addAction(Actions.fadeIn(0.15f));
        Gdx.input.setInputProcessor(stage);
    }

    public void hide(Runnable onComplete) {
        this.addAction(Actions.sequence(
            Actions.fadeOut(0.15f),
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

    public TextBtn getResumeBtn() { return resumeBtn; }
    public TextBtn getSettingsBtn() { return settingsBtn; }
    public TextBtn getSaveExitBtn() { return saveExitBtn; }
    public TextBtn getCheatCodesBtn() { return cheatCodesBtn; }
}

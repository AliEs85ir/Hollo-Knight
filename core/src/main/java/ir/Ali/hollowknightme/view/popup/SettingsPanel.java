package ir.Ali.hollowknightme.view.popup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import ir.Ali.hollowknightme.controller.screens.SettingsController;
import ir.Ali.hollowknightme.controller.ui.UIManager;
import ir.Ali.hollowknightme.model.ui.CheckBoxDi;
import ir.Ali.hollowknightme.model.ui.HollowMenuPanel;
import ir.Ali.hollowknightme.model.ui.TextBtn;

public class SettingsPanel extends Table {
    private HollowMenuPanel mainPanel;
    private Slider musicVolumeSlider;
    private Slider brightnessSlider;
    private CheckBoxDi musicMuteCheck;
    private CheckBoxDi sfxMuteCheck;
    private TextBtn resetSoundsBtn;
    private TextBtn keyBindsBtn;
    private TextBtn resetBindsBtn;
    private TextBtn languageBtn;
    private SettingsController controller;
    private InputProcessor previousInputProcessor;

    public SettingsPanel() {
        this.setFillParent(true);
        this.setTouchable(Touchable.enabled);
        TextureRegion bgRegion = UIManager.getAtlas().findRegion("main_menu_bg");
        if (bgRegion != null) {
            this.setBackground(new com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable(bgRegion));
        }
        this.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        mainPanel = new HollowMenuPanel("SETTINGS");
        Table bodyTable = new Table();

        Label musicVolLabel = new Label("Music Volume", UIManager.getSkin(), "Text");
        musicVolumeSlider = new Slider(0f, 1f, 0.1f, false, UIManager.getSkin(), "slider");

        Label brightnessLabel = new Label("Brightness", UIManager.getSkin(), "Text");
        brightnessSlider = new Slider(0.1f, 1f, 0.1f, false, UIManager.getSkin(), "slider");

        musicMuteCheck = new CheckBoxDi("Mute Music", "Disabled");
        sfxMuteCheck = new CheckBoxDi("Mute SFX", "Disabled");

        resetSoundsBtn = new TextBtn("RESET", -200f);
        keyBindsBtn = new TextBtn("CHANGE", -200f);
        resetBindsBtn = new TextBtn("RESET", -200f);
        languageBtn = new TextBtn("CHANGE LANGUAGE", -200f);

        bodyTable.add(musicVolLabel).left().padRight(20f);
        bodyTable.add(musicVolumeSlider).left().width(350f).padRight(20f);
        bodyTable.add(resetSoundsBtn).row();

        bodyTable.add(brightnessLabel).left().padRight(20f);
        bodyTable.add(brightnessSlider).width(350f);
        bodyTable.add(resetBindsBtn).row();

        bodyTable.add(musicMuteCheck).left().padBottom(15f).colspan(2).row();
        bodyTable.add(sfxMuteCheck).left().padBottom(30f).colspan(2).row();

        mainPanel.setBody(bodyTable);
        this.add(mainPanel).expand().fill();

        this.controller = new SettingsController(this);
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

    public HollowMenuPanel getMainPanel() { return mainPanel; }
    public Slider getMusicVolumeSlider() { return musicVolumeSlider; }
    public Slider getBrightnessSlider() { return brightnessSlider; }
    public CheckBoxDi getMusicMuteCheck() { return musicMuteCheck; }
    public CheckBoxDi getSfxMuteCheck() { return sfxMuteCheck; }
    public TextBtn getResetSoundsBtn() { return resetSoundsBtn; }
    public TextBtn getKeyBindsBtn() { return keyBindsBtn; }
    public TextBtn getResetBindsBtn() { return resetBindsBtn; }
    public TextBtn getLanguageBtn() { return languageBtn; }
}

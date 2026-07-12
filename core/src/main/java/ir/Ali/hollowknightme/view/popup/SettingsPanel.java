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
import ir.Ali.hollowknightme.controller.sound.AudioManager;
import ir.Ali.hollowknightme.controller.sound.GameSettings;
import ir.Ali.hollowknightme.controller.ui.UIManager;
import ir.Ali.hollowknightme.model.ui.CheckBoxDi;
import ir.Ali.hollowknightme.model.ui.HollowMenuPanel;
import ir.Ali.hollowknightme.model.ui.TextBtn;

public class SettingsPanel extends Table {
    private HollowMenuPanel mainPanel;
    private Slider musicVolumeSlider;
    private Slider sfxVolumeSlider;
    private Slider brightnessSlider;
    private CheckBoxDi musicMuteCheck;
    private CheckBoxDi sfxMuteCheck;
    private TextBtn resetBtn;
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

        GameSettings settings = AudioManager.getInstance().getSettings();

        Label musicVolLabel = new Label("Music Volume", UIManager.getSkin(), "Text");
        musicVolumeSlider = new Slider(0f, 1f, 0.05f, false, UIManager.getSkin(), "slider");
        musicVolumeSlider.setValue(settings.getMusicVolume());

        Label sfxVolLabel = new Label("SFX Volume", UIManager.getSkin(), "Text");
        sfxVolumeSlider = new Slider(0f, 1f, 0.05f, false, UIManager.getSkin(), "slider");
        sfxVolumeSlider.setValue(settings.getSfxVolume());

        Label brightnessLabel = new Label("Brightness", UIManager.getSkin(), "Text");
        brightnessSlider = new Slider(0.2f, 1.5f, 0.05f, false, UIManager.getSkin(), "slider");
        brightnessSlider.setValue(settings.getBrightness());

        musicMuteCheck = new CheckBoxDi("Mute Music", "Disabled");
        musicMuteCheck.setChecked(settings.isMusicMute());

        sfxMuteCheck = new CheckBoxDi("Mute SFX", "Disabled");
        sfxMuteCheck.setChecked(settings.isSfxMute());

        resetBtn = new TextBtn("RESET TO DEFAULT", -200f);

        bodyTable.add(musicVolLabel).left().padRight(20f).padBottom(10f);
        bodyTable.add(musicVolumeSlider).left().width(350f).padBottom(10f).row();

        bodyTable.add(sfxVolLabel).left().padRight(20f).padBottom(10f);
        bodyTable.add(sfxVolumeSlider).left().width(350f).padBottom(10f).row();

        bodyTable.add(brightnessLabel).left().padRight(20f).padBottom(20f);
        bodyTable.add(brightnessSlider).left().width(350f).padBottom(20f).row();

        bodyTable.add(musicMuteCheck).left().padBottom(15f).colspan(2).row();
        bodyTable.add(sfxMuteCheck).left().padBottom(30f).colspan(2).row();
        bodyTable.add(resetBtn).center().colspan(2).padTop(10f).row();

        mainPanel.setBody(bodyTable);
        this.add(mainPanel).expand().fill();

        this.controller = new SettingsController(this);
    }

    public void updateUIFromSettings() {
        GameSettings settings = AudioManager.getInstance().getSettings();
        musicVolumeSlider.setValue(settings.getMusicVolume());
        sfxVolumeSlider.setValue(settings.getSfxVolume());
        brightnessSlider.setValue(settings.getBrightness());
        musicMuteCheck.setChecked(settings.isMusicMute());
        sfxMuteCheck.setChecked(settings.isSfxMute());
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
        AudioManager.getInstance().getSettings().save();
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
    public Slider getSfxVolumeSlider() { return sfxVolumeSlider; }
    public Slider getBrightnessSlider() { return brightnessSlider; }
    public CheckBoxDi getMusicMuteCheck() { return musicMuteCheck; }
    public CheckBoxDi getSfxMuteCheck() { return sfxMuteCheck; }
    public TextBtn getResetBtn() { return resetBtn; }
}

package ir.Ali.hollowknightme.controller.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import ir.Ali.hollowknightme.controller.sound.AudioManager;
import ir.Ali.hollowknightme.controller.sound.GameSettings;
import ir.Ali.hollowknightme.view.popup.SettingsPanel;

public class SettingsController {
    private final SettingsPanel view;

    public SettingsController(SettingsPanel view) {
        this.view = view;
        bindEvents();
    }

    private void bindEvents() {
        GameSettings settings = AudioManager.getInstance().getSettings();

        view.getMainPanel().getBackButton().addClickListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                view.hide(null);
            }
        });

        view.getMusicVolumeSlider().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setMusicVolume(view.getMusicVolumeSlider().getValue());
            }
        });

        view.getSfxVolumeSlider().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setSfxVolume(view.getSfxVolumeSlider().getValue());
            }
        });

        view.getBrightnessSlider().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setBrightness(view.getBrightnessSlider().getValue());
                applyBrightnessEffect(settings.getBrightness());
            }
        });

        view.getMusicMuteCheck().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                settings.setMusicMute(view.getMusicMuteCheck().isChecked());
            }
        });

        view.getSfxMuteCheck().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                settings.setSfxMute(view.getSfxMuteCheck().isChecked());
            }
        });

        view.getResetMusicBtn().addClickListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                settings.resetMusic();
                view.updateUIFromSettings();
            }
        });

        view.getResetSfxBtn().addClickListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                settings.resetSfx();
                view.updateUIFromSettings();
            }
        });

        view.getResetBrightnessBtn().addClickListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                settings.resetBrightness();
                view.updateUIFromSettings();
                applyBrightnessEffect(settings.getBrightness());
            }
        });

        view.getResetAllBtn().addClickListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                settings.reset();
                view.updateUIFromSettings();
                applyBrightnessEffect(settings.getBrightness());
            }
        });
    }

    private void applyBrightnessEffect(float value) {

    }
}

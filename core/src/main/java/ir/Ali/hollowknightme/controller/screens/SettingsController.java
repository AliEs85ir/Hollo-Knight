package ir.Ali.hollowknightme.controller.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import ir.Ali.hollowknightme.view.popup.SettingsPanel;

public class SettingsController {
    private final SettingsPanel view;

    public SettingsController(SettingsPanel view) {
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
        view.getMusicVolumeSlider().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            }
        });
        view.getBrightnessSlider().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            }
        });
        view.getMusicMuteCheck().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            }
        });
        view.getSfxMuteCheck().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            }
        });
        view.getResetSoundsBtn().addClickListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            }
        });
        view.getKeyBindsBtn().addClickListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            }
        });
        view.getResetBindsBtn().addClickListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            }
        });
        view.getLanguageBtn().addClickListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            }
        });
    }
}

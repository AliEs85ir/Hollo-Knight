package ir.Ali.hollowknightme.controller.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GameSettings {
    private static final String PREF_NAME = "HollowKnightMeSettings";

    private float masterVolume;
    private float musicVolume;
    private float sfxVolume;
    private float brightness;

    private boolean masterMute;
    private boolean musicMute;
    private boolean sfxMute;

    public void load() {
        Preferences prefs = Gdx.app.getPreferences(PREF_NAME);
        masterVolume = prefs.getFloat("masterVolume", 1.0f);
        musicVolume = prefs.getFloat("musicVolume", 0.7f);
        sfxVolume = prefs.getFloat("sfxVolume", 0.8f);
        brightness = prefs.getFloat("brightness", 1.0f);

        masterMute = prefs.getBoolean("masterMute", false);
        musicMute = prefs.getBoolean("musicMute", false);
        sfxMute = prefs.getBoolean("sfxMute", false);
    }

    public void save() {
        Preferences prefs = Gdx.app.getPreferences(PREF_NAME);
        prefs.putFloat("masterVolume", masterVolume);
        prefs.putFloat("musicVolume", musicVolume);
        prefs.putFloat("sfxVolume", sfxVolume);
        prefs.putFloat("brightness", brightness);

        prefs.putBoolean("masterMute", masterMute);
        prefs.putBoolean("musicMute", musicMute);
        prefs.putBoolean("sfxMute", sfxMute);

        prefs.flush();
    }

    public void reset() {
        masterVolume = 1.0f;
        musicVolume = 0.7f;
        sfxVolume = 0.8f;
        brightness = 1.0f;
        masterMute = false;
        musicMute = false;
        sfxMute = false;
    }

    public void resetMusic() {
        musicVolume = 0.7f;
    }

    public void resetSfx() {
        sfxVolume = 0.8f;
    }

    public void resetBrightness() {
        brightness = 1.0f;
    }

    public float getMasterVolume() { return masterMute ? 0f : masterVolume; }
    public void setMasterVolume(float masterVolume) { this.masterVolume = masterVolume; }

    public float getMusicVolume() { return musicMute ? 0f : musicVolume; }
    public void setMusicVolume(float musicVolume) { this.musicVolume = musicVolume; }

    public float getSfxVolume() { return sfxMute ? 0f : sfxVolume; }
    public void setSfxVolume(float sfxVolume) { this.sfxVolume = sfxVolume; }

    public float getBrightness() { return brightness; }
    public void setBrightness(float brightness) { this.brightness = brightness; }

    public boolean isMasterMute() { return masterMute; }
    public void setMasterMute(boolean masterMute) { this.masterMute = masterMute; }

    public boolean isMusicMute() { return musicMute; }
    public void setMusicMute(boolean musicMute) { this.musicMute = musicMute; }

    public boolean isSfxMute() { return sfxMute; }
    public void setSfxMute(boolean sfxMute) { this.sfxMute = sfxMute; }
}

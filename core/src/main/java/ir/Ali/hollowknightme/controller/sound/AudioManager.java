package ir.Ali.hollowknightme.controller.sound;

import ir.Ali.hollowknightme.enums.sound.MusicType;
import ir.Ali.hollowknightme.enums.sound.SfxType;

public class AudioManager {
    private static AudioManager instance;

    private final AudioSettings settings;
    private final SoundManager soundManager;
    private final MusicManager musicManager;

    private AudioManager() {
        this.settings = new AudioSettings();
        this.settings.load();

        this.soundManager = new SoundManager(this.settings);
        this.musicManager = new MusicManager(this.settings);
    }

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }
    public void playSfx(SfxType type) { soundManager.play(type); }
    public void loopSfx(SfxType type) { soundManager.loop(type); }
    public void stopLoop(SfxType type) { soundManager.stopLoop(type); }
    public void stopAllLoops() { soundManager.stopAllLoops(); }
    public void playMusic(MusicType type) { musicManager.play(type); }
    public void stopMusic() { musicManager.stop(); }
    public void pause() { musicManager.pause(); }
    public void resume() { musicManager.resume(); }
    public void update(float delta) { musicManager.update(delta); }
    public AudioSettings getSettings() { return settings; }

    public void dispose() {
        settings.save();
        soundManager.dispose();
        musicManager.dispose();
    }
}

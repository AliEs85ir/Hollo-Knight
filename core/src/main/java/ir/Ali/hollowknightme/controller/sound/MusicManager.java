package ir.Ali.hollowknightme.controller.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import ir.Ali.hollowknightme.enums.sound.MusicType;

import java.util.EnumMap;
import java.util.Map;

public class MusicManager {
    private final Map<MusicType, Music> musics;
    private final GameSettings settings;

    private MusicType currentType;
    private MusicType nextType;

    private float fadeTimer;
    private final float fadeDuration = 3.0f;
    private boolean isCrossFading;

    public MusicManager(GameSettings settings) {
        this.settings = settings;
        this.musics = new EnumMap<>(MusicType.class);
        loadMusics();
    }

    private void loadMusics() {
        for (MusicType type : MusicType.values()) {
            Music music = Gdx.audio.newMusic(Gdx.files.internal("sound/Music/" + type.getFileName()));
            music.setLooping(true);
            musics.put(type, music);
        }
    }

    public void play(MusicType type) {
        if (currentType == type || nextType == type) {
            return;
        }

        if (currentType == null) {
            currentType = type;
            Music currentMusic = musics.get(currentType);
            if (currentMusic != null) {
                currentMusic.setVolume(calculateVolume(currentType, 1.0f));
                currentMusic.play();
            }
        } else {
            if (isCrossFading && nextType != null) {
                Music oldCurrent = musics.get(currentType);
                if (oldCurrent != null) {
                    oldCurrent.stop();
                }
                currentType = nextType;
            }

            nextType = type;
            isCrossFading = true;
            fadeTimer = 0f;
            Music nextMusic = musics.get(nextType);
            if (nextMusic != null) {
                nextMusic.setVolume(0f);
                nextMusic.play();
            }
        }
    }

    public void pause() {
        if (currentType != null) musics.get(currentType).pause();
        if (nextType != null) musics.get(nextType).pause();
    }

    public void resume() {
        if (currentType != null) musics.get(currentType).play();
        if (nextType != null) musics.get(nextType).play();
    }

    public void stop() {
        if (currentType != null) {
            musics.get(currentType).stop();
            currentType = null;
        }
        if (nextType != null) {
            musics.get(nextType).stop();
            nextType = null;
        }
        isCrossFading = false;
    }

    public void update(float delta) {
        if (!isCrossFading && currentType != null) {
            Music currentMusic = musics.get(currentType);
            if (currentMusic != null) {
                currentMusic.setVolume(calculateVolume(currentType, 1.0f));
            }
            return;
        }

        if (isCrossFading) {
            fadeTimer += delta;
            float progress = Math.min(fadeTimer / fadeDuration, 1.0f);

            Music currentMusic = musics.get(currentType);
            Music nextMusic = musics.get(nextType);

            if (currentMusic != null) {
                currentMusic.setVolume(calculateVolume(currentType, 1.0f - progress));
            }
            if (nextMusic != null) {
                nextMusic.setVolume(calculateVolume(nextType, progress));
            }

            if (progress >= 1.0f) {
                if (currentMusic != null) {
                    currentMusic.stop();
                }
                currentType = nextType;
                nextType = null;
                isCrossFading = false;
            }
        }
    }

    private float calculateVolume(MusicType type, float fadeMultiplier) {
        return type.getBaseVolume() * settings.getMasterVolume() * settings.getMusicVolume() * fadeMultiplier;
    }

    public void dispose() {
        stop();
        for (Music music : musics.values()) {
            music.dispose();
        }
        musics.clear();
    }
}

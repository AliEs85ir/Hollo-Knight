package ir.Ali.hollowknightme.controller.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import ir.Ali.hollowknightme.enums.sound.SfxType;
import java.util.EnumMap;
import java.util.Map;


public class SoundManager {
    private final Map<SfxType, Sound> sounds;
    private final Map<SfxType, Long> activeLoops;
    private final AudioSettings settings;

    public SoundManager(AudioSettings settings) {
        this.settings = settings;
        this.sounds = new EnumMap<>(SfxType.class);
        this.activeLoops = new EnumMap<>(SfxType.class);
        loadSounds();
    }

    private void loadSounds() {
        for (SfxType type : SfxType.values()) {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal("sound/SFX/" + type.getFileName()));
            sounds.put(type, sound);
        }
    }

    public void play(SfxType type) {
        float volume = calculateVolume(type);
        if (volume > 0f) {
            sounds.get(type).play(volume);
        }
    }

    public void loop(SfxType type) {
        if (!type.isLoop() || activeLoops.containsKey(type)) {
            return;
        }

        float volume = calculateVolume(type);
        if (volume > 0f) {
            long id = sounds.get(type).loop(volume);
            activeLoops.put(type, id);
        }
    }

    public void stopLoop(SfxType type) {
        Long id = activeLoops.remove(type);
        if (id != null) {
            sounds.get(type).stop(id);
        }
    }

    public void stopAllLoops() {
        for (Map.Entry<SfxType, Long> entry : activeLoops.entrySet()) {
            sounds.get(entry.getKey()).stop(entry.getValue());
        }
        activeLoops.clear();
    }

    private float calculateVolume(SfxType type) {
        return type.getBaseVolume() * settings.getMasterVolume() * settings.getSfxVolume();
    }

    public void dispose() {
        stopAllLoops();
        for (Sound sound : sounds.values()) {
            sound.dispose();
        }
        sounds.clear();
    }
}

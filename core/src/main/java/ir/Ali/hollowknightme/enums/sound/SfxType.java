package ir.Ali.hollowknightme.enums.sound;

public enum SfxType {
    ENEMY_DEATH("enemyDeath.wav", 0.8f, false),
    CLICK_UI("clickUI.wav", 1.0f, false),
    BUTTON_UI("buttonUI.wav", 1.0f, false),
    SOUL("soul.wav", 0.7f, false),
    ENEMY_DAMAGE("enemyDamage.wav", 0.4f, false),
    HERO_DAMAGE("heroDamage.wav", 0.9f, false),
    WALL_SLIDE("wallSlide.wav", 0.5f, true),
    TRANSITION("transition.wav", 1.0f, false),
    LAND("land.wav", 0.6f, false),
    DOUBLE_JUMP("doubleJump.wav", 0.6f, false),
    JUMP("jump.wav", 0.5f, false),
    WALK("walk.wav", 0.4f, true),
    KNIGHT_DEATH("knightDeath.wav", 1.0f, false),
    DASH("dash.wav", 0.6f, false),
    NAIL("nail.wav", 1.0f, false);

    private final String fileName;
    private final float baseVolume;
    private final boolean loop;

    SfxType(String fileName, float baseVolume, boolean loop) {
        this.fileName = fileName;
        this.baseVolume = baseVolume;
        this.loop = loop;
    }

    public String getFileName() {
        return fileName;
    }

    public float getBaseVolume() {
        return baseVolume;
    }

    public boolean isLoop() {
        return loop;
    }
}

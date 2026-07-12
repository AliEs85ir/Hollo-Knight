package ir.Ali.hollowknightme.enums.sound;

public enum MusicType {
    ENTER("Enter.mp3", 0.2f),
    FORGOTTEN_CROSSROADS("ForgottenCrossroads.mp3", 0.3f),
    CITY_OF_TEARS("CityOfTears.mp3", 0.3f),
    BOSS("Boss.mp3", 0.4f),
    WIN("Win.mp3", 0.5f),
    GAME_OVER("GameOver.mp3", 0.5f);

    private final String fileName;
    private final float baseVolume;

    MusicType(String fileName, float baseVolume) {
        this.fileName = fileName;
        this.baseVolume = baseVolume;
    }

    public String getFileName() {
        return fileName;
    }

    public float getBaseVolume() {
        return baseVolume;
    }
}

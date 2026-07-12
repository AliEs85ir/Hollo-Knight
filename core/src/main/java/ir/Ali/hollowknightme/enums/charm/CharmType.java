package ir.Ali.hollowknightme.enums.charm;

public enum CharmType {
    SOUL_CATCHER("SoulCatcher", "Increases the amount of Soul gained when striking an enemy with the nail."),
    DASHMASTER("Dashmaster", "Allows the bearer to dash more frequently."),
    UNBREAKABLE_STRENGTH("UnbreakableStrength", "Strengthens the bearer, increasing the damage they deal to enemies with the nail."),
    QUICK_FLASH("QuickSlash", "Increases the attack speed of the nail, reducing the cooldown between slashes."),
    QUICK_FOCUS("QuickFocus", "Increases the speed of focusing Soul, allowing the bearer to heal faster."),
    HEAVY_BLOW("HeavyBlow", "Increases the knockback force of the nail, sending enemies flying further.");

    private final String regionName;
    private final String description;

    CharmType(String regionName, String description) {
        this.regionName = regionName;
        this.description = description;
    }

    public String getRegionName() {
        return regionName;
    }

    public String getDescription() {
        return description;
    }
}

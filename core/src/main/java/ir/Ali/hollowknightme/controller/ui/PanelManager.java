package ir.Ali.hollowknightme.controller.ui;

import ir.Ali.hollowknightme.view.popup.AchievementPanel;
import ir.Ali.hollowknightme.view.popup.GuidePanel;
import ir.Ali.hollowknightme.view.popup.SettingsPanel;

public class PanelManager {
    private static PanelManager instance;

    private SettingsPanel settingsPanel;
    private AchievementPanel achievementPanel;
    private GuidePanel guidePanel;

    private PanelManager() {
    }

    public static PanelManager getInstance() {
        if (instance == null) {
            instance = new PanelManager();
        }
        return instance;
    }

    public SettingsPanel getSettingsPanel() {
        if (settingsPanel == null) {
            settingsPanel = new SettingsPanel();
        }
        return settingsPanel;
    }

    public AchievementPanel getAchievementPanel() {
        if (achievementPanel == null) {
            achievementPanel = new AchievementPanel();
        }
        return achievementPanel;
    }

    public GuidePanel getGuidePanel() {
        if (guidePanel == null) {
            guidePanel = new GuidePanel();
        }
        return guidePanel;
    }
}

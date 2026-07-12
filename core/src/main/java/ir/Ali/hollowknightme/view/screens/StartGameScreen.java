package ir.Ali.hollowknightme.view.screens;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import ir.Ali.hollowknightme.controller.screens.StartGameController;
import ir.Ali.hollowknightme.controller.ui.UIManager;
import ir.Ali.hollowknightme.model.ui.HollowMenuPanel;
import ir.Ali.hollowknightme.model.save.SaveModel;
import ir.Ali.hollowknightme.model.save.SaveSlotComponent;
import ir.Ali.hollowknightme.model.save.GameSaveData;
import ir.Ali.hollowknightme.service.SaveService;
import ir.Ali.hollowknightme.service.JsonSaveService;

public class StartGameScreen extends BaseScreen {
    private final HollowMenuPanel mainPanel;
    private final SaveSlotComponent[] slots;
    private final StartGameController controller;
    private Image backgroundImage;

    public StartGameScreen() {
        super();
        TextureRegion bgRegion = UIManager.getAtlas().findRegion("main_menu_bg");
        if(bgRegion != null) {
            backgroundImage = new Image(new TextureRegionDrawable(bgRegion));
            backgroundImage.setScaling(Scaling.fill);
            backgroundImage.setFillParent(true);
            stage.addActor(backgroundImage);
        }

        mainPanel = new HollowMenuPanel("SELECT PROFILE");
        slots = new SaveSlotComponent[4];
        Table bodyTable = new Table();

        SaveService saveService = new JsonSaveService();
        SaveModel[] actualModels = new SaveModel[4];

        for (int i = 0; i < 4; i++) {
            int slotNumber = i + 1;

            if (saveService.hasSaveSlot(slotNumber)) {
                GameSaveData data = saveService.load(slotNumber);

                long totalSeconds = data.getPlayTimeSeconds();
                long minutes = totalSeconds / 60;
                long seconds = totalSeconds % 60;
                String formattedTime = minutes + "m " + seconds + "s";

                String roomPath = data.getCurrentRoomPath().toLowerCase();
                String locationName = roomPath.replace(".tmx", "").toUpperCase();
                int progress = 0;

                if (roomPath.contains("room1")) progress = 5;
                else if (roomPath.contains("room2")) progress = 20;
                else if (roomPath.contains("room3")) progress = 40;
                else if (roomPath.contains("room4")) progress = 60;
                else if (roomPath.contains("room5")) progress = 80;
                else if (roomPath.contains("boss")) progress = 90;

                actualModels[i] = new SaveModel(
                    slotNumber,
                    locationName,
                    progress,
                    formattedTime,
                    data.getTotalDeaths(),
                    data.getTotalEnemiesKilled(),
                    data.getKnightHealth(),
                    data.getKnightSoul()
                );
            } else {
                actualModels[i] = new SaveModel(slotNumber);
            }
        }

        for (int i = 0; i < 4; i++) {
            slots[i] = new SaveSlotComponent(actualModels[i]);
            bodyTable.add(slots[i]).padBottom(25f).row();
        }

        mainPanel.setBody(bodyTable);
        stage.addActor(mainPanel);
        controller = new StartGameController(this);
    }

    public HollowMenuPanel getMainPanel() { return mainPanel; }
    public SaveSlotComponent[] getSlots() { return slots; }
}

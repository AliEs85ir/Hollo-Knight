package ir.Ali.hollowknightme.controller.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import ir.Ali.hollowknightme.controller.game.GameProgressManager;
import ir.Ali.hollowknightme.view.popup.AreYouSurePanel;
import ir.Ali.hollowknightme.model.save.SaveSlotComponent;
import ir.Ali.hollowknightme.model.save.GameSaveData;
import ir.Ali.hollowknightme.service.SaveService;
import ir.Ali.hollowknightme.service.JsonSaveService;
import ir.Ali.hollowknightme.view.screens.GameScreen;
import ir.Ali.hollowknightme.view.screens.StartGameScreen;

public class StartGameController {
    private final StartGameScreen view;
    private final SaveService saveService;

    public StartGameController(StartGameScreen view) {
        this.view = view;
        this.saveService = new JsonSaveService();
        initListeners();
    }

    private void initListeners() {
        view.getMainPanel().getBackButton().addClickListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().popScreen();
            }
        });

        for (SaveSlotComponent slotComponent : view.getSlots()) {
            slotComponent.getProfileClickZone().addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    int slotNumber = slotComponent.getModel().getSlotNumber();

                    if (slotComponent.getModel().isEmpty()) {
                        GameProgressManager.getInstance().startNewGame(slotNumber);
                        ScreenManager.getInstance().setScreen(new GameScreen());
                    } else {
                        GameSaveData savedData = saveService.load(slotNumber);
                        if (savedData != null) {
                            GameProgressManager.getInstance().loadFromSave(savedData);
                            ScreenManager.getInstance().setScreen(new GameScreen());
                        }
                    }
                }
            });

            slotComponent.getClearBtn().addClickListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    event.handle();
                    AreYouSurePanel popUp = new AreYouSurePanel();

                    popUp.getNoBtn().addClickListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent ev, float x, float y) {
                            popUp.hide(null);
                        }
                    });

                    popUp.getYesBtn().addClickListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent ev, float x, float y) {
                            int slotNumber = slotComponent.getModel().getSlotNumber();
                            saveService.deleteSlot(slotNumber);
                            slotComponent.getModel().clear();
                            slotComponent.updateModel(slotComponent.getModel());
                            popUp.hide(null);
                        }
                    });

                    popUp.show(view.getStage());
                }
            });
        }
    }
}

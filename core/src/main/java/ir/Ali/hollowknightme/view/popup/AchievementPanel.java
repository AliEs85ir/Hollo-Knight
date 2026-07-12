package ir.Ali.hollowknightme.view.popup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import ir.Ali.hollowknightme.controller.screens.AchievementController;
import ir.Ali.hollowknightme.controller.ui.UIManager;
import ir.Ali.hollowknightme.model.ui.HollowMenuPanel;
import ir.Ali.hollowknightme.model.save.GameSaveData;
import ir.Ali.hollowknightme.service.JsonSaveService;
import ir.Ali.hollowknightme.service.SaveService;

public class AchievementPanel extends Table {
    private final HollowMenuPanel mainPanel;
    private final AchievementController achievementController;
    private InputProcessor previousInputProcessor;
    private final SaveService saveService;

    public AchievementPanel() {
        this.setFillParent(true);
        this.setTouchable(Touchable.enabled);
        TextureRegion bgRegion = UIManager.getAtlas().findRegion("main_menu_bg");
        if (bgRegion != null) {
            this.setBackground(new com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable(bgRegion));
        }
        this.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        mainPanel = new HollowMenuPanel("ACHIEVEMENTS");
        saveService = new JsonSaveService();

        refreshAchievements();

        this.add(mainPanel).expand().fill();
        achievementController = new AchievementController(this);
    }

    public void refreshAchievements() {
        Table bodyTable = new Table();
        boolean achCompletion = false;
        boolean achSpeedrun = false;
        boolean achTrueHunter = false;
        boolean achFalseKnight = false;
        boolean achProPlayer = false;

        for (int i = 1; i <= 4; i++) {
            if (saveService.hasSaveSlot(i)) {
                GameSaveData data = saveService.load(i);
                if (data.isAchCompletion()) achCompletion = true;
                if (data.isAchSpeedrun()) achSpeedrun = true;
                if (data.isAchTrueHunter()) achTrueHunter = true;
                if (data.isAchFalseKnight()) achFalseKnight = true;
                if (data.isAchProPlayer()) achProPlayer = true;
            }
        }

        bodyTable.add(new AchievementComponent("Completion", "Finish the game by defeating Droofin Knight.", achCompletion)).padBottom(12f).row();
        bodyTable.add(new AchievementComponent("Speedrun", "Finish the game in under 2 minutes.", achSpeedrun)).padBottom(12f).row();
        bodyTable.add(new AchievementComponent("True Hunter", "Kill at least one of every enemy type.", achTrueHunter)).padBottom(12f).row();
        bodyTable.add(new AchievementComponent("Defeat False Knight", "Defeat the False Knight boss.", achFalseKnight)).padBottom(12f).row();
        bodyTable.add(new AchievementComponent("Professional Player", "Reach the boss room without a single death.", achProPlayer)).padBottom(12f);

        mainPanel.setBody(bodyTable);
    }

    public void show(Stage stage) {
        refreshAchievements();

        this.previousInputProcessor = Gdx.input.getInputProcessor();
        stage.addActor(this);
        this.setSize(stage.getWidth(), stage.getHeight());
        this.setPosition(0, 0);
        this.getColor().a = 0;
        this.addAction(Actions.fadeIn(0.15f));
        Gdx.input.setInputProcessor(stage);
    }

    public void hide(Runnable onComplete) {
        this.addAction(Actions.sequence(
            Actions.fadeOut(0.15f),
            Actions.run(() -> {
                if (previousInputProcessor != null) {
                    Gdx.input.setInputProcessor(previousInputProcessor);
                }
                if (onComplete != null) {
                    onComplete.run();
                }
            }),
            Actions.removeActor()
        ));
    }

    public HollowMenuPanel getMainPanel() { return mainPanel; }

    private static class AchievementComponent extends Table {
        private final Image frameImage;
        private final Image slotBgImage;
        private final Animation<TextureRegion> frameAnimation;
        private float stateTime = 0f;
        private boolean hasFrames = false;
        private final Table infoTable;
        private final Table profileClickZone;
        private final Image leftArrow;
        private final Image rightArrow;

        public AchievementComponent(String title, String desc, boolean isUnlocked) {
            Array<TextureRegion> frames = new Array<>();
            for (int i = 0; i <= 12; i++) {
                String frameName = String.format("profile_fleur00%02d", i);
                TextureRegion region = UIManager.getAtlas().findRegion(frameName);
                if (region != null) {
                    frames.add(region);
                }
            }
            frameAnimation = new Animation<>(0.06f, frames, Animation.PlayMode.NORMAL);
            hasFrames = frames.size > 0;

            frameImage = new Image();
            slotBgImage = new Image();

            leftArrow = new Image(UIManager.getSkin(), "arrow_left");
            rightArrow = new Image(UIManager.getSkin(), "arrow_right");
            leftArrow.setVisible(false);
            rightArrow.setVisible(false);

            Stack profileStack = new Stack();

            Table slotWrapper = new Table();
            slotWrapper.add(slotBgImage).padTop(25f);
            profileStack.add(slotWrapper);

            profileStack.add(frameImage);

            infoTable = new Table();
            infoTable.padTop(25f);
            profileStack.add(infoTable);

            profileClickZone = new Table();
            profileClickZone.add(leftArrow).padRight(20f);
            profileClickZone.add(profileStack).width(800f).height(120f);
            profileClickZone.add(rightArrow).padLeft(20f);

            this.add(profileClickZone).center();

            setupVisuals(title, desc, isUnlocked);
            setupHoverEngine();
        }

        private void setupVisuals(String title, String desc, boolean isUnlocked) {
            infoTable.clearChildren();

            if (isUnlocked) {
                TextureRegion bgRegion = UIManager.getAtlas().findRegion("Area_Dirtmouth");
                if (bgRegion != null) {
                    slotBgImage.setDrawable(new TextureRegionDrawable(bgRegion));
                }
            } else {
                slotBgImage.setDrawable(null);
            }

            Table textData = new Table();
            textData.left();

            Table mainRow = new Table();
            Label titleLabel = new Label(title, UIManager.getSkin(), "Text");
            Label statusLabel = new Label(isUnlocked ? "[ UNLOCKED ]" : "[ LOCKED ]", UIManager.getSkin(), "default");

            mainRow.add(titleLabel).left().expandX();
            mainRow.add(statusLabel).padRight(25f);
            textData.add(mainRow).width(580f).padBottom(6f).row();

            Table subRow = new Table();
            Label descLabel = new Label(desc, UIManager.getSkin(), "default");

            subRow.add(descLabel).left();
            textData.add(subRow).left();

            infoTable.add(textData).left().padLeft(25f);

            if (!isUnlocked) { this.setColor(new Color(0.35f, 0.35f, 0.35f, 0.6f)); }
        }

        private void setupHoverEngine() {
            profileClickZone.addListener(new ClickListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    super.enter(event, x, y, pointer, fromActor);
                    leftArrow.setVisible(true);
                    rightArrow.setVisible(true);
                    leftArrow.clearActions();
                    leftArrow.addAction(Actions.forever(Actions.sequence(
                        Actions.moveBy(-5f, 0f, 0.4f),
                        Actions.moveBy(5f, 0f, 0.4f)
                    )));
                    rightArrow.clearActions();
                    rightArrow.addAction(Actions.forever(Actions.sequence(
                        Actions.moveBy(5f, 0f, 0.4f),
                        Actions.moveBy(-5f, 0f, 0.4f)
                    )));
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    super.exit(event, x, y, pointer, toActor);
                    leftArrow.setVisible(false);
                    rightArrow.setVisible(false);
                    leftArrow.clearActions();
                    rightArrow.clearActions();
                }
            });
        }

        @Override
        public void act(float delta) {
            super.act(delta);
            stateTime += delta;
            if (hasFrames) {
                frameImage.setDrawable(new TextureRegionDrawable(frameAnimation.getKeyFrame(stateTime)));
            }
        }
    }
}

package ir.Ali.hollowknightme.model.save;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import ir.Ali.hollowknightme.controller.ui.UIManager;
import ir.Ali.hollowknightme.model.ui.TextBtn;

public class SaveSlotComponent extends Table {
    private SaveModel model;
    private final Image frameImage;
    private final Image slotBgImage;
    private final Animation<TextureRegion> frameAnimation;
    private float stateTime = 0f;
    private boolean hasFrames = false;
    private final Table infoTable;
    private final Table profileClickZone;
    private final TextBtn clearBtn;
    private final Image leftArrow;
    private final Image rightArrow;

    public SaveSlotComponent(SaveModel model) {
        this.model = model;

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
        profileClickZone.add(profileStack).width(650f).height(95f);
        profileClickZone.add(rightArrow).padLeft(20f);

        clearBtn = new TextBtn("CLEAR SAVE", -125f);
        clearBtn.setName("CLEAR SAVE");

        this.add(profileClickZone).left();
        this.add(clearBtn).padLeft(70f).right();

        setupHoverEngine();
        updateVisuals();
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

    public void updateModel(SaveModel newModel) {
        this.model = newModel;
        updateVisuals();
    }

    private void updateVisuals() {
        infoTable.clearChildren();
        String slotPrefix = model.getSlotNumber() + ".  ";

        if (model.isEmpty()) {
            slotBgImage.setDrawable(null);
            Label newGameLabel = new Label(slotPrefix + "NEW GAME", UIManager.getSkin(), "Text");
            infoTable.add(newGameLabel).left();
            clearBtn.setVisible(false);
        } else {
            TextureRegion bgRegion = UIManager.getAtlas().findRegion("Area_Forgotten Crossroads");
            if (bgRegion != null) {
                slotBgImage.setDrawable(new TextureRegionDrawable(bgRegion));
            }

            Table textData = new Table();
            textData.left();

            Table mainStatsRow = new Table();
            Label locationLabel = new Label(slotPrefix + model.getLocationName(), UIManager.getSkin(), "Text");
            Label progressLabel = new Label(model.getProgressPercentage() + "%", UIManager.getSkin(), "Text");
            Label timeLabel = new Label(model.getPlayTime(), UIManager.getSkin(), "Text");

            mainStatsRow.add(locationLabel).left().expandX();
            mainStatsRow.add(progressLabel).padRight(30f);
            mainStatsRow.add(timeLabel).padRight(15f);
            textData.add(mainStatsRow).width(580f).padBottom(4f).row();

            Table subStatsRow = new Table();
            Label healthLabel = new Label("HP: " + model.getCurrentHealth(), UIManager.getSkin(), "Text");
            Label soulLabel = new Label("SOUL: " + model.getCurrentSoul(), UIManager.getSkin(), "Text");
            Label deathsLabel = new Label("DEATHS: " + model.getDeathCount(), UIManager.getSkin(), "Text");
            Label killsLabel = new Label("KILLS: " + model.getEnemiesKilled(), UIManager.getSkin(), "Text");

            subStatsRow.add(healthLabel).padRight(25f);
            subStatsRow.add(soulLabel).padRight(25f);
            subStatsRow.add(deathsLabel).padRight(25f);
            subStatsRow.add(killsLabel);

            textData.add(subStatsRow).left();
            infoTable.add(textData).left().padLeft(25f);

            clearBtn.setVisible(true);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
        if (hasFrames) {
            frameImage.setDrawable(new TextureRegionDrawable(frameAnimation.getKeyFrame(stateTime)));
        }
    }

    public SaveModel getModel() { return model; }
    public Table getProfileClickZone() { return profileClickZone; }
    public TextBtn getClearBtn() { return clearBtn; }
}

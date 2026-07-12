package ir.Ali.hollowknightme.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import ir.Ali.hollowknightme.controller.screens.MainMenuController;
import ir.Ali.hollowknightme.controller.sound.AudioManager;
import ir.Ali.hollowknightme.controller.ui.UIManager;
import ir.Ali.hollowknightme.enums.sound.MusicType;
import ir.Ali.hollowknightme.model.ui.TextBtn;
import ir.Ali.hollowknightme.view.animation.AnimatedImage;

public class MainMenuScreen extends BaseScreen {
    private Image backgroundImage;
    private Image logoImage;
    private AnimatedImage topOrnament;
    private AnimatedImage bottomOrnament;
    private TextBtn startGameBtn;
    private TextBtn optionsBtn;
    private TextBtn achievementsBtn;
    private TextBtn giudBtn;
    private TextBtn quitGameBtn;
    private MainMenuController controller;

    public MainMenuScreen() {
        super();
        AudioManager.getInstance().playMusic(MusicType.ENTER);
        TextureRegion bgRegion = UIManager.getAtlas().findRegion("main_menu_bg");
        if(bgRegion != null) {
            backgroundImage = new Image(new TextureRegionDrawable(bgRegion));
            backgroundImage.setScaling(Scaling.fill);
            backgroundImage.setFillParent(true);
            stage.addActor(backgroundImage);
        }
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.top();
        Table contentTable = new Table();
        Array<TextureRegion> topFrames = new Array<>();
        for (int i = 0; i <= 4; i++) {
            String regionName = "gg_board_UI_bottom_000" + i;
            TextureRegion region = UIManager.getAtlas().findRegion(regionName);
            if (region != null) {
                topFrames.add(region);
            }
        }
        if (topFrames.size > 0) {
            Animation<TextureRegion> topAnim = new Animation<>(0.1f, topFrames, Animation.PlayMode.NORMAL);
            topOrnament = new AnimatedImage(topAnim);
            contentTable.add(topOrnament).padBottom(5f).top().row();
        }
        TextureRegion logoRegion = UIManager.getAtlas().findRegion("logo");
        if (logoRegion != null) {
            logoImage = new Image(new TextureRegionDrawable(logoRegion));
            contentTable.add(logoImage).padBottom(-100f).top().row();
        }
        Array<TextureRegion> bottomFrames = new Array<>();
        for (int i = 0; i <= 7; i++) {
            String regionName = "GG_board_top_fleur000" + i;
            TextureRegion region = UIManager.getAtlas().findRegion(regionName);
            if (region != null) {
                bottomFrames.add(region);
            }
        }
        if (bottomFrames.size > 0) {
            Animation<TextureRegion> bottomAnim = new Animation<>(0.1f, bottomFrames, Animation.PlayMode.NORMAL);
            bottomOrnament = new AnimatedImage(bottomAnim);
            contentTable.add(bottomOrnament).padBottom(-370f).top().row();
        }
        startGameBtn = new TextBtn("START GAME", -20);
        optionsBtn = new TextBtn("OPTIONS" , -20);
        achievementsBtn = new TextBtn("ACHIEVEMENTS" , -20);
        giudBtn = new TextBtn("GUIDE" , -20);
        quitGameBtn = new TextBtn("QUIT GAME" , -20);
        Label text = new Label("Created By <Ali Esmaeili>" , UIManager.getSkin() , "default");
        contentTable.add(startGameBtn).top().padBottom(15f).row();
        contentTable.add(optionsBtn).padBottom(15f).top().row();
        contentTable.add(achievementsBtn).padBottom(15f).top().row();
        contentTable.add(giudBtn).padBottom(15f).top().row();
        contentTable.add(quitGameBtn).padBottom(30f).top().row();
        contentTable.add(text).padBottom(15f).top().row();
        rootTable.add(contentTable).top();
        stage.addActor(rootTable);

        contentTable.setColor(1, 1, 1, 0);
        contentTable.addAction(Actions.fadeIn(1.3f));

        controller = new MainMenuController(this);
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
        AudioManager.getInstance().update(Gdx.graphics.getDeltaTime());

    }
    public TextBtn getStartGameBtn() { return startGameBtn; }
    public TextBtn getOptionsBtn() { return optionsBtn; }
    public TextBtn getAchievementsBtn() { return achievementsBtn; }
    public TextBtn getGiudBtn() { return giudBtn; }
    public TextBtn getQuitGameBtn() { return quitGameBtn; }
}

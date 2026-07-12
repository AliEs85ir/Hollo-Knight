package ir.Ali.hollowknightme.model.ui;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import ir.Ali.hollowknightme.controller.ui.UIManager;

public class HollowMenuPanel extends Table {
    private final Label titleLabel;
    private final TextBtn backButton;
    private final Image animatedLine;
    private final Skin skin = UIManager.getSkin();
    private final Cell<Table> bodyCell;
    private float stateTime = 0f;
    private Animation<TextureRegion> lineAnimation;

    public HollowMenuPanel(String titleText) {
        this.setFillParent(true);
        titleLabel = new Label(titleText, skin, "Title");
        animatedLine = new Image();

        Array<TextureRegion> frames = new Array<>();
        for (int i = 2; i <= 8; i++) {
            frames.add(UIManager.getAtlas().findRegion("Warning_Fleur000" + i));
        }
        lineAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);
        backButton = new TextBtn("BACK", -200f);

        this.add(titleLabel).padBottom(-10f).row();
        this.add(animatedLine).padBottom(40f).row();
        bodyCell = this.add(new Table()).padBottom(30f);
        this.row();
        this.add(backButton).padBottom(20f);

        titleLabel.getColor().a = 0f;
        backButton.getColor().a = 0f;
        titleLabel.addAction(Actions.fadeIn(0.7f, Interpolation.sineOut));
        animatedLine.addAction(Actions.fadeIn(0.5f, Interpolation.bounce));
        backButton.addAction(Actions.fadeIn(0.7f, Interpolation.sineOut));
    }

    public void setBody(Table bodyTable) {
        bodyCell.setActor(bodyTable);
        bodyTable.getColor().a = 0f;
        bodyTable.addAction(Actions.fadeIn(0.7f, Interpolation.sineOut));
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
        if (!lineAnimation.isAnimationFinished(stateTime)) {
            animatedLine.setDrawable(new TextureRegionDrawable(lineAnimation.getKeyFrame(stateTime)));
        }
    }

    public TextBtn getBackButton() {
        return backButton;
    }
}

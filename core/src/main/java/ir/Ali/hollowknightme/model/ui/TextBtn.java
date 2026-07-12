package ir.Ali.hollowknightme.model.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import ir.Ali.hollowknightme.controller.ui.UIManager;

public class TextBtn extends Table {
    private final TextButton textButton;
    private final Image leftArrow;
    private final Image rightArrow;
    private final Image glowImage;
    private final Skin skin = UIManager.getSkin();

    public TextBtn(String text) {
        this(text, 15f);
    }

    public TextBtn(String text, float arrowPadding) {
        textButton = new TextButton(text, skin, "menu");
        leftArrow = new Image(skin, "arrow_left");
        rightArrow = new Image(skin, "arrow_right");
        glowImage = new Image(skin, "glow");

        leftArrow.setVisible(false);
        rightArrow.setVisible(false);
        glowImage.setVisible(false);

        Stack centerStack = new Stack();
        centerStack.add(glowImage);
        centerStack.add(textButton);

        this.add(leftArrow).padRight(arrowPadding);
        this.add(centerStack);
        this.add(rightArrow).padLeft(arrowPadding);

        textButton.addListener(new ClickListener() {
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
                glowImage.setVisible(false);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                glowImage.setVisible(true);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                glowImage.setVisible(false);
            }
        });
    }

    public void addClickListener(ClickListener listener) {
        textButton.addListener(listener);
    }
}

package ir.Ali.hollowknightme.model.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import ir.Ali.hollowknightme.controller.ui.UIManager;

public class CheckBoxDi extends Table {
    private final Image checkboxImage;
    private final Label mainLabel;
    private final Label notificationLabel;
    private final Skin skin = UIManager.getSkin();
    private boolean checked = false;

    public CheckBoxDi(String text, String onStatus) {
        checkboxImage = new Image(skin, "box_off");
        mainLabel = new Label(text, skin, "Text");

        Label.LabelStyle smallStyle;
        if (skin.has("default", Label.LabelStyle.class)) {
            smallStyle = new Label.LabelStyle(skin.get("default", Label.LabelStyle.class));
        } else {
            smallStyle = new Label.LabelStyle();
            smallStyle.font = skin.getFont("default");
            smallStyle.fontColor = Color.WHITE;
        }

        notificationLabel = new Label(" (" + onStatus + ") ", smallStyle);
        notificationLabel.setVisible(false);

        this.left();
        this.add(mainLabel).left();
        this.add(checkboxImage).left().padLeft(15f);
        this.add(notificationLabel).left();
        this.pack();

        this.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setChecked(!checked);
            }
        });
    }

    public void setChecked(boolean value) {
        if (this.checked == value) {
            return;
        }
        this.checked = value;
        updateVisuals();
    }

    private void updateVisuals() {
        notificationLabel.clearActions();
        if (checked) {
            checkboxImage.setDrawable(skin, "box_on");
            notificationLabel.setVisible(true);
            notificationLabel.setColor(1, 1, 1, 0);
            notificationLabel.setX(checkboxImage.getX() + checkboxImage.getWidth());
            notificationLabel.addAction(Actions.sequence(
                Actions.parallel(
                    Actions.fadeIn(0.25f),
                    Actions.moveBy(20f, 0f, 0.25f)
                ),
                Actions.delay(1.8f),
                Actions.parallel(
                    Actions.fadeOut(0.25f),
                    Actions.moveBy(-20f, 0f, 0.25f)
                ),
                Actions.run(() -> notificationLabel.setVisible(false))
            ));
        } else {
            checkboxImage.setDrawable(skin, "box_off");
            notificationLabel.setVisible(false);
        }
    }

    public boolean isChecked() {
        return checked;
    }
}

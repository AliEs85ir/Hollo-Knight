// File: .\core\src\main\java\ir\Ali\hollowknightme\view\popup\AreYouSurePanel.java
package ir.Ali.hollowknightme.view.popup;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import ir.Ali.hollowknightme.controller.ui.UIManager;
import ir.Ali.hollowknightme.model.ui.TextBtn;

public class AreYouSurePanel extends Table {
    private final TextBtn noBtn;
    private final TextBtn yesBtn;

    public AreYouSurePanel() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0f, 0f, 0f, 0.75f));
        pixmap.fill();
        Texture bgTexture = new Texture(pixmap);
        pixmap.dispose();
        this.setBackground(new TextureRegionDrawable(bgTexture));

        this.setTouchable(Touchable.enabled);
        this.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        Label titleLabel = new Label("ARE YOU SURE?", UIManager.getSkin(), "Text");
        noBtn = new TextBtn("NO", -200f);
        yesBtn = new TextBtn("YES", -200f);

        Table buttonsTable = new Table();
        buttonsTable.add(noBtn).padRight(100f);
        buttonsTable.add(yesBtn);

        this.add(titleLabel).padBottom(50f).row();
        this.add(buttonsTable);
    }

    public void show(Stage stage) {
        stage.addActor(this);
        this.setSize(stage.getWidth(), stage.getHeight());
        this.setPosition(0, 0);
        this.getColor().a = 0;
        this.addAction(Actions.fadeIn(0.25f));
    }

    public void hide(Runnable onComplete) {
        this.addAction(Actions.sequence(
            Actions.fadeOut(0.2f),
            Actions.run(() -> {
                if (onComplete != null) {
                    onComplete.run();
                }
            }),
            Actions.removeActor()
        ));
    }

    public TextBtn getNoBtn() { return noBtn; }
    public TextBtn getYesBtn() { return yesBtn; }
}

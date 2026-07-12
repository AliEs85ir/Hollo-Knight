package ir.Ali.hollowknightme.view.popup;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import ir.Ali.hollowknightme.enums.game.GameStatus;
import ir.Ali.hollowknightme.enums.charm.CharmType;
import ir.Ali.hollowknightme.controller.game.GameStateManager;
import ir.Ali.hollowknightme.controller.ui.UIManager;
import ir.Ali.hollowknightme.controller.charm.CharmManager;
import ir.Ali.hollowknightme.model.ui.TextBtn; // ایمپورت کلاس TextBtn شما

public class InventoryPopup extends Table {
    private final Label charmTitleLabel;     // لیبل عنوان چارم با استایل Text
    private final Label descriptionLabel;    // لیبل توضیحات چارم با استایل default
    private final Label notchesLabel;
    private final Table charmsGrid;
    private final Texture bgTexture;

    public InventoryPopup() {
        super(UIManager.getSkin());
        this.setFillParent(true);
        this.center();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0f, 0f, 0f, 0.75f);
        bgTexture = new Texture(pixmap);
        pixmap.dispose();
        this.setBackground(new TextureRegionDrawable(new TextureRegion(bgTexture)));

        this.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getTarget() == InventoryPopup.this) {
                    hide();
                }
            }
        });

        Label titleLabel = new Label("CHARMS INVENTORY", UIManager.getSkin(), "Text");
        titleLabel.setFontScale(1.5f);
        titleLabel.setAlignment(Align.center);
        this.add(titleLabel).padTop(20f).row();

        notchesLabel = new Label("", UIManager.getSkin(), "default");
        notchesLabel.setAlignment(Align.center);
        this.add(notchesLabel).padTop(10f).padBottom(30f).row();

        charmsGrid = new Table();
        this.add(charmsGrid).padBottom(30f).row();

        Table infoTable = new Table();

        charmTitleLabel = new Label("SELECT A CHARM", UIManager.getSkin(), "Text");
        charmTitleLabel.setAlignment(Align.center);
        charmTitleLabel.setColor(Color.WHITE);

        descriptionLabel = new Label("Hover over a charm to view its details.", UIManager.getSkin(), "default");
        descriptionLabel.setWrap(true);
        descriptionLabel.setAlignment(Align.center);

        infoTable.add(charmTitleLabel).padBottom(10f).row();
        infoTable.add(descriptionLabel).width(550f).center();

        this.add(infoTable).height(120f).padBottom(80f).row();

        TextBtn resumeBtn = new TextBtn("RETURN TO GAME", -100f);
        resumeBtn.addClickListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });
        this.add(resumeBtn).padBottom(20f).row();

        updateNotchesLabel();
        rebuildGrid();
    }

    private void updateNotchesLabel() {
        notchesLabel.setText("Notches: " + CharmManager.getInstance().getEquippedCount() + " / " + CharmManager.getInstance().getMaxNotches());
    }

    private void rebuildGrid() {
        charmsGrid.clearChildren();
        int count = 0;

        for (final CharmType charm : CharmType.values()) {
            Table charmSlot = new Table();
            charmSlot.setBackground((TextureRegionDrawable) null);

            TextureAtlas.AtlasRegion charmRegion = UIManager.getAtlas().findRegion(charm.getRegionName());
            if (charmRegion != null) {
                Image charmImage = new Image(charmRegion);
                charmSlot.add(charmImage).size(100f, 100f).pad(10f);
            } else {
                Label placeholder = new Label(charm.getRegionName(), UIManager.getSkin(), "default");
                charmSlot.add(placeholder).size(100f, 100f).pad(10f);
            }

            if (CharmManager.getInstance().isCharmEquipped(charm)) {
                charmSlot.setColor(1f, 1f, 1f, 1.0f);
            } else {
                charmSlot.setColor(1f, 1f, 1f, 0.3f);
            }

            charmSlot.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    CharmManager.getInstance().toggleCharm(charm);
                    updateNotchesLabel();
                    rebuildGrid();
                }

                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    charmTitleLabel.setText(charm.name().replace("_", " "));
                    descriptionLabel.setText(charm.getDescription());
                }
            });

            charmsGrid.add(charmSlot).pad(20f);
            count++;

            if (count % 3 == 0) {
                charmsGrid.row();
            }
        }
    }

    public void show(Stage stage) {
        GameStateManager.getInstance().setGameStatus(GameStatus.PAUSED);
        stage.addActor(this);
    }

    public void hide() {
        GameStateManager.getInstance().setGameStatus(GameStatus.PLAYING);
        if (bgTexture != null) {
            bgTexture.dispose();
        }
        this.remove();
    }
}

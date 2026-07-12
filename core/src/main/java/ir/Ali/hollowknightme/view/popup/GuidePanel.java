package ir.Ali.hollowknightme.view.popup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import ir.Ali.hollowknightme.controller.screens.GuideController;
import ir.Ali.hollowknightme.controller.ui.UIManager;
import ir.Ali.hollowknightme.model.ui.HollowMenuPanel;
import ir.Ali.hollowknightme.model.ui.TextBtn;

public class GuidePanel extends Table {
    private final HollowMenuPanel mainPanel;
    private final TextBtn controlsBtn;
    private final TextBtn statsBtn;
    private final TextBtn abilitiesBtn;
    private final TextBtn movementBtn;
    private final TextBtn cheatsBtn;
    private final Label contentLabel;
    private final ScrollPane scrollPane;
    private final GuideController controller;
    private InputProcessor previousInputProcessor;

    private final String[] guideTexts = {
        "1. Controls & Default Keys\n\nThe character \"The Knight\" is controlled via keyboard. The default keys are as follows:\n\nMovement: Arrow keys (→, ←) for horizontal movement.\nJump: Up arrow key (↑) to jump. Pressing this key again while in mid‑air performs a Double Jump.\nDash / Evade: Shift key (or the assigned key) for a quick dash.\nFocus / Heal: A key (hold down) to begin the meditation process and convert Soul into health.\nInteract: E key to start dialogue with non‑playable characters (e.g., Zote) or to open certain doors.\nPause: Esc key to open the pause menu and temporarily stop the game.\nInventory: I key to view Charms and manage equipment.",
        "2. Character Stats & Core Systems\n\nUnderstanding the health and Soul systems is key to succeeding in battles:\n\nHealth System (Masks): The character’s health is shown as a discrete set of 5 masks in the top‑left corner. Each time the Knight collides with enemies, projectiles, or environmental spikes, one mask is removed. When the mask count reaches zero, the character is returned to the last safe location – a respawn – and the health bar is fully restored. Immediately after taking damage, the character enters a 1‑second invincibility state (accompanied by a blinking visual effect) to prevent repeated hits.\n\nSoul System (Soul Vessel): Next to the masks, there is a circular vessel for storing Soul energy. Its capacity is 99 units. The player gains 11 units of Soul for each successful sword (Nail) hit on regular enemies or bosses, gradually filling the vessel. Stored Soul is persistent and does not vanish when moving between rooms.\n\nFocus / Healing Mechanic: One of the most important survival mechanics is \"Focus\". By holding the Focus key (default A), the player begins a meditation process. This process follows strict rules:\n- While focusing, the character stands completely still on the ground and cannot move, jump, dash, or attack.\n- Healing requires 1.5 seconds of continuous focus and a dedicated animation.\n- If the timer completes successfully, a set amount of Soul is consumed and one mask is added to the health bar.\n- If the player releases the key or takes damage before the 1.5 seconds are up, the process is cancelled.",
        "3. Abilities & Spells\n\nThe Knight can use two unique spells by spending a certain amount of Soul (equal to 33 units, i.e. one‑third of the total vessel). While casting these spells, player movement is briefly locked:\n\nVengeful Spirit: Fires a magical projectile horizontally in the direction the Knight is facing. The projectile moves at a constant speed, is unaffected by gravity, and disappears upon hitting environmental obstacles (e.g., walls). This spirit can pass through enemies and deals significant damage to all enemies along its path. It is very effective for crowd control at range.\n\nHowling Wraiths: Unlike the previous spell, this creates a magical explosion upwards, directly above the player’s head. The effect does not move; it stays in place for a short time. During this period, it deals three rapid, successive damage ticks to any enemies within its hitbox. This spell is specifically designed to counter flying enemies or those positioned above the Knight.",
        "4. Advanced Movement & Platforming Techniques\n\nBeyond basic moves, the Knight has two vital techniques for overcoming environmental challenges, which test the player’s platforming skill:\n\nMantis Claw / Wall Slide: If the character is in the air and the player holds the movement key towards a vertical wall, upon touching the wall, the Knight will slide down the wall at a very slow speed. In this state, a wall‑grip animation plays; releasing the key or moving in the opposite direction cancels the slide. This technique is useful for hanging on and timing subsequent jumps.\n\nPogo Jumping: A key skill that allows the Knight to land on spikes and enemies. While in mid‑air, if the player holds the down arrow (↓) and presses the attack key (X), the Knight strikes downward. If this strike hits spikes or enemies, not only does the Knight take no damage, but the character is launched upward again (a mid‑air re‑jump). Successfully performing a pogo also resets the dash and double‑jump limits for that jump.",
        "5. Cheat Codes\n\nTo facilitate testing, debugging, and assist players when needed, a set of cheat codes is built into the game. These codes are activated by pressing Ctrl + one of the main control keys:\n\nBoss Arena Teleport: Using Ctrl + S, the character is instantly moved to the start of the False Knight boss arena, allowing quick testing of the boss phases.\n\nNoclip / Spectator Mode: With Ctrl + F, the character’s movement speed increases, movement animations are disabled, and gravity no longer affects the Knight; the player can freely roam the entire map without collision.\n\nEmergency Heal: Using Ctrl + E in critical situations, immediately adds one health mask to the Knight.\n\nRefill Soul Vessel: Using Ctrl + D fully replenishes the character’s Soul vessel.\n\nGod Mode: With Ctrl + G, the Knight becomes immune to all damage from spikes, enemies, and boss attacks.\n\nPower Up: Using Ctrl + T increases the Knight’s speed and attack power."
    };

    public GuidePanel() {
        this.setFillParent(true);
        this.setTouchable(Touchable.enabled);

        TextureRegion bgRegion = UIManager.getAtlas().findRegion("main_menu_bg");
        if (bgRegion != null) {
            this.setBackground(new com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable(bgRegion));
        }

        this.setFillParent(true);
        this.setTouchable(Touchable.enabled);
        this.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        mainPanel = new HollowMenuPanel("GUIDE");

        Table bodyTable = new Table();
        Table leftMenuTable = new Table();

        controlsBtn = new TextBtn("CONTROLS", -50f);
        statsBtn = new TextBtn("STATS", -50f);
        abilitiesBtn = new TextBtn("ABILITIES", -50f);
        movementBtn = new TextBtn("MOVEMENT", -50f);
        cheatsBtn = new TextBtn("CHEATS", -50f);

        leftMenuTable.add(controlsBtn).width(250f).padBottom(15f).row();
        leftMenuTable.add(statsBtn).width(250f).padBottom(15f).row();
        leftMenuTable.add(abilitiesBtn).width(250f).padBottom(15f).row();
        leftMenuTable.add(movementBtn).width(250f).padBottom(15f).row();
        leftMenuTable.add(cheatsBtn).width(250f);

        contentLabel = new Label(guideTexts[0], UIManager.getSkin(), "Text");
        contentLabel.setWrap(true);

        scrollPane = new ScrollPane(contentLabel, UIManager.getSkin(), "scroll");
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        bodyTable.add(leftMenuTable).top().left().padRight(40f);
        bodyTable.add(scrollPane).expand().fill().top().left().width(650f).height(450f);

        mainPanel.setBody(bodyTable);
        this.add(mainPanel).expand().fill();

        setupListeners();
        controller = new GuideController(this);
    }

    private void setupListeners() {
        controlsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                switchText(0);
            }
        });

        statsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                switchText(1);
            }
        });

        abilitiesBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                switchText(2);
            }
        });

        movementBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                switchText(3);
            }
        });

        cheatsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                switchText(4);
            }
        });
    }

    private void switchText(int index) {
        if (index >= 0 && index < guideTexts.length) {
            contentLabel.setText(guideTexts[index]);
            scrollPane.setScrollY(0f);
        }
    }

    public void show(Stage stage) {
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
    public TextBtn getControlsBtn() { return controlsBtn; }
    public TextBtn getStatsBtn() { return statsBtn; }
    public TextBtn getAbilitiesBtn() { return abilitiesBtn; }
    public TextBtn getMovementBtn() { return movementBtn; }
    public TextBtn getCheatsBtn() { return cheatsBtn; }
    public Label getContentLabel() { return contentLabel; }
    public ScrollPane getScrollPane() { return scrollPane; }
}

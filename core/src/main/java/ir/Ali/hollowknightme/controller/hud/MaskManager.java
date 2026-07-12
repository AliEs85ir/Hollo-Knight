package ir.Ali.hollowknightme.controller.hud;

import com.badlogic.gdx.utils.Array;
import ir.Ali.hollowknightme.enums.hud.MaskState;
import ir.Ali.hollowknightme.model.hud.Mask;

public class MaskManager {
    private final Array<Mask> masks;
    private int currentHealth;
    private int maxHealth;
    private final float startX;
    private final float startY;
    private final float padding;

    public MaskManager(int maxHealth, float startX, float startY, float padding) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.startX = startX;
        this.startY = startY;
        this.padding = padding;
        this.masks = new Array<>(maxHealth);

        for (int i = 0; i < maxHealth; i++) {
            masks.add(new Mask(startX + (i * padding), startY));
        }
    }

    public void update(float dt, int knightHealth, int knightMaxHealth) {
        if (this.maxHealth != knightMaxHealth) {
            syncMaxHealth(knightMaxHealth);
        }

        if (this.currentHealth > knightHealth) {
            for (int i = 0; i < (this.currentHealth - knightHealth); i++) {
                damageMask();
            }
            this.currentHealth = knightHealth;
        } else if (this.currentHealth < knightHealth) {
            for (int i = 0; i < (knightHealth - this.currentHealth); i++) {
                healMask();
            }
            this.currentHealth = knightHealth;
        }

        for (Mask mask : masks) {
            mask.update(dt);
        }
    }

    private void syncMaxHealth(int newMax) {
        if (newMax > maxHealth) {
            for (int i = maxHealth; i < newMax; i++) {
                masks.add(new Mask(startX + (i * padding), startY));
            }
        } else if (newMax < maxHealth) {
            masks.removeRange(newMax, maxHealth - 1);
        }
        this.maxHealth = newMax;
    }

    private void damageMask() {
        for (int i = masks.size - 1; i >= 0; i--) {
            Mask mask = masks.get(i);
            if (mask.getCurrentState() == MaskState.IDLE_FULL ||
                mask.getCurrentState() == MaskState.CREATING ||
                mask.getCurrentState() == MaskState.FILLED_EFFECT) {
                mask.setState(MaskState.DELETING);
                return;
            }
        }
    }

    private void healMask() {
        for (int i = 0; i < masks.size; i++) {
            Mask mask = masks.get(i);
            if (mask.getCurrentState() == MaskState.IDLE_EMPTY ||
                mask.getCurrentState() == MaskState.DELETING) {
                mask.setState(MaskState.CREATING);
                return;
            }
        }
    }

    public Array<Mask> getMasks() {
        return masks;
    }
}

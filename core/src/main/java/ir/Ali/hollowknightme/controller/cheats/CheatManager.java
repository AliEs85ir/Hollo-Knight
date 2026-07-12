package ir.Ali.hollowknightme.controller.cheats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import ir.Ali.hollowknightme.enums.knight.MovementStatus;
import ir.Ali.hollowknightme.controller.game.ControlManager;
import ir.Ali.hollowknightme.controller.charm.CharmManager;
import ir.Ali.hollowknightme.model.knight.Knight;

public class CheatManager {
    private static volatile CheatManager instance;
    private boolean godMode = false;
    private boolean noclip = false;

    private CheatManager() {}

    public static CheatManager getInstance() {
        if (instance == null) {
            synchronized (CheatManager.class) {
                if (instance == null) {
                    instance = new CheatManager();
                }
            }
        }
        return instance;
    }

    public boolean isGodMode() {
        return godMode;
    }

    public boolean isNoclip() {
        return noclip;
    }

    public void update(Knight knight, float deltaTime) {
        if (knight == null || knight.getBody() == null) return;

        boolean ctrlPressed = Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT);

        // تشخیص زدن کلیدهای تقلب (فقط زمان نگه‌داشتن کنترل)
        if (ctrlPressed) {
            if (Gdx.input.isKeyJustPressed(Keys.F)) {
                noclip = !noclip;
                if (!noclip) {
                    knight.getBody().setGravityScale(1f);
                } else {
                    knight.setLocked(false);
                }
            }

            if (Gdx.input.isKeyJustPressed(Keys.E)) {
                if (knight.getCurrentHealth() < knight.getMaxHealth()) {
                    knight.setCurrentHealth(knight.getCurrentHealth() + 1);
                }
            }

            if (Gdx.input.isKeyJustPressed(Keys.D)) {
                knight.setSoul(99);
            }

            if (Gdx.input.isKeyJustPressed(Keys.G)) {
                godMode = !godMode;
            }

            if (Gdx.input.isKeyJustPressed(Keys.T)) {
                CharmManager.getInstance().equipAllCharmsCheat();
            }
        }

        // اعمال پایداری مودها (جدا از فشرده بودن کنترل)
        if (godMode) {
            knight.setCurrentHealth(knight.getMaxHealth());
            knight.setLocked(false);
        }

        if (noclip) {
            handleNoclipMovement(knight, deltaTime);
        }
    }

    private void handleNoclipMovement(Knight knight, float deltaTime) {
        float speed = 12f;
        Vector2 pos = knight.getBody().getPosition().cpy();
        ControlManager cm = ControlManager.getInstance();

        // بررسی کلیدهای کاستوم بازی + کلیدهای فیزیکی استاندارد به صورت همزمان
        if (cm.isActionPressed(MovementStatus.MOVE_LEFT) || Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {
            pos.x -= speed * deltaTime;
        }
        if (cm.isActionPressed(MovementStatus.MOVE_RIGHT) || Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)) {
            pos.x += speed * deltaTime;
        }
        if (cm.isActionPressed(MovementStatus.LOOK_UP) || Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W)) {
            pos.y += speed * deltaTime;
        }
        if (cm.isActionPressed(MovementStatus.LOOK_DOWN) || Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.S)) {
            pos.y -= speed * deltaTime;
        }

        knight.getBody().setGravityScale(0);
        knight.getBody().setLinearVelocity(0, 0);
        knight.getBody().setTransform(pos, 0);
    }
}

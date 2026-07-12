package ir.Ali.hollowknightme.view.enemy;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ir.Ali.hollowknightme.enums.enemy.EnemyState;
import ir.Ali.hollowknightme.model.enemy.*;

public class EnemyRenderer {

    private final EnemyAnimator animator;
    private final LaserAnimator laserAnimator;

    public EnemyRenderer(EnemyAnimator animator) {
        this.animator = animator;
        this.laserAnimator = new LaserAnimator();
    }

    public void render(SpriteBatch batch, Enemy enemy, float deltaTime) {
        TextureRegion frame = animator.getFrame(enemy.getCurrentState(), deltaTime, enemy.isFacingRight());

        if (frame != null) {
            float scale = (enemy instanceof WingedSentry) ? 0.5f : 1.0f;
            float width = (frame.getRegionWidth() / 100f) * scale;
            float height = (frame.getRegionHeight() / 100f) * scale;

            float x = enemy.getX() - (width / 2f);
            float y = enemy.getY() - (height / 2f);

            if (enemy.isFacingRight()) {
                batch.draw(frame, x + width, y, -width, height);
            } else {
                batch.draw(frame, x, y, width, height);
            }

            if (enemy instanceof FalseKnight)
            {
                FalseKnight falseKnight = (FalseKnight) enemy;
                if (falseKnight.isShowHitFlash()) {
                    float alpha = 0.7f;
                    batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
                    batch.setColor(1f, 1f, 1f, alpha);
                    if (falseKnight.isFacingRight()) {
                        batch.draw(frame, x + width, y, -width, height);
                    } else {
                        batch.draw(frame, x, y, width, height);
                    }
                    batch.setColor(1f, 1f, 1f, 1f);
                    batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                }
            }

        }

        if (enemy instanceof CrystalGuardian crystalGuardian && enemy.getCurrentState() == EnemyState.SHOOT) {
            float offsetX = crystalGuardian.getLaser().getOffsetX();
            float offsetY = crystalGuardian.getLaser().getOffsetY();
            float laserLength = crystalGuardian.getLaser().getWidth();

            float startX = enemy.isFacingRight() ? enemy.getX() + offsetX : enemy.getX() - offsetX;
            float startY = enemy.getY() + offsetY;

            laserAnimator.draw(batch, deltaTime, true, enemy.isFacingRight(), startX, startY, laserLength);
        } else {
            laserAnimator.draw(batch, deltaTime, false, enemy.isFacingRight(), 0, 0, 0);
        }
    }
}

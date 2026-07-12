package ir.Ali.hollowknightme.view.enemy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ir.Ali.hollowknightme.model.enemy.CrystalGuardian;
import ir.Ali.hollowknightme.model.enemy.EnemyLaser;
import ir.Ali.hollowknightme.enums.enemy.EnemyState;

public class LaserRenderer {
    private float stateTime = 0f;

    public void update(float dt) {
        stateTime += dt;
    }

    public void draw(SpriteBatch batch, CrystalGuardian guardian, TextureRegion frame3, TextureRegion frame4, float headWidthRatio, float ppm) {
        if (guardian.getCurrentState() != EnemyState.SHOOT) return;

        EnemyLaser laser = guardian.getLaser();
        if (!laser.isActive()) return;

        TextureRegion activeFrame = (stateTime % 0.1f < 0.05f) ? frame3 : frame4;
        Texture texture = activeFrame.getTexture();

        int srcX = activeFrame.getRegionX();
        int srcY = activeFrame.getRegionY();
        int totalWidth = activeFrame.getRegionWidth();
        int srcHeight = activeFrame.getRegionHeight();

        int headSrcWidth = (int) (totalWidth * headWidthRatio);
        int bodySrcWidth = totalWidth - headSrcWidth;

        float x = guardian.getX() * ppm;
        float y = guardian.getY() * ppm;
        boolean facingRight = guardian.isFacingRight();

        float currentX = facingRight ? x + (laser.getOffsetX() * ppm) : x - (laser.getOffsetX() * ppm);
        float drawY = y + (laser.getOffsetY() * ppm) - (srcHeight / 2f);

        float headDrawWidth = headSrcWidth;
        float headDrawX = facingRight ? currentX : currentX - headDrawWidth;

        batch.draw(texture, headDrawX, drawY, 0, 0, headDrawWidth, srcHeight, 1, 1, 0, srcX, srcY, headSrcWidth, srcHeight, !facingRight, false);

        currentX = facingRight ? currentX + headDrawWidth : currentX - headDrawWidth;
        float remainingLength = laser.getWidth() * ppm - headDrawWidth;
        int step = 0;

        int bodySrcX = srcX + headSrcWidth;

        while (remainingLength > 0) {
            float drawWidth = Math.min(bodySrcWidth, remainingLength);
            boolean flipPattern = (step % 2 != 0);
            boolean finalFlipX = facingRight ? flipPattern : !flipPattern;

            float bodyDrawX = facingRight ? currentX : currentX - drawWidth;

            batch.draw(texture, bodyDrawX, drawY, 0, 0, drawWidth, srcHeight, 1, 1, 0, bodySrcX, srcY, (int) drawWidth, srcHeight, finalFlipX, false);

            currentX = facingRight ? currentX + drawWidth : currentX - drawWidth;
            remainingLength -= drawWidth;
            step++;
        }
    }
}

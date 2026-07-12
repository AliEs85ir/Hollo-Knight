package ir.Ali.hollowknightme.view.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class LaserAnimator {
    private final TextureAtlas atlas;
    private final Animation<TextureRegion> laserAnimation;
    private float stateTime = 0f;

    public LaserAnimator() {
        this.atlas = new TextureAtlas(Gdx.files.internal("animations/animation/Laser.atlas"));
        this.laserAnimation = new Animation<>(0.05f, atlas.findRegions("Laser"), Animation.PlayMode.NORMAL);
    }

    public void draw(SpriteBatch batch, float deltaTime, boolean isAttacking, boolean facingRight, float startX, float startY, float totalLength) {
        if (!isAttacking) {
            stateTime = 0f;
            return;
        }

        stateTime += deltaTime;
        TextureRegion frame = laserAnimation.getKeyFrame(stateTime);
        int frameIndex = laserAnimation.getKeyFrameIndex(stateTime);

        float width = frame.getRegionWidth() / 100f;
        float height = frame.getRegionHeight() / 100f;

        if (frameIndex < 3) {
            float drawX = facingRight ? startX : startX - width;
            float originX = width / 2f;
            float originY = height / 2f;
            batch.draw(frame.getTexture(), drawX, startY - height / 2f, originX, originY, width, height, 1f, 1f, 0f,
                frame.getRegionX(), frame.getRegionY(), frame.getRegionWidth(), frame.getRegionHeight(), !facingRight, false);
        } else {
            float remainingLength = totalLength;
            float currentX = startX;
            boolean mirror = false;

            while (remainingLength > 0) {
                float drawWidth = Math.min(width, remainingLength);
                float drawX = facingRight ? currentX : currentX - drawWidth;

                boolean finalFlipX = facingRight ? mirror : !mirror;

                batch.draw(frame.getTexture(), drawX, startY - height / 2f, drawWidth / 2f, height / 2f, drawWidth, height, 1f, 1f, 0f,
                    frame.getRegionX(), frame.getRegionY(), (int)(frame.getRegionWidth() * (drawWidth / width)), frame.getRegionHeight(), finalFlipX, false);

                if (facingRight) {
                    currentX += drawWidth;
                } else {
                    currentX -= drawWidth;
                }
                remainingLength -= drawWidth;
                mirror = !mirror;
            }
        }
    }

    public void dispose() {
        if (atlas != null) atlas.dispose();
    }
}

package ir.Ali.hollowknightme.view.knight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class NailAnimator {
    private final TextureAtlas atlas;
    private final Animation<TextureRegion> slashAnimation;
    private float stateTime = 0f;

    public NailAnimator() {
        this.atlas = new TextureAtlas(Gdx.files.internal("nail.atlas"));
        this.slashAnimation = new Animation<>(0.03f, atlas.findRegions("Nail"),
            Animation.PlayMode.LOOP_REVERSED);
    }

    public TextureRegion getFrame(float deltaTime, boolean isAttacking, boolean facingRight) {
        if (!isAttacking) {
            stateTime = 0f;
            return null;
        }

        stateTime += deltaTime;
        TextureRegion frame = slashAnimation.getKeyFrame(stateTime);

        if (frame != null) {
            boolean shouldFlip = !facingRight;
            if (frame.isFlipX() != shouldFlip) {
                frame.flip(true, false);
            }
        }

        return frame;
    }

    public void dispose() {
        if (atlas != null) {
            atlas.dispose();
        }
    }
}

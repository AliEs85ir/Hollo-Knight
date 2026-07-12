package ir.Ali.hollowknightme.view.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ir.Ali.hollowknightme.enums.enemy.EnemyState;
import ir.Ali.hollowknightme.enums.enemy.EnemyType;

public class EnemyAnimator {

    private final EnemyType type;
    private final EnemyAnimationLibrary library;
    private float stateTime;
    private EnemyState previousState;

    public EnemyAnimator(EnemyType type, EnemyAnimationLibrary library) {
        this.type = type;
        this.library = library;
        this.stateTime = 0f;
        this.previousState = EnemyState.IDLE;
    }

    public TextureRegion getFrame(EnemyState currentState, float deltaTime, boolean isFacingRight) {
        updateStateTime(currentState, deltaTime);

        Animation<TextureRegion> currentAnimation = library.getAnimation(type, currentState);
        if (currentAnimation == null) return null;

        if (currentAnimation.getPlayMode() == Animation.PlayMode.NORMAL) {
            return currentAnimation.getKeyFrame(Math.min(stateTime, currentAnimation.getAnimationDuration()));
        }

        return currentAnimation.getKeyFrame(stateTime);
    }

    public float getAnimationDuration(EnemyState state) {
        Animation<TextureRegion> animation = library.getAnimation(type, state);
        return animation != null ? animation.getAnimationDuration() : 0f;
    }

    private void updateStateTime(EnemyState currentState, float deltaTime) {
        if (currentState != previousState) {
            stateTime = 0f;
            previousState = currentState;
        } else {
            stateTime += deltaTime;
        }
    }
}

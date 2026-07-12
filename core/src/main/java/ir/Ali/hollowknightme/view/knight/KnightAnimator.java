package ir.Ali.hollowknightme.view.knight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import ir.Ali.hollowknightme.enums.knight.KnightState;
import ir.Ali.hollowknightme.model.knight.Knight;
import java.util.EnumMap;

public class KnightAnimator {
    private final Knight knight;
    private final EnumMap<KnightState, Animation<TextureRegion>> animations = new EnumMap<>(KnightState.class);
    private final TextureAtlas atlas;
    private float stateTime = 0f;
    private KnightState previousState = null;

    public KnightAnimator(Knight knight) {
        this.knight = knight;
        this.atlas = new TextureAtlas(Gdx.files.internal("assets/knight.atlas"));
        initializeAnimations();
    }

    private void initializeAnimations() {
        for (KnightState state : KnightState.values()) {
            Array<TextureAtlas.AtlasRegion> regions = atlas.findRegions(getAtlasRegionName(state));
            if (regions.isEmpty()) continue;

            animations.put(state, new Animation<>(getFrameDuration(state), regions, getPlayMode(state)));
        }
    }

    public TextureRegion getFrame(float deltaTime, KnightState currentState) {
        if (currentState != previousState) {
            stateTime = 0f;
            previousState = currentState;
        }
        stateTime += deltaTime;

        Animation<TextureRegion> animation = animations.get(currentState);
        if (animation == null) return null;

        TextureRegion frame = animation.getKeyFrame(stateTime);
        boolean shouldFlip = knight.isFacingRight();

        if (frame.isFlipX() != shouldFlip) {
            frame.flip(true, false);
        }
        return frame;
    }

    public void dispose() {
        if (atlas != null) {
            atlas.dispose();
        }
    }

    public float getAnimationDuration(KnightState state) {
        Animation<TextureRegion> anim = animations.get(state);
        return anim != null ? anim.getAnimationDuration() : 0.3f;
    }

    private String getAtlasRegionName(KnightState state) {
        switch (state) {
            case DOUBLE_JUMP: return "Double Jump";
            case DOWN_SLASH: return "DownSlash";
            case FIREBALL_CAST: return "Fireball Cast";
            case FOCUS_GET: return "Focus Get";
            case FOCUS_START: return "Focus Start";
            case FOCUS_END: return "Focus End";
            case IDLE_HURT: return "Idle Hurt";
            case LOOK_DOWN: return "LookDown";
            case LOOK_UP: return "LookUp";
            case SLASH_ALT: return "SlashAlt";
            case UP_SLASH: return "UpSlash";
            case WALL_SLIDE: return "Wall Slide";
            case WALL_JUMP: return "Walljump";
            case TRANSITION: return "Run";
            default: return state.name().substring(0, 1).toUpperCase() + state.name().substring(1).toLowerCase();
        }
    }

    private float getFrameDuration(KnightState state) {
        switch (state) {
            case DASH: return 0.03f;
            case RUN: return 0.08f;
            case SLASH: return 0.05f;
            case SLASH_ALT: return 0.05f;
            case WALL_SLIDE: return 0.1f;
            case AIRBORNE: return 0.1f;
            case DEATH: return 0.15f;
            case DOUBLE_JUMP: return 0.05f;
            default: return 0.12f;
        }
    }

    private Animation.PlayMode getPlayMode(KnightState state) {
        switch (state) {
            case IDLE:
            case RUN:
            case AIRBORNE:
            case WALL_SLIDE:
            case FOCUS:
                return Animation.PlayMode.LOOP;
            default:
                return Animation.PlayMode.NORMAL;
        }
    }
}

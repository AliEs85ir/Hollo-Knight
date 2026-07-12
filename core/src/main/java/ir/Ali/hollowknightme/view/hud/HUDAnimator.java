package ir.Ali.hollowknightme.view.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import ir.Ali.hollowknightme.enums.hud.MaskState;
import ir.Ali.hollowknightme.enums.hud.SoulState;
import java.util.EnumMap;

public class HUDAnimator {
    private final TextureAtlas atlas;
    private final EnumMap<MaskState, Animation<TextureRegion>> maskAnimations;
    private final EnumMap<SoulState, Animation<TextureRegion>> soulAnimations;
    private Animation<TextureRegion> healthBarCreateAnimation;
    private TextureRegion healthBarIdle;

    public HUDAnimator() {
        this.atlas = new TextureAtlas(Gdx.files.internal("animations/animation/HUD/HUD.atlas"));
        this.maskAnimations = new EnumMap<>(MaskState.class);
        this.soulAnimations = new EnumMap<>(SoulState.class);

        initializeHealthBar();
        initializeMaskAnimations();
        initializeSoulAnimations();
    }

    private void initializeHealthBar() {
        Array<TextureAtlas.AtlasRegion> createRegions = atlas.findRegions("HealthBar");
        if (!createRegions.isEmpty()) {
            healthBarCreateAnimation = new Animation<>(0.05f, createRegions, Animation.PlayMode.NORMAL);
            healthBarIdle = createRegions.get(createRegions.size - 1);
        }
    }

    private void initializeMaskAnimations() {
        Array<TextureAtlas.AtlasRegion> createRegions = atlas.findRegions("CreateMask");
        Array<TextureAtlas.AtlasRegion> deleteRegions = atlas.findRegions("BreakHealth");
        Array<TextureAtlas.AtlasRegion> filledRegions = atlas.findRegions("FilledHealthShine");

        if (!createRegions.isEmpty()) {
            maskAnimations.put(MaskState.CREATING, new Animation<>(0.07f, createRegions, Animation.PlayMode.NORMAL));

            TextureRegion fullFrame = createRegions.get(createRegions.size - 1);

            maskAnimations.put(MaskState.IDLE_FULL, new Animation<>(1f, new TextureRegion[]{fullFrame}));
        }

        if (!deleteRegions.isEmpty()) {
            maskAnimations.put(MaskState.DELETING, new Animation<>(0.1f, deleteRegions, Animation.PlayMode.NORMAL));

            TextureRegion emptyFrame = deleteRegions.get(deleteRegions.size-1);
            maskAnimations.put(MaskState.IDLE_EMPTY, new Animation<>(1f, new TextureRegion[]{emptyFrame}));
        }

        if (!filledRegions.isEmpty()) {
            maskAnimations.put(MaskState.FILLED_EFFECT, new Animation<>(0.5f, filledRegions, Animation.PlayMode.NORMAL));
        }
    }

    private void initializeSoulAnimations() {
        soulAnimations.put(SoulState.ZERO, createAnimation("HUDZero", Animation.PlayMode.LOOP, false));
        soulAnimations.put(SoulState.ZERO_TO_ONE, createAnimation("HUDZeroOne", Animation.PlayMode.NORMAL, false));
        soulAnimations.put(SoulState.ONE_TO_ZERO, createAnimation("HUDZeroOne", Animation.PlayMode.NORMAL, true));

        soulAnimations.put(SoulState.ONE, createAnimation("HUDOne", Animation.PlayMode.LOOP, false));
        soulAnimations.put(SoulState.ONE_TO_TWO, createAnimation("HUDOneTwo", Animation.PlayMode.NORMAL, false));
        soulAnimations.put(SoulState.TWO_TO_ONE, createAnimation("HUDOneTwo", Animation.PlayMode.NORMAL, true));

        soulAnimations.put(SoulState.TWO, createAnimation("HUDTwo", Animation.PlayMode.LOOP, false));
        soulAnimations.put(SoulState.TWO_TO_THREE, createAnimation("HUDTwoThree", Animation.PlayMode.NORMAL, false));
        soulAnimations.put(SoulState.THREE_TO_TWO, createAnimation("HUDTwoThree", Animation.PlayMode.NORMAL, true));

        soulAnimations.put(SoulState.THREE, createAnimation("HUDThree", Animation.PlayMode.LOOP, false));
    }

    private Animation<TextureRegion> createAnimation(String regionName, Animation.PlayMode mode, boolean reversed) {
        Array<TextureAtlas.AtlasRegion> regions = atlas.findRegions(regionName);
        if (regions.isEmpty()) {
            return null;
        }
        if (reversed) {
            regions.reverse();
        }
        return new Animation<>(0.12f, regions, mode);
    }

    public TextureRegion getHealthBarFrame(float stateTime, boolean isStarting) {
        if (isStarting && healthBarCreateAnimation != null) {
            return healthBarCreateAnimation.getKeyFrame(stateTime);
        }
        return healthBarIdle;
    }

    public boolean isHealthBarCreateFinished(float stateTime) {
        if (healthBarCreateAnimation == null) return true;
        return healthBarCreateAnimation.isAnimationFinished(stateTime);
    }

    public TextureRegion getMaskFrame(MaskState state, float stateTime) {
        Animation<TextureRegion> anim = maskAnimations.get(state);
        if (anim == null) return null;
        return anim.getKeyFrame(stateTime);
    }

    public boolean isMaskAnimationFinished(MaskState state, float stateTime) {
        Animation<TextureRegion> anim = maskAnimations.get(state);
        if (anim == null) return true;
        return anim.isAnimationFinished(stateTime);
    }

    public TextureRegion getSoulFrame(SoulState state, float stateTime) {
        Animation<TextureRegion> anim = soulAnimations.get(state);
        if (anim == null) return null;
        return anim.getKeyFrame(stateTime);
    }

    public boolean isSoulTransitionFinished(SoulState state, float stateTime) {
        Animation<TextureRegion> anim = soulAnimations.get(state);
        if (anim == null) return true;
        return anim.isAnimationFinished(stateTime);
    }

    public void dispose() {
        if (atlas != null) {
            atlas.dispose();
        }
    }
}

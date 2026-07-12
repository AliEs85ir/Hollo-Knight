package ir.Ali.hollowknightme.view.hud;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ir.Ali.hollowknightme.enums.hud.MaskState;
import ir.Ali.hollowknightme.enums.hud.SoulState;
import ir.Ali.hollowknightme.controller.hud.MaskManager;
import ir.Ali.hollowknightme.controller.hud.SoulManager;
import ir.Ali.hollowknightme.model.knight.Knight;
import ir.Ali.hollowknightme.model.hud.Mask;

public class HUDRenderer {
    private final Knight knight;
    private final HUDAnimator animator;
    private final MaskManager maskManager;
    private final SoulManager soulManager;
    private final OrthographicCamera hudCamera;
    private final Viewport hudViewport;

    private float globalStateTime;
    private float soulStateTime;
    private boolean isStarting;

    private final float HEALTH_BAR_X = 50f;
    private final float HEALTH_BAR_Y = 930f;
    private final float SOUL_X_OFFSET = -35f;
    private final float SOUL_Y_OFFSET = -42f;
    private final float MASK_START_X_OFFSET = 140f;
    private final float MASK_START_Y_OFFSET = 0f;
    private final float MASK_PADDING = 75f;

    public HUDRenderer(Knight knight) {
        this.knight = knight;
        this.animator = new HUDAnimator();

        this.maskManager = new MaskManager(
            knight.getMaxHealth(),
            HEALTH_BAR_X + MASK_START_X_OFFSET,
            HEALTH_BAR_Y + MASK_START_Y_OFFSET,
            MASK_PADDING
        );

        this.soulManager = new SoulManager(knight.getSoul());

        this.hudCamera = new OrthographicCamera();
        this.hudViewport = new FitViewport(1920, 1080, hudCamera);
        this.hudCamera.position.set(1920f / 2f, 1080f / 2f, 0);
        this.hudCamera.update();

        this.globalStateTime = 0f;
        this.soulStateTime = 0f;
        this.isStarting = true;
    }

    public void update(float dt) {
        globalStateTime += dt;
        soulStateTime += dt;

        if (isStarting && animator.isHealthBarCreateFinished(globalStateTime)) {
            isStarting = false;
        }

        maskManager.update(dt, knight.getCurrentHealth(), knight.getMaxHealth());

        Array<Mask> masks = maskManager.getMasks();
        for (int i = 0; i < masks.size; i++) {
            Mask mask = masks.get(i);
            MaskState state = mask.getCurrentState();

            if (state == MaskState.CREATING && animator.isMaskAnimationFinished(state, mask.getStateTime())) {
                mask.setState(MaskState.IDLE_FULL);
            } else if (state == MaskState.DELETING && animator.isMaskAnimationFinished(state, mask.getStateTime())) {
                mask.setState(MaskState.IDLE_EMPTY);
            } else if (state == MaskState.FILLED_EFFECT && animator.isMaskAnimationFinished(state, mask.getStateTime())) {
                mask.setState(MaskState.IDLE_FULL);
            }
        }

        soulManager.update(knight.getSoul());
        if (soulManager.isTransitioning()) {
            SoulState currentRenderState = soulManager.getCurrentRenderState();
            if (animator.isSoulTransitionFinished(currentRenderState, soulStateTime)) {
                soulManager.completeCurrentTransition();
                soulStateTime = 0f;
            }
        }
    }

    public void render(SpriteBatch batch) {
        hudCamera.update();
        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();

        TextureRegion healthBarFrame = animator.getHealthBarFrame(globalStateTime, isStarting);
        if (healthBarFrame != null) {
            batch.draw(healthBarFrame, HEALTH_BAR_X, HEALTH_BAR_Y);
        }

        TextureRegion soulFrame = animator.getSoulFrame(soulManager.getCurrentRenderState(), soulStateTime);
        if (soulFrame != null) {
            batch.draw(soulFrame, HEALTH_BAR_X + SOUL_X_OFFSET, HEALTH_BAR_Y + SOUL_Y_OFFSET ,
                218 , 218);
        }

        Array<Mask> masks = maskManager.getMasks();
        for (int i = 0; i < masks.size; i++) {
            Mask mask = masks.get(i);
            TextureRegion maskFrame = animator.getMaskFrame(mask.getCurrentState(), mask.getStateTime());
            if (maskFrame != null) {
                batch.draw(maskFrame, mask.getX(), mask.getY());
            }
        }

        batch.end();
    }

    public void resize(int width, int height) {
        hudViewport.update(width, height, true);
    }

    public void dispose() {
        animator.dispose();
    }
}

package ir.Ali.hollowknightme.controller.knight;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import ir.Ali.hollowknightme.enums.knight.KnightState;
import ir.Ali.hollowknightme.enums.knight.MovementStatus;
import ir.Ali.hollowknightme.enums.charm.CharmType;
import ir.Ali.hollowknightme.controller.game.ControlManager;
import ir.Ali.hollowknightme.controller.game.GameRunManager;
import ir.Ali.hollowknightme.controller.cheats.CheatManager;
import ir.Ali.hollowknightme.model.knight.Knight;
import ir.Ali.hollowknightme.view.knight.KnightAnimator;
import ir.Ali.hollowknightme.controller.charm.CharmManager;
import ir.Ali.hollowknightme.controller.sound.AudioManager;
import ir.Ali.hollowknightme.enums.sound.SfxType;

public class KnightController {
    private final Knight knight;

    private static final float MAX_VELOCITY = 2f;
    private static final float DASH_VELOCITY = 8f;
    private static final float JUMP_VELOCITY = 10f;
    private static final float DOUBLE_JUMP_VELOCITY = 8.0f;
    private static final float WALL_JUMP_VELOCITY_Y = 9.2f;
    private static final float WALL_JUMP_VELOCITY_X = 6.5f;
    private static final float JUMP_CUTOFF_FACTOR = 0.5f;
    private static final float GRAVITY_SCALE = 1f;
    private static final float WALL_SLIDE_SPEED = -1.0f;
    private static final int FOCUS_SOUL_COST = 30;
    private static final int FOCUS_HEAL = 1;

    private float dashTimer = 0f;
    private float attackTimer = 0f;
    private float landingTimer = 0f;
    private float wallJumpTimer = 0f;
    private float castTimer = 0f;
    private float pogoImmunityTimer = 0f;
    private float focusTimer = 0f;
    private float dashCooldownTimer = 0f;
    private boolean focusKeyConsumed = false;

    private boolean isJumping = false;
    private boolean previousGrounded = false;
    private int comboCount = 0;
    private KnightState currentAttackDirection = KnightState.SLASH;

    private final KnightAnimator knightAnimator;

    private final World world;
    private final KnightContactListener knightContactListener;

    private GameRunManager gameRunManager;
    private boolean deathProcessed = false;

    public KnightController(Knight knight, World world, KnightContactListener knightContactListener) {
        this.knight = knight;
        this.world = world;
        this.knightContactListener = knightContactListener;
        this.previousGrounded = knight.isGrounded();
        this.knightAnimator = new KnightAnimator(knight);
    }

    public void setGameRunManager(GameRunManager gameRunManager) {
        this.gameRunManager = gameRunManager;
    }

    private float getDashCooldown() {
        return CharmManager.getInstance().isCharmEquipped(CharmType.DASHMASTER) ? 0.6f : 2.0f;
    }

    private float getNailFrameDuration() {
        return CharmManager.getInstance().isCharmEquipped(CharmType.QUICK_FLASH) ? 0.02f : 0.07f;
    }

    private float getTotalAttackDuration() {
        return getNailFrameDuration() * 5;
    }

    private float getPeakStart() {
        return getNailFrameDuration() * 2;
    }

    private float getPeakEnd() {
        return getNailFrameDuration() * 3;
    }

    private float getFocusTime() {
        return CharmManager.getInstance().isCharmEquipped(CharmType.QUICK_FOCUS) ? 0.6f : 1.5f;
    }

    public void update(float deltaTime) {
        CheatManager.getInstance().update(knight, deltaTime);

        if (CheatManager.getInstance().isGodMode()) {
            knight.setCurrentHealth(knight.getMaxHealth());
            knight.setLocked(false);
            knight.clearDamageFlag();
        }

        if (CheatManager.getInstance().isNoclip()) {
            knight.setLocked(false);
            knight.update(deltaTime);
            updateState();
            return;
        }

        if (knightContactListener != null && world != null) {
            knightContactListener.updateStates(world);
        }

        knight.update(deltaTime);
        if (knight.isDamagedThisFrame()) {
            knight.setFocusing(false);
            knight.setLocked(false);
            focusTimer = 0f;
            knight.clearDamageFlag();
        }

        updateTimers(deltaTime);
        handleFocus(ControlManager.getInstance(), deltaTime);
        handleInputAndPhysics();
        if (knight.isLocked() && knight.getInvincibleTimer() < 1.0f) {
            knight.setLocked(false);
        }
        updateState();
        checkLanding();

        KnightState currentState = knight.getCurrentState();
        if (currentState == KnightState.RUN) {
            AudioManager.getInstance().loopSfx(SfxType.WALK);
        } else {
            AudioManager.getInstance().stopLoop(SfxType.WALK);
        }

        if (currentState == KnightState.WALL_SLIDE) {
            AudioManager.getInstance().loopSfx(SfxType.WALL_SLIDE);
        } else {
            AudioManager.getInstance().stopLoop(SfxType.WALL_SLIDE);
        }
    }

    private void updateTimers(float dt) {
        if (knight.isImmuneToSpikes()) knight.updatePogoImmunityTimer(dt);
        if (dashTimer > 0) dashTimer -= dt;
        if (dashCooldownTimer > 0) {dashCooldownTimer -= dt;}
        if (landingTimer > 0) landingTimer -= dt;
        if (wallJumpTimer > 0) wallJumpTimer -= dt;
        if (castTimer > 0) castTimer -= dt;
        if (attackTimer > 0) {
            attackTimer -= dt;
            float elapsed = getTotalAttackDuration() - attackTimer;
            if (elapsed >= getPeakStart() && elapsed <= getPeakEnd()) {
                knight.getNail().setActive(true, knight.isFacingRight(), currentAttackDirection);
            } else {
                knight.getNail().setActive(false, knight.isFacingRight(), currentAttackDirection);
            }
        } else {
            comboCount = 0;
            currentAttackDirection = KnightState.SLASH;
            knight.getNail().setActive(false, knight.isFacingRight(), currentAttackDirection);
            knight.getNail().setHasHit(false);
        }
    }

    private void handleInputAndPhysics() {
        if (knight == null || knight.getBody() == null) return;
        if (knight.isLocked() || knight.isFocusing() || knight.getCurrentHealth() <= 0) return;

        ControlManager cm = ControlManager.getInstance();
        Vector2 velocity = knight.getBody().getLinearVelocity();
        boolean canMove = dashTimer <= 0 && wallJumpTimer <= 0;
        if (canMove) {
            handleMovement(cm, velocity);
        }
        handleDash(cm, velocity);
        handleJump(cm, velocity);
        handleAttack(cm);
        handleWallSlide(velocity);

        if (knight.getBody() != null) {
            knight.getBody().setLinearVelocity(velocity);
        }
    }

    private void handleMovement(ControlManager cm, Vector2 velocity) {
        boolean moveRight = cm.isActionPressed(MovementStatus.MOVE_RIGHT);
        boolean moveLeft = cm.isActionPressed(MovementStatus.MOVE_LEFT);
        if (moveRight) {
            velocity.x = MAX_VELOCITY;
            knight.setFacingRight(true);
        } else if (moveLeft) {
            velocity.x = -MAX_VELOCITY;
            knight.setFacingRight(false);
        } else {
            velocity.x = 0;
        }
    }

    private void handleDash(ControlManager cm, Vector2 velocity) {
        if (knight.getBody() == null) return;

        if (cm.isActionJustPressed(MovementStatus.DASH) && dashTimer <= 0 && dashCooldownTimer <= 0) {
            dashTimer = knightAnimator.getAnimationDuration(KnightState.DASH);
            dashCooldownTimer = getDashCooldown();
            knight.setDashing(true);
            AudioManager.getInstance().playSfx(SfxType.DASH);
        }

        if (dashTimer > 0) {
            velocity.x = knight.isFacingRight() ? DASH_VELOCITY : -DASH_VELOCITY;
            velocity.y = 0;
            knight.getBody().setGravityScale(0);
        } else {
            knight.getBody().setGravityScale(GRAVITY_SCALE);
            knight.setDashing(false);
        }
    }

    private void handleJump(ControlManager cm, Vector2 velocity) {
        boolean jumpPressed = cm.isActionPressed(MovementStatus.JUMP);
        boolean jumpJustPressed = cm.isActionJustPressed(MovementStatus.JUMP);
        if (jumpJustPressed) {
            if (knight.isGrounded()) {
                velocity.y = JUMP_VELOCITY;
                knight.setCanDoubleJump(true);
                isJumping = true;
                AudioManager.getInstance().playSfx(SfxType.JUMP);
            } else if (knight.isOnWall()) {
                velocity.x = knight.isFacingRight() ? -WALL_JUMP_VELOCITY_X : WALL_JUMP_VELOCITY_X;
                velocity.y = WALL_JUMP_VELOCITY_Y;
                wallJumpTimer = 0.25f;
                knight.setFacingRight(!knight.isFacingRight());
                isJumping = true;
                AudioManager.getInstance().playSfx(SfxType.JUMP);
            } else if (knight.isCanDoubleJump()) {
                velocity.y = DOUBLE_JUMP_VELOCITY;
                knight.setCanDoubleJump(false);
                isJumping = true;
                AudioManager.getInstance().playSfx(SfxType.DOUBLE_JUMP);
            }
        }
        if (!jumpPressed && isJumping && velocity.y > 0) {
            velocity.y *= JUMP_CUTOFF_FACTOR;
            isJumping = false;
        }
    }

    private void handleWallSlide(Vector2 velocity) {
        if (knight.isOnWall() && !knight.isGrounded() && dashTimer <= 0 && wallJumpTimer <= 0) {
            velocity.y = WALL_SLIDE_SPEED;
        }
    }

    private void handleAttack(ControlManager cm) {
        if (knight.getBody() == null) return;

        if (cm.isActionJustPressed(MovementStatus.NAIL_ATTACK) && attackTimer <= 0) {
            comboCount = (comboCount == 0) ? 1 : 0;
            attackTimer = getTotalAttackDuration();
            knight.getNail().setHasHit(false);
            AudioManager.getInstance().playSfx(SfxType.NAIL);
            if (cm.isActionPressed(MovementStatus.LOOK_UP)) {
                currentAttackDirection = KnightState.UP_SLASH;
            } else if (!knight.isGrounded() && cm.isActionPressed(MovementStatus.LOOK_DOWN)) {
                currentAttackDirection = KnightState.DOWN_SLASH;
            } else {
                currentAttackDirection = comboCount == 1 ? KnightState.SLASH_ALT : KnightState.SLASH;
            }

            float recoilForce = knight.isFacingRight() ? -1.5f : 1.5f;
            if (CharmManager.getInstance().isCharmEquipped(CharmType.HEAVY_BLOW)) {
                recoilForce *= 1.8f;
            }

            if (currentAttackDirection == KnightState.UP_SLASH) {
                knight.getBody().applyLinearImpulse(new Vector2(recoilForce * 0.5f, 0.5f), knight.getBody().getWorldCenter(), true);
            } else if (currentAttackDirection != KnightState.DOWN_SLASH && knight.isGrounded()) {
                knight.getBody().applyLinearImpulse(new Vector2(recoilForce, 0.2f), knight.getBody().getWorldCenter(), true);
            }
        }
    }

    private void handleFocus(ControlManager cm, float dt) {
        boolean pressed = cm.isActionPressed(MovementStatus.FOCUS);

        if (focusKeyConsumed) {
            if (!pressed) {
                focusKeyConsumed = false;
            }
            return;
        }

        if (!pressed) {
            focusTimer = 0;
            knight.setFocusing(false);
            knight.setLocked(false);
            return;
        }

        if (!knight.isFocusing()) {
            knight.setFocusing(true);
            knight.setLocked(true);
        }

        focusTimer += dt;

        if (focusTimer >= getFocusTime()) {
            if (knight.getSoul() >= FOCUS_SOUL_COST &&
                knight.getCurrentHealth() < knight.getMaxHealth()) {

                knight.setSoul(knight.getSoul() - FOCUS_SOUL_COST);
                knight.setCurrentHealth(knight.getCurrentHealth() + 1);
            }

            knight.setFocusing(false);
            knight.setLocked(false);
            focusTimer = 0;
            focusKeyConsumed = true;
        }
    }

    private void checkLanding() {
        if (!previousGrounded && knight.isGrounded()) {
            landingTimer = 0.15f;
            AudioManager.getInstance().playSfx(SfxType.LAND);
        }
        previousGrounded = knight.isGrounded();
    }

    private void cancelFocus() {
        focusTimer = 0f;
        knight.setFocusing(false);
    }

    private void updateState() {
        if (knight.getBody() == null) return;

        if (CheatManager.getInstance().isNoclip()) {
            knight.setCurrentState(KnightState.IDLE);
            return;
        }

        if (knight.getCurrentHealth() <= 0) {
            if (!deathProcessed && gameRunManager != null) {
                AudioManager.getInstance().playSfx(SfxType.KNIGHT_DEATH);
                gameRunManager.playerDied();
                deathProcessed = true;
            }
            knight.setCurrentState(KnightState.DEATH);
            return;
        }

        deathProcessed = false;

        if (knight.getInvincibleTimer() >= 1.0f) {
            knight.setCurrentState(KnightState.IDLE_HURT);
            return;
        }
        if (knight.getCurrentState() == KnightState.TRANSITION) return;
        if (knight.isFocusing()) {
            knight.setCurrentState(KnightState.FOCUS);
            return;
        }
        if (dashTimer > 0) {
            knight.setCurrentState(KnightState.DASH);
            return;
        }
        if (castTimer > 0) {
            knight.setCurrentState(KnightState.FIREBALL_CAST);
            return;
        }
        if (attackTimer > 0) {
            knight.setCurrentState(currentAttackDirection);
            return;
        }
        if (wallJumpTimer > 0) {
            knight.setCurrentState(KnightState.WALL_JUMP);
            return;
        }
        if (knight.isOnWall() && !knight.isGrounded() && knight.getBody().getLinearVelocity().y < -0.1f) {
            knight.setCurrentState(KnightState.WALL_SLIDE);
            return;
        }
        if (landingTimer > 0) {
            knight.setCurrentState(KnightState.LANDING);
            return;
        }
        if (!knight.isGrounded()) {
            if (knight.getBody().getLinearVelocity().y > 0 && !knight.isCanDoubleJump()) {
                knight.setCurrentState(KnightState.DOUBLE_JUMP);
            } else {
                knight.setCurrentState(KnightState.AIRBORNE);
            }
            return;
        }

        ControlManager cm = ControlManager.getInstance();
        boolean isMovingIntent = cm.isActionPressed(MovementStatus.MOVE_LEFT) || cm.isActionPressed(MovementStatus.MOVE_RIGHT);

        if (isMovingIntent && Math.abs(knight.getBody().getLinearVelocity().x) > 0.05f) {
            knight.setCurrentState(KnightState.RUN);
            return;
        }

        if (cm.isActionPressed(MovementStatus.LOOK_UP)) {
            knight.setCurrentState(KnightState.LOOK_UP);
            return;
        }
        if (cm.isActionPressed(MovementStatus.LOOK_DOWN)) {
            knight.setCurrentState(KnightState.LOOK_DOWN);
            return;
        }
        knight.setCurrentState(KnightState.IDLE);
    }

    public void resetAfterRespawn() {
        this.deathProcessed = false;
        knight.setCurrentState(KnightState.IDLE);
        knight.setLocked(false);
        this.dashCooldownTimer = 0;
        this.attackTimer = 0;
        this.dashTimer = 0;
        knight.getBody().setLinearVelocity(0, 0);
    }
}

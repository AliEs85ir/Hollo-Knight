package ir.Ali.hollowknightme.controller.enemy;

import ir.Ali.hollowknightme.enums.enemy.EnemyState;
import ir.Ali.hollowknightme.model.enemy.WingedSentry;

public class WingedSentryController extends EnemyController {
    private final WingedSentry sentry;
    private boolean returning;
    private float chargeDirectionX;

    public WingedSentryController(WingedSentry sentry) {
        super(sentry);
        this.sentry = sentry;
    }

    @Override
    public void update(float dt) {
        updateTimers(dt);
        if (!sentry.isAlive()) {
            handleDeath();
            return;
        }
        if (sentry.isLocked()) return;

        if (returning) {
            updateReturn();
            return;
        }

        switch (getState()) {
            case CHARGE_ANTIC -> updateChargeAntic();
            case CHARGE -> updateCharge();
            default -> updateIdle();
        }
    }

    private void updateIdle() {
        stop();
        if (playerInVision()) {
            startChargeAntic();
        } else if (!isAtInitialPosition()) {
            returning = true;
        }
    }

    private void startChargeAntic() {
        stop();
        setState(EnemyState.CHARGE_ANTIC);
        startTimer(EnemyState.CHARGE_ANTIC, sentry.getAnticDuration());
    }

    private void updateChargeAntic() {
        stop();
        if (!isTimerActive(EnemyState.CHARGE_ANTIC)) {
            chargeDirectionX = sentry.isFacingRight() ? 1f : -1f;
            setState(EnemyState.CHARGE);
            startTimer(EnemyState.CHARGE, sentry.getChargeDuration());
        }
    }

    private void updateCharge() {
        if (!isTimerActive(EnemyState.CHARGE)) {
            endCharge();
            return;
        }
        move(chargeDirectionX * sentry.getChargeSpeed(), 0f);
    }

    private void endCharge() {
        stop();
        if (playerInVision()) {
            startChargeAntic();
        } else {
            returning = true;
            setState(EnemyState.IDLE);
        }
    }

    private void updateReturn() {
        if (playerInVision()) {
            returning = false;
            startChargeAntic();
            return;
        }

        float dx = sentry.getInitialPosition().x - sentry.getX();
        float dy = sentry.getInitialPosition().y - sentry.getY();

        if (Math.abs(dx) < 0.1f && Math.abs(dy) < 0.1f) {
            sentry.setPosition(sentry.getInitialPosition().x, sentry.getInitialPosition().y);
            stop();
            returning = false;
            if (sentry.isFacingRight() != sentry.isInitialFacingRight()) {
                sentry.setFacingDirection(sentry.isInitialFacingRight());
            }
            setState(EnemyState.IDLE);
            return;
        }

        float dirX = Math.signum(dx);
        float dirY = Math.signum(dy);

        if (Math.abs(dx) > 0.1f && sentry.isFacingRight() != (dirX > 0)) {
            sentry.setFacingDirection(dirX > 0);
        }

        float vx = Math.abs(dx) > 0.1f ? dirX * sentry.getReturnSpeed() : 0;
        float vy = Math.abs(dy) > 0.1f ? dirY * sentry.getReturnSpeed() : 0;

        move(vx, vy);
        setState(EnemyState.RUN);
    }

    private boolean playerInVision() {
        if (target == null) return false;
        float x = sentry.getX();
        float y = sentry.getY();
        boolean facingRight = sentry.isFacingRight();
        float visionWidth = sentry.getVisionRange().width;
        float visionHeight = sentry.getVisionRange().height;

        float visionMinX = facingRight ? x : x - visionWidth;
        float visionMaxX = facingRight ? x + visionWidth : x;
        float visionMinY = y - (visionHeight * 0.2f);
        float visionMaxY = y + (visionHeight * 0.3f);

        return target.getX() >= visionMinX && target.getX() <= visionMaxX &&
            target.getY() >= visionMinY && target.getY() <= visionMaxY;
    }

    private boolean isAtInitialPosition() {
        float dx = Math.abs(sentry.getInitialPosition().x - sentry.getX());
        float dy = Math.abs(sentry.getInitialPosition().y - sentry.getY());
        return dx < 0.1f && dy < 0.1f;
    }

    private void handleDeath() {
        if (sentry.getBody().getGravityScale() == 0f) {
            sentry.getBody().setGravityScale(1.5f);
        }

        if (sentry.isGrounded() && getState() != EnemyState.DEATH_LAND) {
            setState(EnemyState.DEATH_LAND);
            startTimer(EnemyState.DEATH_LAND, sentry.getDeathDuration());
        } else if (!sentry.isGrounded() && getState() != EnemyState.DEATH_AIR && getState() != EnemyState.DEATH_LAND) {
            setState(EnemyState.DEATH_AIR);
        }

        if (getState() == EnemyState.DEATH_LAND && !isTimerActive(EnemyState.DEATH_LAND)) {
            sentry.getBody().setActive(false);
        }
    }

    @Override
    protected void stop() {
        if (sentry.isAlive()) {
            sentry.getBody().setLinearVelocity(0, 0);
        }
    }

    @Override
    public void enemyHitPlayer(float knightHitDir) {
        super.enemyHitPlayer(knightHitDir);
        if (sentry.isAlive() && getState() == EnemyState.CHARGE) {
            setState(EnemyState.IDLE);
        }
    }

    @Override
    public void enemyHitWall() {
        super.enemyHitWall();
        if (sentry.isAlive() && getState() == EnemyState.CHARGE) {
            endCharge();
        }
    }

    @Override
    public void playerHitEnemy(float damage, float hitDir) {
        super.playerHitEnemy(damage, hitDir);
        if (sentry.isAlive() && getState() == EnemyState.CHARGE) {
            setState(EnemyState.IDLE);
        }
    }
}

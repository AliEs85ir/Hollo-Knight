package ir.Ali.hollowknightme.controller.enemy;

import com.badlogic.gdx.physics.box2d.World;
import ir.Ali.hollowknightme.model.enemy.CrystalGuardian;
import ir.Ali.hollowknightme.enums.enemy.EnemyState;

public class CrystalGuardianController extends GroundEnemyController {
    private final CrystalGuardian guardian;
    private boolean returning;
    private final boolean guardFacingRight;
    private boolean charging;
    private float chargeTimer;
    private static final float CHARGE_DURATION = 2.0f;

    public CrystalGuardianController(CrystalGuardian enemy, World world) {
        super(enemy, world);
        this.guardian = enemy;
        this.guardFacingRight = enemy.isFacingRight();
    }

    @Override
    public void update(float dt) {
        updateTimers(dt);
        if (!guardian.isAlive()) {
            guardian.getLaser().setActive(false, guardian.isFacingRight());
            charging = false;
            handleDeath();
            return;
        }
        if (guardian.isLocked()) return;

        if (returning) {
            updateReturn();
            return;
        }

        if (charging) {
            updateCharging(dt);
            return;
        }

        switch (getState()) {
            case SHOOT -> updateShoot();
            case RUN -> updateEnraged();
            default -> updateIdle();
        }
    }

    private void updateCharging(float dt) {
        stop();
        chargeTimer -= dt;
        faceTarget();
        if (chargeTimer <= 0) {
            charging = false;
            startLaserAttack();
        }
    }

    private void startCharging() {
        charging = true;
        chargeTimer = CHARGE_DURATION;
        setState(EnemyState.TURN);
    }

    private void updateIdle() {
        stop();
        if (playerInVision()) {
            startCharging();
        }
    }

    private void updateShoot() {
        stop();
        if (!isTimerActive(EnemyState.SHOOT)) {
            guardian.getLaser().setActive(false, guardian.isFacingRight());
            if (playerInVision()) {
                faceTarget();
            }
            setState(EnemyState.RUN);
            startTimer(EnemyState.RUN, guardian.getEnrageDuration());
        }
    }

    private void updateEnraged() {
        if (!isTimerActive(EnemyState.RUN) || target == null || isLedgeAhead()) {
            stop();
            if (playerInVision()) {
                startCharging();
            } else {
                returning = true;
                setState(EnemyState.IDLE);
            }
            return;
        }
        float dir = Math.signum(target.getX() - guardian.getX());
        if (dir != 0) {
            boolean targetRight = dir > 0;
            if (guardian.isFacingRight() != targetRight) {
                guardian.setFacingDirection(targetRight);
            }
            move(dir * guardian.getSpeed(), guardian.getBody().getLinearVelocity().y);
        }
    }

    private void updateReturn() {
        if (playerInVision()) {
            returning = false;
            startCharging();
            return;
        }
        float dx = guardian.getGuardPosition().x - guardian.getX();
        if (Math.abs(dx) < 0.2f || isLedgeAhead()) {
            stop();
            returning = false;
            if (guardian.isFacingRight() != guardFacingRight) {
                guardian.setFacingDirection(guardFacingRight);
            }
            setState(EnemyState.IDLE);
            return;
        }
        float dir = Math.signum(dx);
        boolean dirRight = dir > 0;
        if (guardian.isFacingRight() != dirRight) {
            guardian.setFacingDirection(dirRight);
        }
        move(dir * guardian.getSpeed(), guardian.getBody().getLinearVelocity().y);
    }

    private void startLaserAttack() {
        setState(EnemyState.SHOOT);
        startTimer(EnemyState.SHOOT, guardian.getLaserDuration());
        guardian.getLaser().setActive(true, guardian.isFacingRight());
    }

    private boolean playerInVision() {
        if (target == null) return false;
        float x = guardian.getX();
        float y = guardian.getY();
        boolean facingRight = guardian.isFacingRight();
        float visionWidth = guardian.getVisionRange().width;
        float visionHeight = guardian.getVisionRange().height;
        float visionMinX = facingRight ? x : x - visionWidth;
        float visionMaxX = facingRight ? x + visionWidth : x;
        float visionMinY = y - (visionHeight * 0.27f);
        float visionMaxY = y + (visionHeight * 0.27f);
        return target.getX() >= visionMinX && target.getX() <= visionMaxX &&
            target.getY() >= visionMinY && target.getY() <= visionMaxY;
    }

    private void faceTarget() {
        if (target == null) return;
        float dx = target.getX() - guardian.getX();
        if (dx > 0 && !guardian.isFacingRight()) {
            guardian.setFacingDirection(true);
        } else if (dx < 0 && guardian.isFacingRight()) {
            guardian.setFacingDirection(false);
        }
    }

    @Override
    public void enemyHitWall() {
        if (getState() == EnemyState.RUN) {
            returning = true;
            setState(EnemyState.IDLE);
        }
    }

    @Override
    public void playerHitEnemy(float damage, float hitDir) {
        super.playerHitEnemy(damage, hitDir);
        if (guardian.isAlive()) {
            returning = false;
            boolean shouldFaceRight = hitDir < 0;
            guardian.setFacingDirection(shouldFaceRight);
            if (getState() == EnemyState.IDLE || getState() == EnemyState.WALK) {
                if (!charging && getState() != EnemyState.SHOOT) {
                    startCharging();
                }
            }
        }
    }
}

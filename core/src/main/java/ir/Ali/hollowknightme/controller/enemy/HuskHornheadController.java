package ir.Ali.hollowknightme.controller.enemy;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import ir.Ali.hollowknightme.enums.enemy.EnemyState;
import ir.Ali.hollowknightme.model.enemy.HuskHornhead;

public class HuskHornheadController extends GroundEnemyController {
    private final HuskHornhead husk;
    private boolean attacking;
    private boolean resting;
    private float walkTimer = 0f;
    private float targetWalkDuration;
    private float lungeTime = 0f;

    public HuskHornheadController(HuskHornhead enemy, World world) {
        super(enemy, world);
        this.husk = enemy;
        setNextWalkDuration();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        if (!groundEnemy.isAlive() || groundEnemy.isLocked()) return;

        if (resting) {
            updateRest();
            return;
        }

        switch (getState()) {
            case ATTACK_ANTICIPATE -> updateAnticipate();
            case ATTACK_LUNGE -> updateLunge(dt);
            case ATTACK_COOLDOWN -> updateCooldown();
            default -> {
                if (tryStartAttack()) return;
                if (tryRest(dt)) return;
                updatePatrol();
            }
        }
    }

    private void setNextWalkDuration() {
        int rand = MathUtils.random(1, 100);
        if (rand <= 15) {
            targetWalkDuration = 6f;
        } else if (rand <= 30) {
            targetWalkDuration = 7f;
        } else if (rand <= 65) {
            targetWalkDuration = 8f;
        } else if (rand <= 90) {
            targetWalkDuration = 10f;
        } else {
            targetWalkDuration = 12f;
        }
    }

    private boolean tryStartAttack() {
        if (isTimerActive(EnemyState.ATTACK_COOLDOWN) || !playerInVision()) {
            return false;
        }
        attacking = true;
        walkTimer = 0f;
        stop();
        setState(EnemyState.ATTACK_ANTICIPATE);
        startTimer(EnemyState.ATTACK_ANTICIPATE, husk.getAttackAnticipateDuration());
        return true;
    }

    private void updateAnticipate() {
        stop();
        if (!isTimerActive(EnemyState.ATTACK_ANTICIPATE)) {
            lungeTime = 0f;
            setState(EnemyState.ATTACK_LUNGE);
        }
    }

    private void updateLunge(float dt) {
        lungeTime += dt;
        float dir = groundEnemy.isFacingRight() ? 1f : -1f;
        move(dir * husk.getLungeSpeed(), groundEnemy.getBody().getLinearVelocity().y);

        float x = groundEnemy.getX();
        boolean stuckOnWall = lungeTime > 0.1f && Math.abs(groundEnemy.getBody().getLinearVelocity().x) < 0.5f;

        if (x <= groundEnemy.getPatrolMinX() || x >= groundEnemy.getPatrolMaxX() || isLedgeAhead() || stuckOnWall) {
            finishAttack();
            fastFlip();
        }
    }

    private void updateCooldown() {
        stop();
        if (!isTimerActive(EnemyState.ATTACK_COOLDOWN)) {
            attacking = false;
            setState(EnemyState.WALK);
        }
    }

    private boolean tryRest(float dt) {
        EnemyState currentState = getState();
        if (currentState == EnemyState.WALK || currentState == EnemyState.TURN) {
            walkTimer += dt;
        }
        if (walkTimer >= targetWalkDuration) {
            resting = true;
            walkTimer = 0f;
            setNextWalkDuration();
            stop();
            setState(EnemyState.IDLE);
            startTimer(EnemyState.IDLE, husk.getRestDuration());
            return true;
        }
        return false;
    }

    private void updateRest() {
        stop();
        if (!isTimerActive(EnemyState.IDLE)) {
            resting = false;
            setState(EnemyState.WALK);
        }
    }

    private void finishAttack() {
        attacking = false;
        stop();
        setState(EnemyState.ATTACK_COOLDOWN);
        startTimer(EnemyState.ATTACK_COOLDOWN, husk.getAttackCooldown());
    }

    private boolean playerInVision() {
        if (target == null) return false;

        Rectangle r = husk.getVisionRange();
        float x = groundEnemy.getX();
        float y = groundEnemy.getY();
        boolean facingRight = groundEnemy.isFacingRight();

        float visionMinX = facingRight ? x : x - r.width;
        float visionMaxX = facingRight ? x + r.width : x;
        float visionMinY = y - (r.height * 0.20f);
        float visionMaxY = y + (r.height * 0.25f);

        return target.getX() >= visionMinX && target.getX() <= visionMaxX &&
            target.getY() >= visionMinY && target.getY() <= visionMaxY;
    }

    @Override
    public void enemyHitWall() {
        if (attacking) {
            finishAttack();
            fastFlip();
        } else {
            super.enemyHitWall();
        }
    }

    @Override
    public void playerHitEnemy(float damage, float hitDir) {
        if (attacking) {
            finishAttack();
        }
        super.playerHitEnemy(damage, hitDir);
    }

    @Override
    public void enemyHitPlayer(float knightHitDir) {
        if (attacking) {
            finishAttack();
        }
        super.enemyHitPlayer(knightHitDir);
    }


}

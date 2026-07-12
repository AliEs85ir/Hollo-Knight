package ir.Ali.hollowknightme.controller.enemy;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import ir.Ali.hollowknightme.enums.fixture.FixtureType;
import ir.Ali.hollowknightme.enums.enemy.EnemyState;
import ir.Ali.hollowknightme.model.enemy.GroundEnemy;

public abstract class GroundEnemyController extends EnemyController {
    protected final GroundEnemy groundEnemy;
    protected final World world;
    protected boolean isFlipping;

    public GroundEnemyController(GroundEnemy enemy, World world) {
        super(enemy);
        this.groundEnemy = enemy;
        this.world = world;
    }

    @Override
    public void update(float dt) {
        updateTimers(dt);
        if (!groundEnemy.isAlive()) {
            handleDeath();
        }
    }

    protected void updatePatrol() {
        if (groundEnemy.isLocked() || !groundEnemy.isGrounded()) return;

        EnemyState state = getState();
        if (state != EnemyState.WALK && state != EnemyState.IDLE && state != EnemyState.TURN) return;

        if (updateTurn()) return;

        if (isLedgeAhead()) {
            flip();
            return;
        }

        float speed = (groundEnemy.isFacingRight() ? 1f : -1f) * groundEnemy.getWalkSpeed();
        move(speed, groundEnemy.getBody().getLinearVelocity().y);

        if (state != EnemyState.WALK) {
            setState(EnemyState.WALK);
        }
    }

    protected boolean isLedgeAhead() {
        float x = groundEnemy.getX();
        float y = groundEnemy.getY();
        boolean facingRight = groundEnemy.isFacingRight();
        float offset = facingRight ? 0.3f : -0.3f;
        Vector2 start = new Vector2(x + offset, y);
        Vector2 end = new Vector2(x + offset, y - 1.2f);
        final boolean[] foundGround = {false};

        world.rayCast((fixture, point, normal, fraction) -> {
            Object data = fixture.getUserData();
            if (data == FixtureType.GROUND || data == FixtureType.PLATFORM || data == FixtureType.WALL) {
                foundGround[0] = true;
                return 0;
            }
            return -1;
        }, start, end);

        return !foundGround[0];
    }

    @Override
    public void enemyHitWall() {
        if (!groundEnemy.isLocked()) {
            flip();
        }
    }

    protected boolean updateTurn() {
        if (!isFlipping) return false;
        if (isTimerActive(EnemyState.TURN)) {
            stop();
            return true;
        }
        isFlipping = false;
        setState(EnemyState.WALK);
        return false;
    }

    protected void flip() {
        if (isFlipping) return;
        isFlipping = true;
        groundEnemy.setFacingDirection(!groundEnemy.isFacingRight());
        setState(EnemyState.TURN);
        startTimer(EnemyState.TURN, 0.1f);
        stop();
    }

    protected void fastFlip() {
        groundEnemy.setFacingDirection(!groundEnemy.isFacingRight());
    }

    protected void handleDeath() {
        EnemyState current = getState();

        if (current != EnemyState.DEATH_AIR && current != EnemyState.DEATH_LAND) {
            groundEnemy.setGrounded(false);
            setState(EnemyState.DEATH_AIR);
            return;
        }

        if (current == EnemyState.DEATH_LAND) {
            Vector2 vel = groundEnemy.getBody().getLinearVelocity();
            float dampedVx = vel.x * 0.85f;
            if (Math.abs(dampedVx) < 0.2f) {
                dampedVx = 0f;
            }
            groundEnemy.getBody().setLinearVelocity(dampedVx, vel.y);
        }
    }
}

package ir.Ali.hollowknightme.model.enemy;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import ir.Ali.hollowknightme.enums.enemy.EnemyState;
import ir.Ali.hollowknightme.enums.enemy.EnemyType;

public class CrystalGuardian extends GroundEnemy {
    private final Rectangle visionRange;
    private final float laserDuration;
    private final float enrageDuration;
    private final Vector2 guardPosition;
    private final EnemyLaser laser;

    public CrystalGuardian(Body body, float hp, float damage, float speed,
                           Rectangle visionRange, float laserDuration,
                           float enrageDuration, boolean facingRight) {
        super(EnemyType.CRYSTAL_GUARDIAN, body, hp, damage, speed);
        this.visionRange = visionRange;
        this.laserDuration = laserDuration;
        this.enrageDuration = enrageDuration;
        this.guardPosition = new Vector2(body.getPosition());
        this.currentState = EnemyState.IDLE;
        this.normalKnockback.set(4f, 3f);
        this.setCollisionKnockback(5f, 3f);
        this.deathKnockback.set(10, 7);
        this.facingRight = facingRight;
        this.laser = new EnemyLaser(body, 25f, 0.5f, 0.5f, -0.0f, damage);
    }

    @Override
    protected boolean isValidState(EnemyState state) {
        return switch (state) {
            case IDLE, EVADE, RUN, SHOOT, TURN, DEATH_AIR, DEATH_LAND -> true;
            default -> false;
        };
    }

    public Rectangle getVisionRange() {
        return visionRange;
    }

    public float getLaserDuration() {
        return laserDuration;
    }

    public float getEnrageDuration() {
        return enrageDuration;
    }

    public Vector2 getGuardPosition() {
        return guardPosition;
    }

    public EnemyLaser getLaser() {
        return laser;
    }

}

package ir.Ali.hollowknightme.model.enemy;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import ir.Ali.hollowknightme.enums.enemy.EnemyState;
import ir.Ali.hollowknightme.enums.enemy.EnemyType;

public class WingedSentry extends Enemy {
    private final Rectangle visionRange;
    private final float anticDuration;
    private final float chargeSpeed;
    private final float returnSpeed;
    private final float chargeDuration;
    private final Vector2 initialPosition;
    private final boolean initialFacingRight;

    public WingedSentry(Body body, float hp, float damage, float returnSpeed, float chargeSpeed,
                        Rectangle visionRange, float anticDuration, float chargeDuration, boolean facingRight) {
        super(EnemyType.WINGED_SENTRY, body, hp, damage, returnSpeed);
        this.visionRange = visionRange;
        this.anticDuration = anticDuration;
        this.chargeSpeed = chargeSpeed;
        this.returnSpeed = returnSpeed;
        this.chargeDuration = chargeDuration;
        this.facingRight = facingRight;
        this.initialFacingRight = facingRight;
        this.initialPosition = new Vector2(body.getPosition());
        this.currentState = EnemyState.IDLE;

        this.normalKnockback.set(5f, 4f);
        this.setCollisionKnockback(6f, 4f);
        this.deathKnockback.set(6f, 5f);

        this.body.setGravityScale(0f);
    }

    @Override
    protected boolean isValidState(EnemyState state) {
        return switch (state) {
            case IDLE, RUN, CHARGE_ANTIC, CHARGE, TURN, DEATH_AIR, DEATH_LAND -> true;
            default -> false;
        };
    }

    public Rectangle getVisionRange() {
        return visionRange;
    }

    public float getAnticDuration() {
        return anticDuration;
    }

    public float getChargeSpeed() {
        return chargeSpeed;
    }

    public float getReturnSpeed() {
        return returnSpeed;
    }

    public float getChargeDuration() {
        return chargeDuration;
    }

    public Vector2 getInitialPosition() {
        return initialPosition;
    }

    public boolean isInitialFacingRight() {
        return initialFacingRight;
    }
}

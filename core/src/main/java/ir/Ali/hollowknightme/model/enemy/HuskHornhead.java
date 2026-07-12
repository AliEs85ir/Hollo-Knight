package ir.Ali.hollowknightme.model.enemy;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import ir.Ali.hollowknightme.enums.enemy.EnemyState;
import ir.Ali.hollowknightme.enums.enemy.EnemyType;

public class HuskHornhead extends GroundEnemy {
    private final Rectangle visionRange;
    private final float lungeSpeed;
    private final float restDuration;
    private final float attackCooldown;
    private final float attackAnticipateDuration;

    public HuskHornhead(Body body, float hp, float damage, float speed,
                        float lungeSpeed, float restDuration,
                        float attackCooldown, float attackAnticipateDuration,
                        Rectangle visionRange) {
        super(EnemyType.HUSK_HORNHEAD, body, hp, damage, speed);
        this.lungeSpeed = lungeSpeed;
        this.restDuration = restDuration;
        this.attackCooldown = attackCooldown;
        this.attackAnticipateDuration = attackAnticipateDuration;
        this.visionRange = visionRange;
        this.currentState = EnemyState.WALK;

        this.normalKnockback.set(6f, 3f);
        this.setCollisionKnockback(6f, 2f);
        this.deathKnockback.set(5f , 10f);
    }

    public HuskHornhead(Body body, float hp, float damage, float speed,
                        float patrolMinX, float patrolMaxX,
                        float lungeSpeed, float restDuration,
                        float attackCooldown, float attackAnticipateDuration,
                        Rectangle visionRange) {
        super(EnemyType.HUSK_HORNHEAD, body, hp, damage, speed, patrolMinX, patrolMaxX);
        this.lungeSpeed = lungeSpeed;
        this.restDuration = restDuration;
        this.attackCooldown = attackCooldown;
        this.attackAnticipateDuration = attackAnticipateDuration;
        this.visionRange = visionRange;
        this.currentState = EnemyState.WALK;
    }

    @Override
    protected boolean isValidState(EnemyState state) {
        return switch (state) {
            case IDLE, WALK, TURN, ATTACK_ANTICIPATE, ATTACK_LUNGE, ATTACK_COOLDOWN, DEATH_AIR, DEATH_LAND -> true;
            default -> false;
        };
    }

    public Rectangle getVisionRange() { return visionRange; }
    public float getLungeSpeed() { return lungeSpeed; }
    public float getRestDuration() { return restDuration; }
    public float getAttackCooldown() { return attackCooldown; }
    public float getAttackAnticipateDuration() { return attackAnticipateDuration; }
}

package ir.Ali.hollowknightme.model.enemy;

import com.badlogic.gdx.physics.box2d.Body;
import ir.Ali.hollowknightme.enums.enemy.EnemyType;

public abstract class GroundEnemy extends Enemy {
    protected float patrolMinX = Float.NEGATIVE_INFINITY;
    protected float patrolMaxX = Float.POSITIVE_INFINITY;
    protected float walkSpeed;

    public GroundEnemy(EnemyType type, Body body, float hp, float damage, float speed) {
        super(type, body, hp, damage, speed);
        this.walkSpeed = speed;
    }

    public GroundEnemy(EnemyType type, Body body, float hp, float damage, float speed, float patrolMinX, float patrolMaxX) {
        this(type, body, hp, damage, speed);
        this.patrolMinX = patrolMinX;
        this.patrolMaxX = patrolMaxX;
    }

    public void setPatrolRange(float minX, float maxX) {
        this.patrolMinX = minX;
        this.patrolMaxX = maxX;
    }

    public float getPatrolMinX() { return patrolMinX; }
    public float getPatrolMaxX() { return patrolMaxX; }
    public float getWalkSpeed() { return walkSpeed; }
}

package ir.Ali.hollowknightme.model.enemy;

import com.badlogic.gdx.physics.box2d.Body;
import ir.Ali.hollowknightme.enums.enemy.EnemyState;
import ir.Ali.hollowknightme.enums.enemy.EnemyType;

public class Crawlid extends GroundEnemy {

    public Crawlid(Body body, float hp, float damage, float speed) {
        super(EnemyType.CRAWLID, body, hp, damage, speed);
        this.currentState = EnemyState.WALK;

        this.normalKnockback.set(5f, 3f);
        this.setCollisionKnockback(3f, 1f);
        this.deathKnockback.set(5f , 10f);
    }

    public Crawlid(Body body, float hp, float damage, float speed, float patrolMinX, float patrolMaxX) {
        super(EnemyType.CRAWLID, body, hp, damage, speed, patrolMinX, patrolMaxX);
        this.currentState = EnemyState.WALK;
    }

    @Override
    protected boolean isValidState(EnemyState state) {
        return switch (state) {
            case WALK, TURN, DEATH_AIR, DEATH_LAND -> true;
            default -> false;
        };
    }
}

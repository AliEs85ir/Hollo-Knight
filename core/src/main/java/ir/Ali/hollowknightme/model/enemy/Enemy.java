package ir.Ali.hollowknightme.model.enemy;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import ir.Ali.hollowknightme.enums.enemy.EnemyState;
import ir.Ali.hollowknightme.enums.enemy.EnemyType;
import ir.Ali.hollowknightme.controller.game.GameProgressManager;
import ir.Ali.hollowknightme.model.interfaces.Damageable;

import java.util.HashMap;
import java.util.Map;

public abstract class Enemy implements Damageable {
    protected final EnemyType type;
    protected final Body body;
    protected final Map<EnemyState, Float> timers = new HashMap<>();

    protected EnemyState currentState = EnemyState.IDLE;
    protected float hp;
    protected float maxHp;
    protected float damage;
    protected float speed;

    protected boolean facingRight = true;
    protected boolean alive = true;
    protected boolean grounded;
    protected boolean locked;

    protected float deathDuration = 1.0f;

    protected Fixture bodyFixture;
    protected Vector2 normalKnockback = new Vector2(3f, 4f);
    protected Vector2 deathKnockback = new Vector2(7f, 10f);

    protected Vector2 collisionKnockback = new Vector2(3f, 1f);

    public Enemy(EnemyType type, Body body, float hp, float damage, float speed) {
        this.type = type;
        this.body = body;
        this.hp = this.maxHp = hp;
        this.damage = damage;
        this.speed = speed;
    }

    @Override
    public void takeDamage(float amount) {
        if (!alive) return;
        hp = Math.max(0, hp - amount);
        if (hp <= 0)
        {
            alive = false;
            GameProgressManager.getInstance().addEnemyKill(this.getType());
        }
    }

    public void setState(EnemyState state) {
        if (isValidState(state)) currentState = state;
    }

    protected abstract boolean isValidState(EnemyState state);

    public void startTimer(EnemyState state, float duration) {
        timers.put(state, duration);
    }

    public void updateTimers(float dt) {
        timers.entrySet().removeIf(entry -> {
            float timeLeft = entry.getValue() - dt;
            if (timeLeft <= 0) return true;
            entry.setValue(timeLeft);
            return false;
        });
    }

    public boolean isTimerActive(EnemyState state) {
        return timers.containsKey(state);
    }

    public void applyMovement(float vx, float vy) {
        if (!locked && alive) body.setLinearVelocity(vx, vy);
    }

    public EnemyType getType() { return type; }
    public EnemyState getCurrentState() { return currentState; }
    public Body getBody() { return body; }
    public float getHp() { return hp; }
    public float getDamage() { return damage; }
    public float getSpeed() { return speed; }
    public boolean isFacingRight() { return facingRight; }
    public boolean isAlive() { return alive; }
    public boolean isGrounded() { return grounded; }
    public boolean isLocked() { return locked; }

    public void setFacingDirection(boolean right) { facingRight = right; }
    public void setGrounded(boolean grounded) { this.grounded = grounded; }
    public void setLocked(boolean locked) { this.locked = locked; }

    public float getDeathDuration() { return deathDuration; }
    public void setDeathDuration(float deathDuration) { this.deathDuration = deathDuration; }

    public float getX() { return body.getPosition().x; }
    public float getY() { return body.getPosition().y; }
    public Vector2 getPosition() { return body.getPosition(); }
    public void setPosition(float x, float y) { body.setTransform(x, y, 0); }

    public Fixture getBodyFixture() { return bodyFixture; }
    public void setBodyFixture(Fixture fixture) { this.bodyFixture = fixture; }
    public Vector2 getNormalKnockback() { return normalKnockback; }
    public Vector2 getDeathKnockback() { return deathKnockback; }

    public Vector2 getCollisionKnockback() {
        return collisionKnockback;
    }

    public void setCollisionKnockback(float x, float y) { this.collisionKnockback.set(x, y); }

    public void setHp(float hp) {
        this.hp = hp;
    }

    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    public float getMaxHp() {
        return maxHp;
    }
}

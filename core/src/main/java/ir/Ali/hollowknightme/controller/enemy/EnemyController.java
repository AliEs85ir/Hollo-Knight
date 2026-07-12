package ir.Ali.hollowknightme.controller.enemy;

import com.badlogic.gdx.math.Vector2;
import ir.Ali.hollowknightme.model.interfaces.Targetable;
import ir.Ali.hollowknightme.model.enemy.Enemy;
import ir.Ali.hollowknightme.enums.enemy.EnemyState;
import ir.Ali.hollowknightme.controller.sound.AudioManager;
import ir.Ali.hollowknightme.enums.sound.SfxType;

public abstract class EnemyController {
    protected final Enemy enemy;
    protected Targetable target;
    protected float stunTimer = 0f;
    protected float knockbackDelayTimer = 0f;

    public EnemyController(Enemy enemy) {
        this.enemy = enemy;
    }

    public abstract void update(float dt);

    public void setTarget(Targetable target) {
        this.target = target;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    protected void updateTimers(float dt) {
        enemy.updateTimers(dt);
        if (stunTimer > 0) {
            stunTimer -= dt;
            if (stunTimer <= 0) {
                stunTimer = 0;
                if (enemy.isAlive()) {
                    enemy.setLocked(false);
                }
            }
        }
        if (knockbackDelayTimer > 0) {
            knockbackDelayTimer -= dt;
        }
    }

    protected void move(float vx, float vy) {
        if (enemy.isAlive() && !enemy.isLocked()) {
            enemy.applyMovement(vx, vy);
        }
    }

    protected void stop() {
        if (enemy.isAlive()) {
            enemy.getBody().setLinearVelocity(0, enemy.getBody().getLinearVelocity().y);
        }
    }

    protected void applyKnockback(float vx, float vy) {
        enemy.getBody().setLinearVelocity(vx, vy);
    }

    protected void setState(EnemyState state) {
        enemy.setState(state);
    }

    protected EnemyState getState() {
        return enemy.getCurrentState();
    }

    protected void startTimer(EnemyState state, float duration) {
        enemy.startTimer(state, duration);
    }

    protected boolean isTimerActive(EnemyState state) {
        return enemy.isTimerActive(state);
    }

    public void playerHitEnemy(float damage, float hitDir) {
        if (!enemy.isAlive()) return;
        enemy.takeDamage(damage);

        if (enemy.getHp() <= 0) {
            Vector2 kb = enemy.getDeathKnockback();
            enemy.setLocked(true);
            enemy.getBody().setLinearVelocity(hitDir * kb.x, kb.y);
            enemy.setGrounded(false);
            knockbackDelayTimer = 0.15f;
            AudioManager.getInstance().playSfx(SfxType.ENEMY_DEATH);
        } else {
            Vector2 kb = enemy.getNormalKnockback();
            enemy.setLocked(true);
            applyKnockback(hitDir * kb.x, kb.y);
            stunTimer = 0.5f;
            knockbackDelayTimer = 0.1f;
            AudioManager.getInstance().playSfx(SfxType.ENEMY_DAMAGE);
        }
    }

    public void enemyHitPlayer(float knightHitDir) {
        if (!enemy.isAlive()) return;
        float enemyKnockbackDir = -knightHitDir;
        enemy.setGrounded(false);
        Vector2 kb = enemy.getCollisionKnockback();
        enemy.getBody().setLinearVelocity(enemyKnockbackDir * kb.x, kb.y);
        knockbackDelayTimer = 0.1f;
    }

    public void enemyHitWall() {}

    public void enemyLanded() {
        if (knockbackDelayTimer > 0) {
            return;
        }
        enemy.setGrounded(true);
        if (getState() == EnemyState.DEATH_AIR) {
            setState(EnemyState.DEATH_LAND);
            startTimer(EnemyState.DEATH_LAND, enemy.getDeathDuration());
        }
    }
}

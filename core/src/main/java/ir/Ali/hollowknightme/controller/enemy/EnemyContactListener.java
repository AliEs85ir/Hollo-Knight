package ir.Ali.hollowknightme.controller.enemy;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import ir.Ali.hollowknightme.enums.fixture.FixtureType;
import ir.Ali.hollowknightme.controller.game.GameRunManager;
import ir.Ali.hollowknightme.model.enemy.CrystalGuardian;
import ir.Ali.hollowknightme.model.enemy.Enemy;
import ir.Ali.hollowknightme.model.knight.Knight;
import ir.Ali.hollowknightme.model.knight.Nail;
import ir.Ali.hollowknightme.model.map.GroundData;
import ir.Ali.hollowknightme.model.map.GroundType;
import ir.Ali.hollowknightme.controller.sound.AudioManager;
import ir.Ali.hollowknightme.enums.sound.SfxType;

public class EnemyContactListener implements ContactListener {
    private final Knight knight;
    private final EnemyControllerManager controllerManager;
    private final GameRunManager gameRunManager;

    public EnemyContactListener(Knight knight, EnemyControllerManager controllerManager , GameRunManager gameRunManager) {
        this.knight = knight;
        this.controllerManager = controllerManager;
        this.gameRunManager = gameRunManager;
    }

    @Override
    public void beginContact(Contact contact) {
        handleContact(contact, contact.getFixtureA(), contact.getFixtureB());
        handleContact(contact, contact.getFixtureB(), contact.getFixtureA());
    }

    private void handleContact(Contact contact, Fixture fixEnemy, Fixture fixOther) {
        Enemy enemy = getEnemy(fixEnemy);
        if (enemy == null) return;
        Object otherData = fixOther.getUserData();
        Object otherBodyData = fixOther.getBody().getUserData();

        if (otherData instanceof Nail || otherData == FixtureType.KNIGHT_WEAPON) {
            if (fixEnemy.getUserData() == FixtureType.ENEMY_WEAPON) {
                return;
            }
            float hitDir = Math.signum(enemy.getX() - knight.getX());
            if (hitDir == 0) hitDir = knight.isFacingRight() ? 1f : -1f;
            controllerManager.onPlayerHitEnemy(enemy, knight.getAttackDamage(), hitDir);
            return;
        }

        if (fixEnemy.getUserData() == FixtureType.ENEMY_WEAPON) {
            if (fixOther.getBody() == knight.getBody() || otherData instanceof Knight || otherBodyData instanceof Knight || otherData == FixtureType.KNIGHT) {
                if (enemy instanceof CrystalGuardian crystalGuardian) {
                    crystalGuardian.getLaser().setHasHit(true);
                }
                float knockbackDir = Math.signum(knight.getX() - enemy.getX());
                if (knockbackDir == 0) knockbackDir = enemy.isFacingRight() ? 1f : -1f;
                knight.takeDamage(enemy.getDamage(), knockbackDir);
                AudioManager.getInstance().playSfx(SfxType.HERO_DAMAGE);

                if (knight.getCurrentHealth() <= 0) {
                    gameRunManager.playerDied();
                    return;
                }
            }
            return;
        }

        Vector2 normal = contact.getWorldManifold().getNormal();
        if (contact.getFixtureA() == fixEnemy) {
            normal.scl(-1f);
        }

        if (!enemy.isAlive()) {
            if (isEnvironment(otherData) || isEnvironment(otherBodyData)) {
                if (normal.y > 0.5f) {
                    controllerManager.onEnemyLanded(enemy);
                } else if (Math.abs(normal.x) > 0.5f) {
                    controllerManager.onEnemyHitWall(enemy);
                }
            }
            return;
        }

        if (fixOther.getBody() == knight.getBody() || otherData instanceof Knight || otherBodyData instanceof Knight || otherData == FixtureType.KNIGHT) {
            float knockbackDir = Math.signum(knight.getX() - enemy.getX());
            if (knockbackDir == 0) knockbackDir = 1f;
            controllerManager.onEnemyHitPlayer(enemy, knockbackDir);
            knight.takeDamage(enemy.getDamage(), knockbackDir);
            AudioManager.getInstance().playSfx(SfxType.HERO_DAMAGE);

            if (knight.getCurrentHealth() <= 0) {
                gameRunManager.playerDied();
            }
        } else if (isEnvironment(otherData) || isEnvironment(otherBodyData)) {
            if (Math.abs(normal.x) > 0.5f) {
                controllerManager.onEnemyHitWall(enemy);
            } else if (normal.y > 0.5f) {
                controllerManager.onEnemyLanded(enemy);
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        Enemy enemyA = getEnemy(fixA);
        Enemy enemyB = getEnemy(fixB);

        if ((enemyA != null && (isSpike(fixB) || isDegradable(fixB)) ||
            (enemyB != null && (isSpike(fixA) || isDegradable(fixA))))) {
            contact.setEnabled(false);
            return;
        }

        if (enemyA != null && !enemyA.isAlive() && fixB.getBody() == knight.getBody()) {
            contact.setEnabled(false);
        }

        if (enemyB != null && !enemyB.isAlive() && fixA.getBody() == knight.getBody()) {
            contact.setEnabled(false);
        }
    }

    private Enemy getEnemy(Fixture fixture) {
        if (fixture == null || fixture.getBody() == null) return null;
        if (fixture.getUserData() == FixtureType.ENEMY_VISION) return null;
        Object bodyData = fixture.getBody().getUserData();
        if (bodyData instanceof Enemy) {
            return (Enemy) bodyData;
        }
        return null;
    }

    private boolean isEnvironment(Object userData) {
        return userData instanceof GroundData ||
            userData == FixtureType.WALL ||
            userData == FixtureType.GROUND ||
            userData == FixtureType.PLATFORM;
    }

    @Override
    public void endContact(Contact contact) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}

    private boolean isSpike(Fixture fixture) {
        if (fixture == null) return false;

        if (fixture.getUserData() == FixtureType.SPIKE) {
            return true;
        }

        Object bodyData = fixture.getBody().getUserData();
        return bodyData instanceof GroundData
            && ((GroundData) bodyData).getType() == GroundType.SPIKE;
    }

    private boolean isDegradable(Fixture fixture) {
        if (fixture == null) return false;

        if (fixture.getUserData() == FixtureType.DEGRADABLE) {
            return true;
        }

        Object bodyData = fixture.getBody().getUserData();
        return bodyData instanceof GroundData
            && ((GroundData) bodyData).getType() == GroundType.DEGRADABLE;
    }
}

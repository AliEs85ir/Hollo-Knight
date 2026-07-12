package ir.Ali.hollowknightme.model.enemy;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import ir.Ali.hollowknightme.enums.fixture.FixtureType;

public class EnemyLaser {
    private final Body body;
    private Fixture fixture;
    private final float width;
    private final float height;
    private final float offsetX;
    private final float offsetY;
    private final float damage;
    private boolean active;
    private boolean hasHit;

    public EnemyLaser(Body body, float width, float height, float offsetX, float offsetY, float damage) {
        this.body = body;
        this.width = width;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.damage = damage;
        this.active = false;
        this.hasHit = false;
    }

    public void setActive(boolean active, boolean facingRight) {
        this.active = active;
        if (this.active) {
            this.hasHit = false;
        }

        if (fixture != null) {
            body.destroyFixture(fixture);
            fixture = null;
        }

        if (this.active) {
            PolygonShape shape = new PolygonShape();
            float direction = facingRight ? 1f : -1f;
            float centerX = (width / 2f + offsetX) * direction;
            shape.setAsBox(width / 2f, height / 2f, new Vector2(centerX, offsetY), 0);
            FixtureDef fdef = new FixtureDef();
            fdef.shape = shape;
            fdef.isSensor = true;
            fixture = body.createFixture(fdef);
            fixture.setUserData(FixtureType.ENEMY_WEAPON);
            shape.dispose();
        }
    }

    public boolean isActive() {
        return active;
    }

    public float getDamage() {
        return damage;
    }

    public float getWidth() {
        return width;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public boolean hasHit() {
        return hasHit;
    }

    public void setHasHit(boolean hasHit) {
        this.hasHit = hasHit;
    }
}

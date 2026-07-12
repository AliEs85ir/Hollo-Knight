package ir.Ali.hollowknightme.model.knight;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import ir.Ali.hollowknightme.enums.knight.KnightState;

public class Nail {
    private final Fixture rightFixture;
    private final Fixture leftFixture;
    private final Fixture upFixture;
    private final Fixture downFixture;
    private boolean active = false;
    private boolean hasHit = false;
    private final float damage = 5f;
    private KnightState attackState = KnightState.SLASH;

    private static final short CATEGORY_WEAPON = 0x0010;
    private static final short MASK_ACTIVE = -1;
    private static final short MASK_INACTIVE = 0;

    public Nail(Body knightBody, float knightWidth, float knightHeight, float ppm) {
        float hWidth = (knightWidth * 3f) / ppm;
        float hHeight = (knightHeight * 1f) / ppm;
        float hOffsetX = (knightWidth * 1.4f) / ppm;

        float vWidth = (knightWidth * 2.5f) / ppm;
        float vHeight = (knightHeight * 2f) / ppm;
        float vOffsetY = (knightHeight * 1.2f) / ppm;

        rightFixture = createFixture(knightBody, hWidth, hHeight, hOffsetX, 0);
        leftFixture = createFixture(knightBody, hWidth, hHeight, -hOffsetX, 0);
        upFixture = createFixture(knightBody, vWidth, vHeight, 0, vOffsetY);
        downFixture = createFixture(knightBody, vWidth, vHeight, 0, -vOffsetY);
    }

    private Fixture createFixture(Body body, float w, float h, float ox, float oy) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(w / 2f, h / 2f, new Vector2(ox, oy), 0);
        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.isSensor = true;
        fDef.filter.categoryBits = CATEGORY_WEAPON;
        fDef.filter.maskBits = MASK_INACTIVE;
        Fixture fixture = body.createFixture(fDef);
        fixture.setUserData(this);
        shape.dispose();
        return fixture;
    }

    public void setActive(boolean active, boolean facingRight, KnightState attackState) {
        if (this.active == active && this.attackState == attackState) return;
        this.active = active;
        this.attackState = attackState;

        Filter activeFilter = new Filter();
        activeFilter.categoryBits = CATEGORY_WEAPON;
        activeFilter.maskBits = active ? MASK_ACTIVE : MASK_INACTIVE;

        Filter inactiveFilter = new Filter();
        inactiveFilter.categoryBits = CATEGORY_WEAPON;
        inactiveFilter.maskBits = MASK_INACTIVE;

        rightFixture.setFilterData(inactiveFilter);
        leftFixture.setFilterData(inactiveFilter);
        upFixture.setFilterData(inactiveFilter);
        downFixture.setFilterData(inactiveFilter);

        if (active) {
            if (attackState == KnightState.UP_SLASH) {
                upFixture.setFilterData(activeFilter);
            } else if (attackState == KnightState.DOWN_SLASH) {
                downFixture.setFilterData(activeFilter);
            } else {
                if (facingRight) {
                    rightFixture.setFilterData(activeFilter);
                } else {
                    leftFixture.setFilterData(activeFilter);
                }
            }
        }
    }

    public boolean isActive() { return active; }
    public boolean isHasHit() { return hasHit; }
    public void setHasHit(boolean hasHit) { this.hasHit = hasHit; }
    public float getDamage() { return damage; }
    public KnightState getAttackState() { return attackState; }
}

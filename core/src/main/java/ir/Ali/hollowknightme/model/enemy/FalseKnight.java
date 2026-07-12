package ir.Ali.hollowknightme.model.enemy;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import ir.Ali.hollowknightme.enums.enemy.EnemyState;
import ir.Ali.hollowknightme.enums.enemy.EnemyType;
import ir.Ali.hollowknightme.enums.fixture.FixtureType;

public class FalseKnight extends GroundEnemy {

    private boolean phaseTwo = false;
    private boolean maceActive = false;
    private boolean powerSlamming = false;
    private Fixture maceSensorRight;
    private Fixture maceSensorLeft;

    private boolean showHitFlash = false;
    private float hitFlashTimer = 0f;
    private static final float HIT_FLASH_DURATION = 0.15f;

    public FalseKnight(Body body, float hp, float damage, float speed) {
        super(EnemyType.FALSE_KNIGHT, body, hp, damage, speed);
        this.normalKnockback.set(5 , 7);
        this.collisionKnockback.set(4,6);
        createMaceSensors();
    }

    private void createMaceSensors() {
        PolygonShape rightShape = new PolygonShape();
        rightShape.setAsBox(0.5f, 1.5f, new Vector2(2.5f, -0.5f), 0f);
        FixtureDef rightDef = new FixtureDef();
        rightDef.shape = rightShape;
        rightDef.isSensor = true;

        maceSensorRight = body.createFixture(rightDef);
        maceSensorRight.setUserData(FixtureType.ENEMY_WEAPON);

        PolygonShape leftShape = new PolygonShape();
        leftShape.setAsBox(0.5f, 1.5f, new Vector2(-2.5f, -0.5f), 0f);
        FixtureDef leftDef = new FixtureDef();
        leftDef.shape = leftShape;
        leftDef.isSensor = true;

        maceSensorLeft = body.createFixture(leftDef);
        maceSensorLeft.setUserData(FixtureType.ENEMY_WEAPON);

        rightShape.dispose();
        leftShape.dispose();
    }

    @Override
    protected boolean isValidState(EnemyState state) {
        return true;
    }

    @Override
    public float getDamage() {
        return powerSlamming ? super.getDamage() * 2f : super.getDamage();
    }

    public boolean isPhaseTwo() {
        return phaseTwo;
    }

    public void setPhaseTwo(boolean phaseTwo) {
        this.phaseTwo = phaseTwo;
    }

    public boolean isMaceActive() {
        return maceActive;
    }

    public void setMaceActive(boolean maceActive) {
        this.maceActive = maceActive;
    }

    public void setPowerSlamming(boolean powerSlamming) {
        this.powerSlamming = powerSlamming;
    }

    public Fixture getActiveMaceSensor() {
        if (!maceActive) return null;
        return facingRight ? maceSensorRight : maceSensorLeft;
    }

    public Fixture getMaceSensorRight() {
        return maceSensorRight;
    }

    public void setMaceSensorRight(Fixture maceSensorRight) {
        this.maceSensorRight = maceSensorRight;
    }

    public Fixture getMaceSensorLeft() {
        return maceSensorLeft;
    }

    public void setMaceSensorLeft(Fixture maceSensorLeft) {
        this.maceSensorLeft = maceSensorLeft;
    }

    public boolean isShowHitFlash() {
        return showHitFlash;
    }

    public void triggerHitFlash() {
        this.showHitFlash = true;
        this.hitFlashTimer = HIT_FLASH_DURATION;
    }

    public void updateHitFlash(float delta) {
        if (showHitFlash) {
            hitFlashTimer -= delta;
            if (hitFlashTimer <= 0) {
                showHitFlash = false;
                hitFlashTimer = 0;
            }
        }
    }
}

package ir.Ali.hollowknightme.model.knight;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import ir.Ali.hollowknightme.enums.knight.KnightState;
import ir.Ali.hollowknightme.controller.map.PhysicsBuilder;
import ir.Ali.hollowknightme.model.interfaces.Targetable;

public class Knight implements Targetable {

    public static final float PPM = 100f;
    private final Body body;
    private final float width;
    private final float height;
    private final Nail nail;

    public final static int maxHealth = 8;
    private int currentHealth = maxHealth;
    private int soul = 80;
    private int maxSoul = 99;

    private boolean isFacingRight = true;
    private KnightState currentKnightState = KnightState.IDLE;
    private boolean isGrounded = false;
    private boolean isOnWall = false;
    private int wallDirection = 0;
    private boolean canDoubleJump = false;
    private boolean isJumpCut = false;
    private boolean isOnCeiling = false;
    private boolean isDashing = false;
    private float jumpTime = 0f;
    private boolean isJumpKeyHeld = false;
    private boolean locked = false;
    private final float attackDamage;
    private boolean focusing = false;

    private float invincibleTimer = 0f;
    private float flashTimer = 0f;
    private float pogoImmunityTimer = 0f;

    private boolean damagedThisFrame = false;

    public Knight(World world, float x, float y, float width, float height) {
        this.width = width;
        this.height = height;

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x / PPM, y / PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        this.body = world.createBody(bodyDef);
        this.body.setFixedRotation(true);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((width / 2f) / PPM, (height / 2f) / PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.4f;
        fixtureDef.filter.categoryBits = PhysicsBuilder.CATEGORY_PLAYER;
        fixtureDef.filter.maskBits = (short) (PhysicsBuilder.CATEGORY_GROUND | PhysicsBuilder.CATEGORY_ENEMY | PhysicsBuilder.CATEGORY_SENSOR);

        this.body.createFixture(fixtureDef);
        shape.dispose();

        this.nail = new Nail(this.body, width, height, PPM);
        this.attackDamage = this.nail.getDamage();
    }

    public void update(float dt) {
        updateEffects(dt);
    }

    public void applyRecoil() {
        body.setLinearVelocity(0, body.getLinearVelocity().y);
        float recoilDir = isFacingRight ? -6f : 6f;
        body.applyLinearImpulse(new Vector2(recoilDir, 0), body.getWorldCenter(), true);
    }

    public boolean isInvincible() {
        return invincibleTimer > 0;
    }

    public void takeDamage(float damage, float knockbackDir) {
        if (invincibleTimer > 0 || currentHealth <= 0) return;
        this.damagedThisFrame = true;
        currentHealth =(int) Math.max(0 , this.currentHealth-damage);
        invincibleTimer = 1.5f;
        locked = true;
        body.setLinearVelocity(0, 0);
        body.applyLinearImpulse(new Vector2(knockbackDir * 3f, 4f), body.getWorldCenter(), true);
    }

    public void takeDamage(float damage, float knockbackDir , float knockbackCoefficient) {
        if (invincibleTimer > 0 || currentHealth <= 0) return;
        this.damagedThisFrame = true;
        currentHealth -= damage;
        invincibleTimer = 1.5f;
        locked = true;

        body.setLinearVelocity(0, 0);
        body.applyLinearImpulse(new Vector2(knockbackDir * knockbackCoefficient * 3f, 4f * knockbackCoefficient),
            body.getWorldCenter(), true);
    }


    public void updateEffects(float dt) {
        if (invincibleTimer > 0) {
            invincibleTimer -= dt;
            flashTimer += dt;
        } else {
            flashTimer = 0f;
        }
    }

    public boolean shouldFlash() {
        return invincibleTimer > 0 && ((int)(flashTimer / 0.1f) % 2 == 0);
    }

    @Override
    public Vector2 getPosition() { return body.getPosition(); }
    @Override
    public Body getBody() { return body; }
    @Override
    public float getX() { return body.getPosition().x; }
    @Override
    public float getY() { return body.getPosition().y; }

    public Nail getNail() { return nail; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public int getCurrentHealth() { return currentHealth; }
    public void setCurrentHealth(int currentHealth) { this.currentHealth = currentHealth; }
    public int getSoul() { return soul; }
    public void setSoul(int soul) { this.soul = soul; }
    public boolean isFacingRight() { return isFacingRight; }
    public void setFacingRight(boolean facingRight) { this.isFacingRight = facingRight; }
    public KnightState getCurrentState() { return currentKnightState; }
    public void setCurrentState(KnightState state) { this.currentKnightState = state; }
    public boolean isGrounded() { return isGrounded; }
    public void setGrounded(boolean grounded) { this.isGrounded = grounded; }
    public boolean isOnWall() { return isOnWall; }
    public void setOnWall(boolean onWall) { this.isOnWall = onWall; }
    public int getWallDirection() { return wallDirection; }
    public void setWallDirection(int wallDirection) { this.wallDirection = wallDirection; }
    public boolean isCanDoubleJump() { return canDoubleJump; }
    public void setCanDoubleJump(boolean canDoubleJump) { this.canDoubleJump = canDoubleJump; }
    public boolean isDashing() { return isDashing; }
    public void setDashing(boolean dashing) { this.isDashing = dashing; }
    public boolean isLocked() { return locked; }
    public void setLocked(boolean locked) { this.locked = locked; }
    public float getAttackDamage() { return attackDamage; }
    public void setJumpCut(boolean jumpCut) { this.isJumpCut = jumpCut; }
    public void setOnCeiling(boolean onCeiling) { this.isOnCeiling = onCeiling; }
    public void setJumpTime(float jumpTime) { this.jumpTime = jumpTime; }
    public void setJumpKeyHeld(boolean jumpKeyHeld) { this.isJumpKeyHeld = jumpKeyHeld; }
    public void setInvincibleTimer(float invincibleTimer) { this.invincibleTimer = invincibleTimer; }
    public float getInvincibleTimer() { return invincibleTimer; }
    public void setPogoImmunityTimer(float time) {
        this.pogoImmunityTimer = time;
    }
    public void updatePogoImmunityTimer(float time){
        this.pogoImmunityTimer -= time;
    }
    public boolean isImmuneToSpikes() {
        return pogoImmunityTimer > 0;
    }
    public boolean isFocusing() {
        return focusing;
    }
    public void setFocusing(boolean focusing) {
        this.focusing = focusing;
    }public int getMaxHealth() {
        return maxHealth;
    }
    public void addSoul(int amount) {
        this.soul = Math.min(maxSoul, this.soul + amount);
    }
    public boolean isDamagedThisFrame() {
        return damagedThisFrame;
    }public void clearDamageFlag() {
        damagedThisFrame = false;
    }
}

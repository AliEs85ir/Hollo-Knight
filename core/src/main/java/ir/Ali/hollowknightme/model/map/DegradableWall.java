package ir.Ali.hollowknightme.model.map;

import com.badlogic.gdx.physics.box2d.Body;
import ir.Ali.hollowknightme.model.interfaces.Damageable;

public class DegradableWall implements Damageable {
    private final GroundData groundData;
    private final Body body;
    private int hp;
    private DestroyStage stage;
    private boolean pendingDestroy;

    public DegradableWall(GroundData groundData, Body body) {
        this.groundData = groundData;
        this.body = body;
        this.hp = 3;
        this.stage = DestroyStage.NORMAL;
        this.pendingDestroy = false;
    }

    @Override
    public void takeDamage(float damage) {
        if (hp > 0) {
            hp--;
            updateStage();
        }
    }

    private void updateStage() {
        if (hp == 2) {
            stage = DestroyStage.FIRST_DAMAGE;
        } else if (hp == 1) {
            stage = DestroyStage.SECOND_DAMAGE;
        } else if (hp <= 0) {
            stage = DestroyStage.DESTROYED;
            pendingDestroy = true;
        }
    }

    public GroundData getGroundData() {
        return groundData;
    }

    public Body getBody() {
        return body;
    }

    public int getHp() {
        return hp;
    }

    public DestroyStage getStage() {
        return stage;
    }

    public boolean isPendingDestroy() {
        return pendingDestroy;
    }


}

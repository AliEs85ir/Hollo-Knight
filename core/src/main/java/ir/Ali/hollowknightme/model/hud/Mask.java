package ir.Ali.hollowknightme.model.hud;

import ir.Ali.hollowknightme.enums.hud.MaskState;

public class Mask {
    private MaskState currentState;
    private float stateTime;
    private final float x;
    private final float y;

    public Mask(float x, float y) {
        this.x = x;
        this.y = y;
        this.currentState = MaskState.CREATING;
        this.stateTime = 0f;
    }

    public void update(float dt) {
        stateTime += dt;
    }

    public void setState(MaskState state) {
        if (this.currentState != state) {
            this.currentState = state;
            this.stateTime = 0f;
        }
    }

    public MaskState getCurrentState() {
        return currentState;
    }

    public float getStateTime() {
        return stateTime;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}

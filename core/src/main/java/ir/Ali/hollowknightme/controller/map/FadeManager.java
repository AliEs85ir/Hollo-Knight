package ir.Ali.hollowknightme.controller.map;

public class FadeManager {

    public enum State {
        NONE,
        FADE_OUT,
        FADE_IN
    }

    private State state = State.NONE;
    private float alpha = 0;
    private final float speed = 1.7f;
    private Runnable middleAction;

    public void start(Runnable middleAction) {
        this.middleAction = middleAction;
        this.alpha = 0;
        this.state = State.FADE_OUT;
    }

    public void update(float dt) {
        switch (state) {
            case FADE_OUT -> {
                alpha += speed * dt;
                if (alpha >= 1) {
                    alpha = 1;
                    if (middleAction != null) {
                        middleAction.run();
                        middleAction = null;
                    }
                    state = State.FADE_IN;
                }
            }
            case FADE_IN -> {
                alpha -= speed * dt;
                if (alpha <= 0) {
                    alpha = 0;
                    state = State.NONE;
                }
            }
        }
    }

    public float getAlpha() {
        return alpha;
    }

    public boolean isBusy() {
        return state != State.NONE;
    }
}

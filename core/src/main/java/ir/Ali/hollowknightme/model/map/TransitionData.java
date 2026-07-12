package ir.Ali.hollowknightme.model.map;

import com.badlogic.gdx.math.Rectangle;

public class TransitionData {
    private final Rectangle rectangle;
    private final TransitionType type;

    public TransitionData(Rectangle rectangle, TransitionType type) {
        this.rectangle = rectangle;
        this.type = type;
    }

    public Rectangle getRectangle() { return rectangle; }
    public TransitionType getType() { return type; }

    public boolean isInput() {
        return type == TransitionType.INPUT;
    }

    public boolean isOutput() {
        return type == TransitionType.OUTPUT;
    }
}

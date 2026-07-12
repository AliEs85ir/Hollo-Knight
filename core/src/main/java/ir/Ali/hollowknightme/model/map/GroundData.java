package ir.Ali.hollowknightme.model.map;

import com.badlogic.gdx.math.Rectangle;

public class GroundData {
    private final Rectangle rectangle;
    private final GroundType type;
    private final boolean activeLeft;
    private final boolean activeRight;
    private final boolean activeTop;
    private final boolean activeBottom;

    public GroundData(Rectangle rectangle, GroundType type, boolean activeLeft, boolean activeRight, boolean activeTop, boolean activeBottom) {
        this.rectangle = rectangle;
        this.type = type;
        this.activeLeft = activeLeft;
        this.activeRight = activeRight;
        this.activeTop = activeTop;
        this.activeBottom = activeBottom;
    }

    public Rectangle getRectangle() { return rectangle; }
    public GroundType getType() { return type; }
    public boolean isActiveLeft() { return activeLeft; }
    public boolean isActiveRight() { return activeRight; }
    public boolean isActiveTop() { return activeTop; }
    public boolean isActiveBottom() { return activeBottom; }
}

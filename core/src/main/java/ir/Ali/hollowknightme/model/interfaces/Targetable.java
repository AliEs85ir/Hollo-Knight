package ir.Ali.hollowknightme.model.interfaces;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public interface Targetable {
    Vector2 getPosition();
    Body getBody();
    float getX();
    float getY();
}

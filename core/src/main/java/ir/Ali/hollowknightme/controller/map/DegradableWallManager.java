package ir.Ali.hollowknightme.controller.map;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import ir.Ali.hollowknightme.model.map.DegradableWall;
import ir.Ali.hollowknightme.model.map.GroundData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DegradableWallManager {
    private final List<DegradableWall> walls;
    private final World world;

    public DegradableWallManager(World world) {
        this.world = world;
        this.walls = new ArrayList<>();
    }

    public void registerWall(GroundData groundData, Body body) {
        walls.add(new DegradableWall(groundData, body));
    }

    public void hit(Body body) {
        if (body == null || body.getUserData() == null) return;
        Object hitUserData = body.getUserData();

        for (DegradableWall wall : walls) {
            if (wall.getGroundData().equals(hitUserData)) {
                wall.takeDamage(1f);
                break;
            }
        }
    }

    public void update() {
        Iterator<DegradableWall> iterator = walls.iterator();
        while (iterator.hasNext()) {
            DegradableWall wall = iterator.next();
            if (wall.isPendingDestroy()) {
                world.destroyBody(wall.getBody());
                iterator.remove();
            }
        }
    }
    public void clear() {
        this.walls.clear();
    }
    public List<DegradableWall> getWalls() {
        return walls;
    }
}

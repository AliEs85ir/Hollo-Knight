package ir.Ali.hollowknightme.controller.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import ir.Ali.hollowknightme.enums.fixture.FixtureType;
import ir.Ali.hollowknightme.model.map.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PhysicsBuilder {

    public static final short CATEGORY_GROUND = 0x0001;
    public static final short CATEGORY_PLAYER = 0x0002;
    public static final short CATEGORY_ENEMY = 0x0004;
    public static final short CATEGORY_SENSOR = 0x0008;

    private PhysicsBuilder() {}

    public static List<Body> buildPhysics(World world, MapData mapData, DegradableWallManager wallManager) {
        if (world == null || mapData == null) {
            return Collections.emptyList();
        }

        List<Body> bodies = new ArrayList<>();
        mapData.getGrounds().forEach(g -> bodies.add(createGroundBody(world, g, wallManager)));
        mapData.getTransitions().forEach(t -> bodies.add(createTransitionBody(world, t)));

        return bodies;
    }

    private static Body createGroundBody(World world, GroundData ground, DegradableWallManager wallManager) {
        Body body = world.createBody(createBodyDef(ground.getRectangle()));
        body.setUserData(ground);

        PolygonShape shape = createShape(ground.getRectangle());
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.5f;

        fixtureDef.filter.categoryBits = CATEGORY_GROUND;
        fixtureDef.filter.maskBits = (short) -1;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(toFixtureType(ground.getType()));

        shape.dispose();

        if (ground.getType() == GroundType.DEGRADABLE) {
            if (wallManager != null) {
                wallManager.registerWall(ground, body);
            } else {
                Gdx.app.error("WALL_DEBUG", "Degradable wall found but wallManager was NULL!");
            }
        }

        return body;
    }

    private static Body createTransitionBody(World world, TransitionData transition) {
        Body body = world.createBody(createBodyDef(transition.getRectangle()));
        body.setUserData(transition);

        PolygonShape shape = createShape(transition.getRectangle());
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;

        fixtureDef.filter.categoryBits = CATEGORY_SENSOR;
        fixtureDef.filter.maskBits = CATEGORY_PLAYER;

        body.createFixture(fixtureDef).setUserData(FixtureType.TRANSITION);

        shape.dispose();
        return body;
    }

    private static BodyDef createBodyDef(Rectangle rect) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((rect.x + rect.width / 2f) / MapManager.PPM, (rect.y + rect.height / 2f) / MapManager.PPM);
        return bodyDef;
    }

    private static PolygonShape createShape(Rectangle rect) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(rect.width / 2f / MapManager.PPM, rect.height / 2f / MapManager.PPM);
        return shape;
    }

    private static FixtureType toFixtureType(GroundType type) {
        return switch (type) {
            case WALL -> FixtureType.WALL;
            case SPIKE -> FixtureType.SPIKE;
            case DEGRADABLE -> FixtureType.DEGRADABLE;
            default -> FixtureType.GROUND;
        };
    }
}

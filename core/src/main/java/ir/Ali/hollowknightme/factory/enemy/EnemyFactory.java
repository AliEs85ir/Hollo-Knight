package ir.Ali.hollowknightme.factory.enemy;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import ir.Ali.hollowknightme.controller.enemy.*;
import ir.Ali.hollowknightme.enums.fixture.FixtureType;
import ir.Ali.hollowknightme.controller.enemy.EnemyManager;
import ir.Ali.hollowknightme.controller.map.PhysicsBuilder;
import ir.Ali.hollowknightme.model.enemy.*;

public class EnemyFactory {
    private final World world;
    private final EnemyControllerManager controllerManager;
    private final EnemyManager enemyManager;

    public EnemyFactory(World world, EnemyControllerManager controllerManager, EnemyManager enemyManager) {
        this.world = world;
        this.controllerManager = controllerManager;
        this.enemyManager = enemyManager;
    }

    private Body createDynamicBody(float x, float y, float width, float height) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = true;

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((width / 2f) - 0.05f, (height / 2f) - 0.05f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.0f;

        fixtureDef.filter.categoryBits = PhysicsBuilder.CATEGORY_ENEMY;
        fixtureDef.filter.maskBits = (short) (0xFFFF & ~PhysicsBuilder.CATEGORY_ENEMY);

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(FixtureType.ENEMY_BODY);

        shape.dispose();

        return body;
    }

    private void setupAndRegisterEnemy(Enemy enemy, Body body, EnemyController controller) {
        enemy.setBodyFixture(body.getFixtureList().first());
        body.setUserData(enemy);
        controllerManager.register(controller);
        enemyManager.registerEnemyInstance(enemy, controller);
    }

    public Crawlid createCrawlid(float x, float y, float width, float height, float hp, float damage, float speed) {
        Body body = createDynamicBody(x, y, width, height);
        Crawlid crawlid = new Crawlid(body, hp, damage, speed);
        CrawlidController controller = new CrawlidController(crawlid, world);
        setupAndRegisterEnemy(crawlid, body, controller);
        return crawlid;
    }

    public HuskHornhead createHuskHornhead(float x, float y, float width, float height, float hp, float damage, float speed, float lungeSpeed, float restDuration, float attackCooldown, float attackAnticipateDuration, Rectangle visionRange) {
        Body body = createDynamicBody(x, y, width, height);
        HuskHornhead husk = new HuskHornhead(body, hp, damage, speed, lungeSpeed, restDuration, attackCooldown, attackAnticipateDuration, visionRange);
        HuskHornheadController controller = new HuskHornheadController(husk, world);
        setupAndRegisterEnemy(husk, body, controller);
        return husk;
    }

    public CrystalGuardian createCrystalGuardian(float x, float y, float width, float height, float hp, float damage, float speed, Rectangle visionRange, float laserRange, float enrageDuration , boolean facingRight) {
        Body body = createDynamicBody(x, y, width, height);
        CrystalGuardian guardian = new CrystalGuardian(body, hp, damage, speed, visionRange, laserRange,
            enrageDuration , facingRight);
        CrystalGuardianController controller = new CrystalGuardianController(guardian, world);
        setupAndRegisterEnemy(guardian, body, controller);
        return guardian;
    }

    public WingedSentry createWingedSentry(float x, float y, float width, float height, float hp, float damage, float returnSpeed, float chargeSpeed, Rectangle visionRange, float anticDuration, float chargeDuration, boolean facingRight) {
        Body body = createDynamicBody(x, y, width, height);
        WingedSentry sentry = new WingedSentry(body, hp, damage, returnSpeed, chargeSpeed, visionRange, anticDuration, chargeDuration, facingRight);
        WingedSentryController controller = new WingedSentryController(sentry);
        setupAndRegisterEnemy(sentry, body, controller);
        return sentry;
    }

    public FalseKnight createFalseKnight(float x, float y, float width, float height, float hp, float damage, float speed) {
        Body body = createDynamicBody(x, y, width, height);
        FalseKnight falseKnight = new FalseKnight(body, hp, damage, speed);
        FalseKnightController controller = new FalseKnightController(falseKnight, world);
        controller.setTarget(enemyManager.getCurrentTarget());
        setupAndRegisterEnemy(falseKnight, body, controller);
        return falseKnight;
    }
}

package ir.Ali.hollowknightme.controller.enemy;

import com.badlogic.gdx.physics.box2d.World;
import ir.Ali.hollowknightme.model.enemy.Crawlid;

public class CrawlidController extends GroundEnemyController {

    public CrawlidController(Crawlid enemy, World world) {
        super(enemy, world);
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        if (!groundEnemy.isAlive() || groundEnemy.isLocked()) {
            return;
        }

        updatePatrol();
    }

    @Override
    public void playerHitEnemy(float damage, float hitDir) {
        super.playerHitEnemy(damage, hitDir);
    }


}

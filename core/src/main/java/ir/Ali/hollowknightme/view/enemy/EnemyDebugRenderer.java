package ir.Ali.hollowknightme.view.enemy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import ir.Ali.hollowknightme.model.enemy.Enemy;
import ir.Ali.hollowknightme.model.enemy.Crawlid;
import ir.Ali.hollowknightme.model.enemy.HuskHornhead;
import ir.Ali.hollowknightme.model.enemy.CrystalGuardian;

public class EnemyDebugRenderer {
    private final ShapeRenderer shapeRenderer;

    public EnemyDebugRenderer(ShapeRenderer shapeRenderer) {
        this.shapeRenderer = shapeRenderer;
    }

    public void render(Enemy enemy) {
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.circle(enemy.getX(), enemy.getY(), 0.1f, 10);

        if (enemy instanceof Crawlid ge) {
            shapeRenderer.line(ge.getPatrolMinX(), enemy.getY() - 0.5f, ge.getPatrolMaxX(), enemy.getY() - 0.5f);
            shapeRenderer.line(ge.getPatrolMinX(), enemy.getY() - 0.6f, ge.getPatrolMinX(), enemy.getY() - 0.4f);
            shapeRenderer.line(ge.getPatrolMaxX(), enemy.getY() - 0.6f, ge.getPatrolMaxX(), enemy.getY() - 0.4f);
        }

        if (enemy instanceof HuskHornhead husk) {
            shapeRenderer.line(husk.getPatrolMinX(), enemy.getY() - 0.5f, husk.getPatrolMaxX(), enemy.getY() - 0.5f);
            shapeRenderer.line(husk.getPatrolMinX(), enemy.getY() - 0.6f, husk.getPatrolMinX(), enemy.getY() - 0.4f);
            shapeRenderer.line(husk.getPatrolMaxX(), enemy.getY() - 0.6f, husk.getPatrolMaxX(), enemy.getY() - 0.4f);
        }

        if (enemy instanceof CrystalGuardian guardian) {
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.circle(guardian.getGuardPosition().x, guardian.getGuardPosition().y, 0.15f, 8);
        }

        shapeRenderer.setColor(Color.CYAN);
        float dirX = enemy.isFacingRight() ? 1f : -1f;
        shapeRenderer.line(enemy.getX(), enemy.getY(), enemy.getX() + (dirX * 0.8f), enemy.getY());
    }
}

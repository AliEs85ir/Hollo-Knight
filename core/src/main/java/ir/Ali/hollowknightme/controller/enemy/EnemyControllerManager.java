package ir.Ali.hollowknightme.controller.enemy;

import ir.Ali.hollowknightme.model.interfaces.Targetable;
import ir.Ali.hollowknightme.model.enemy.Enemy;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EnemyControllerManager {
    private final Map<Enemy, EnemyController> controllers = new HashMap<>();

    public void register(EnemyController controller) {
        if (controller != null) controllers.put(controller.getEnemy(), controller);
    }

    public void unregister(Enemy enemy) {
        if (enemy != null) controllers.remove(enemy);
    }

    public void clear() {
        controllers.clear();
    }

    public Collection<EnemyController> getControllers() {
        return controllers.values();
    }

    public void update(float dt) {
        controllers.values().forEach(c -> c.update(dt));
    }

    public void setTarget(Targetable target) {
        controllers.values().forEach(c -> c.setTarget(target));
    }

    public void onEnemyHitPlayer(Enemy enemy, float knightHitDir) {
        getController(enemy).ifPresent(c -> c.enemyHitPlayer(knightHitDir));
    }

    public void onPlayerHitEnemy(Enemy enemy, float damage, float hitDir) {
        getController(enemy).ifPresent(c -> c.playerHitEnemy(damage, hitDir));
    }

    public void onEnemyHitWall(Enemy enemy) {
        getController(enemy).ifPresent(EnemyController::enemyHitWall);
    }

    public void onEnemyLanded(Enemy enemy) {
        getController(enemy).ifPresent(EnemyController::enemyLanded);
    }

    public void removeDeadEnemies() {
        controllers.entrySet().removeIf(e -> !e.getKey().isAlive());
    }

    private Optional<EnemyController> getController(Enemy enemy) {
        return Optional.ofNullable(controllers.get(enemy));
    }
}

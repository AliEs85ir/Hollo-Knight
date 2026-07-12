package ir.Ali.hollowknightme.controller.enemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import ir.Ali.hollowknightme.model.interfaces.Targetable;
import ir.Ali.hollowknightme.model.enemy.Enemy;
import ir.Ali.hollowknightme.enums.enemy.EnemyState;
import ir.Ali.hollowknightme.view.enemy.EnemyAnimationLibrary;
import ir.Ali.hollowknightme.view.enemy.EnemyAnimator;
import ir.Ali.hollowknightme.view.enemy.EnemyRenderer;

public class EnemyManager {
    private final Array<EnemyInstance> enemies;
    private final World world;
    private final EnemyControllerManager controllerManager;
    private final EnemyAnimationLibrary animationLibrary;
    private Targetable currentTarget;
    private boolean paused;

    public EnemyManager(World world, EnemyControllerManager controllerManager, EnemyAnimationLibrary animationLibrary) {
        this.world = world;
        this.controllerManager = controllerManager;
        this.animationLibrary = animationLibrary;
        this.enemies = new Array<>();
        this.paused = false;
    }

    public void registerEnemyInstance(Enemy model, EnemyController controller) {
        EnemyAnimator animator = new EnemyAnimator(model.getType(), animationLibrary);
        EnemyRenderer renderer = new EnemyRenderer(animator);

        float deathLandDuration = animator.getAnimationDuration(EnemyState.DEATH_LAND);
        model.setDeathDuration(deathLandDuration);

        EnemyInstance instance = new EnemyInstance(model, controller, animator, renderer);
        enemies.add(instance);

        if (currentTarget != null) {
            controller.setTarget(currentTarget);
        }
    }

    public void setTarget(Targetable target) {
        this.currentTarget = target;
        controllerManager.setTarget(target);
    }

    public void update(float deltaTime) {
        if (paused) return;

        controllerManager.update(deltaTime);

        for (int i = enemies.size - 1; i >= 0; i--) {
            EnemyInstance instance = enemies.get(i);
            instance.update(deltaTime);

            Enemy model = instance.getModel();
            if (!model.isAlive()) {
                EnemyState state = model.getCurrentState();
                if (state == EnemyState.DEATH_LAND && !model.isTimerActive(EnemyState.DEATH_LAND)) {
                    if (!instance.isCorpse()) {
                        instance.setCorpse(true);
                        if (model.getBody() != null) {
                            model.getBody().setActive(false);
                        }
                    }
                }
            }

            if (instance.isMarkedForRemoval()) {
                removeEnemyAtIndex(i);
            }
        }
    }

    private void removeEnemyAtIndex(int index) {
        EnemyInstance instance = enemies.get(index);
        Enemy model = instance.getModel();

        controllerManager.unregister(model);

        if (model.getBody() != null) {
            world.destroyBody(model.getBody());
        }

        enemies.removeIndex(index);
    }

    public void render(SpriteBatch batch, float deltaTime) {
        for (int i = 0; i < enemies.size; i++) {
            enemies.get(i).render(batch, deltaTime);
        }
    }

    public void clear() {
        for (int i = 0; i < enemies.size; i++) {
            Enemy model = enemies.get(i).getModel();
            if (model.getBody() != null) {
                world.destroyBody(model.getBody());
            }
        }
        enemies.clear();
        controllerManager.clear();
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }

    public Array<Enemy> getLivingEnemies() {
        Array<Enemy> living = new Array<>();
        for (int i = 0; i < enemies.size; i++) {
            Enemy model = enemies.get(i).getModel();
            if (model.isAlive()) {
                living.add(model);
            }
        }
        return living;
    }

    public Array<Enemy> getAllEnemies() {
        Array<Enemy> all = new Array<>();
        for (int i = 0; i < enemies.size; i++) {
            all.add(enemies.get(i).getModel());
        }
        return all;
    }

    public Enemy getNearestEnemy(Vector2 position) {
        Enemy nearest = null;
        float minDist = Float.MAX_VALUE;
        for (int i = 0; i < enemies.size; i++) {
            Enemy enemy = enemies.get(i).getModel();
            if (!enemy.isAlive()) continue;
            float dist = enemy.getPosition().dst(position);
            if (dist < minDist) {
                minDist = dist;
                nearest = enemy;
            }
        }
        return nearest;
    }

    public Array<Enemy> getEnemiesInRadius(Vector2 position, float radius) {
        Array<Enemy> result = new Array<>();
        float radiusSq = radius * radius;
        for (int i = 0; i < enemies.size; i++) {
            Enemy enemy = enemies.get(i).getModel();
            if (!enemy.isAlive()) continue;
            if (enemy.getPosition().dst2(position) < radiusSq) {
                result.add(enemy);
            }
        }
        return result;
    }

    public int getAliveCount() {
        int count = 0;
        for (int i = 0; i < enemies.size; i++) {
            if (enemies.get(i).getModel().isAlive()) count++;
        }
        return count;
    }

    public int getTotalCount() {
        return enemies.size;
    }

    public boolean isPaused() {
        return paused;
    }

    public Array<EnemyInstance> getInstances() {
        return enemies;
    }

    public Array<EnemyInstance> getEnemies() {
        return enemies;
    }

    public World getWorld() {
        return world;
    }

    public EnemyControllerManager getControllerManager() {
        return controllerManager;
    }

    public EnemyAnimationLibrary getAnimationLibrary() {
        return animationLibrary;
    }

    public Targetable getCurrentTarget() {
        return currentTarget;
    }
}

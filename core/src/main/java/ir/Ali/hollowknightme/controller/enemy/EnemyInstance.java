package ir.Ali.hollowknightme.controller.enemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ir.Ali.hollowknightme.model.enemy.Enemy;
import ir.Ali.hollowknightme.view.enemy.EnemyAnimator;
import ir.Ali.hollowknightme.view.enemy.EnemyRenderer;

public class EnemyInstance {
    private final Enemy model;
    private final EnemyController controller;
    private final EnemyAnimator animator;
    private final EnemyRenderer renderer;
    private boolean markedForRemoval;
    private boolean corpse;

    public EnemyInstance(Enemy model, EnemyController controller, EnemyAnimator animator, EnemyRenderer renderer) {
        this.model = model;
        this.controller = controller;
        this.animator = animator;
        this.renderer = renderer;
        this.markedForRemoval = false;
        this.corpse = false;
    }

    public void update(float deltaTime) {
        if (!corpse) {
            controller.update(deltaTime);
        }
    }

    public void render(SpriteBatch batch, float deltaTime) {
        renderer.render(batch, model, deltaTime);
    }

    public boolean isMarkedForRemoval() {
        return markedForRemoval;
    }

    public void markForRemoval() {
        this.markedForRemoval = true;
    }

    public Enemy getModel() {
        return model;
    }

    public EnemyController getController() {
        return controller;
    }

    public EnemyAnimator getAnimator() {
        return animator;
    }

    public EnemyRenderer getRenderer() {
        return renderer;
    }

    public boolean isCorpse() {
        return corpse;
    }

    public void setCorpse(boolean corpse) {
        this.corpse = corpse;
    }
}

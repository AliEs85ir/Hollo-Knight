package ir.Ali.hollowknightme.view.knight;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ir.Ali.hollowknightme.model.knight.Knight;

public class KnightRenderer {

    private final Knight knight;

    private final KnightAnimator knightAnimator;
    private final NailAnimator nailAnimator;
    private final KnightLightRenderer lightRenderer;

    public KnightRenderer(Knight knight) {
        this.knight = knight;

        this.knightAnimator = new KnightAnimator(knight);
        this.nailAnimator = new NailAnimator();
        this.lightRenderer = new KnightLightRenderer(knight);
    }

    public void update(float delta) {
        lightRenderer.update(delta);
    }

    public void render(SpriteBatch batch, float delta) {
        renderLight(batch);
        renderKnight(batch, delta);
        renderNail(batch, delta);
    }

    private void renderLight(SpriteBatch batch) {
        lightRenderer.render(batch);
    }

    private void renderKnight(SpriteBatch batch, float delta) {

        TextureRegion frame =
            knightAnimator.getFrame(delta, knight.getCurrentState());

        float width = frame.getRegionWidth() / 100f;
        float height = frame.getRegionHeight() / 100f;

        if (knight.isInvincible()) {

            if (knight.shouldFlash()) {
                batch.setColor(1f,1f,1f,0.5f);
            }
            else {
                batch.setColor(0.7f,0.7f,0.8f,1f);
            }

        } else {

            batch.setColor(1f,1f,1f,1f);

        }

        batch.draw(
            frame,
            knight.getBody().getPosition().x - width / 2f,
            knight.getBody().getPosition().y - height / 2f,
            width,
            height
        );

        batch.setColor(1f,1f,1f,1f);
    }

    private void renderNail(SpriteBatch batch, float delta) {

        boolean attacking =
            knight.getCurrentState().toString().contains("SLASH") ;

        TextureRegion frame =
            nailAnimator.getFrame(delta, attacking, knight.isFacingRight());

        if (frame == null) {
            return;
        }

        float width = frame.getRegionWidth() / 200f;
        float height = frame.getRegionHeight() / 200f;

        float offsetX =
            knight.isFacingRight() ? 0.3f : -0.3f;

        batch.draw(
            frame,
            knight.getBody().getPosition().x - width / 2f + offsetX,
            knight.getBody().getPosition().y - height / 2f,
            width,
            height
        );
    }

    public void dispose() {
        knightAnimator.dispose();
        nailAnimator.dispose();
        lightRenderer.dispose();
    }

}

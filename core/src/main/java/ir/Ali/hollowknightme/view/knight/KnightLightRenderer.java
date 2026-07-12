package ir.Ali.hollowknightme.view.knight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import ir.Ali.hollowknightme.model.knight.Knight;

public class KnightLightRenderer {

    private static final float Y_OFFSET = 0.20f;
    private static final float INNER_RADIUS = 2.4f;
    private static final float OUTER_RADIUS = 4.8f;

    private static final float MIN_ALPHA = 0.40f;
    private static final float MAX_ALPHA = 0.50f;
    private static final float MIN_SCALE = 1.35f;
    private static final float MAX_SCALE = 1.45f;

    private static final float MIN_RANDOM_INTERVAL = 0.10f;
    private static final float MAX_RANDOM_INTERVAL = 0.20f;
    private static final float LERP_SPEED = 6f;
    private static final float OUTER_ALPHA_MULTIPLIER = 0.45f;

    private final Texture lightTexture;
    private final Knight knight;

    private float alpha = (MIN_ALPHA + MAX_ALPHA) / 2f;
    private float targetAlpha = alpha;
    private float scale = (MIN_SCALE + MAX_SCALE) / 2f;
    private float targetScale = scale;
    private float timer = 0f;
    private float nextRandomTime = (MIN_RANDOM_INTERVAL + MAX_RANDOM_INTERVAL) / 2f;

    public KnightLightRenderer(Knight knight) {
        this.knight = knight;
        this.lightTexture = new Texture(Gdx.files.internal("white_light.png"));
    }

    public void update(float dt) {
        timer += dt;
        if (timer >= nextRandomTime) {
            timer = 0f;
            nextRandomTime = MathUtils.random(MIN_RANDOM_INTERVAL, MAX_RANDOM_INTERVAL);
            targetAlpha = MathUtils.random(MIN_ALPHA, MAX_ALPHA);
            targetScale = MathUtils.random(MIN_SCALE, MAX_SCALE);
        }
        alpha += (targetAlpha - alpha) * dt * LERP_SPEED;
        scale += (targetScale - scale) * dt * LERP_SPEED;
    }

    public void render(SpriteBatch batch) {
        float baseX = knight.getBody().getPosition().x;
        float baseY = knight.getBody().getPosition().y + Y_OFFSET;
        batch.flush();
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        drawLight(batch, baseX, baseY, OUTER_RADIUS * scale, alpha * OUTER_ALPHA_MULTIPLIER);
        drawLight(batch, baseX, baseY, INNER_RADIUS * scale, alpha);
        batch.flush();
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    private void drawLight(SpriteBatch batch, float cx, float cy, float size, float a) {
        batch.setColor(1f, 1f, 1f, a);
        batch.draw(lightTexture, cx - size / 2f, cy - size / 2f, size, size);
        batch.setColor(1f, 1f, 1f, 1f);
    }

    public void dispose() {
        lightTexture.dispose();
    }
}

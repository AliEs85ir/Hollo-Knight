package ir.Ali.hollowknightme.controller.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

public class CameraShakeManager {
    private static CameraShakeManager instance;
    private float shakeTimer;
    private float shakeDuration;
    private float shakeIntensity;

    private CameraShakeManager() {
    }

    public static CameraShakeManager getInstance() {
        if (instance == null) {
            instance = new CameraShakeManager();
        }
        return instance;
    }

    public void shake(float intensity, float duration) {
        this.shakeIntensity = intensity;
        this.shakeDuration = duration;
        this.shakeTimer = duration;
    }

    public void update(float dt, OrthographicCamera camera, float targetX, float targetY) {
        if (shakeTimer > 0) {
            shakeTimer -= dt;
            float currentIntensity = shakeIntensity * (shakeTimer / shakeDuration);
            float offsetX = MathUtils.random(-currentIntensity, currentIntensity);
            float offsetY = MathUtils.random(-currentIntensity, currentIntensity);
            camera.position.set(targetX + offsetX, targetY + offsetY, 0);
        } else {
            camera.position.set(targetX, targetY, 0);
        }
    }
}

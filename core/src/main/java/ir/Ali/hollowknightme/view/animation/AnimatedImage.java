package ir.Ali.hollowknightme.view.animation;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
public class AnimatedImage extends Image {
    private final Animation<TextureRegion> animation;
    private float stateTime = 0f;
    public AnimatedImage(Animation<TextureRegion> animation) {
        super(animation.getKeyFrame(0));
        this.animation = animation;
    }
    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
        if (!animation.isAnimationFinished(stateTime)) {
            setDrawable(new TextureRegionDrawable(animation.getKeyFrame(stateTime)));
        } else {
            setDrawable(new TextureRegionDrawable(animation.getKeyFrame(animation.getAnimationDuration())));
        }
    }
}

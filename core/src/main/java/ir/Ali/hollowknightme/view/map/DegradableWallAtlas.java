package ir.Ali.hollowknightme.view.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import ir.Ali.hollowknightme.model.map.DestroyStage;

public class DegradableWallAtlas implements Disposable  {
    private final TextureAtlas atlas;
    private final TextureRegion normalRegion;
    private final TextureRegion firstDamageRegion;
    private final TextureRegion secondDamageRegion;

    public DegradableWallAtlas() {
        String atlasPath = "animations/animation/DegradableWall.atlas";
        this.atlas = new TextureAtlas(atlasPath);

        this.normalRegion = atlas.findRegion("Normal");
        this.firstDamageRegion = atlas.findRegion("FirstDestruction");
        this.secondDamageRegion = atlas.findRegion("SecondDestruction");

        if (normalRegion == null) Gdx.app.error("WALL_DEBUG", "AAA");
        if (firstDamageRegion == null) Gdx.app.error("WALL_DEBUG", "BBB");
        if (secondDamageRegion == null) Gdx.app.error("WALL_DEBUG", "CCC");
    }

    public TextureRegion getRegionForStage(DestroyStage stage) {
        return switch (stage) {
            case NORMAL -> normalRegion;
            case FIRST_DAMAGE -> firstDamageRegion;
            case SECOND_DAMAGE -> secondDamageRegion;
            case DESTROYED -> null;
        };
    }

    @Override
    public void dispose() {
        if (atlas != null) {
            atlas.dispose();
        }
    }

}

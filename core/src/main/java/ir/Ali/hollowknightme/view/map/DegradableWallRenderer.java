package ir.Ali.hollowknightme.view.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ir.Ali.hollowknightme.controller.map.DegradableWallManager;
import ir.Ali.hollowknightme.controller.map.MapManager;
import ir.Ali.hollowknightme.model.map.DegradableWall;
import ir.Ali.hollowknightme.model.map.DestroyStage;

public class DegradableWallRenderer {
    private final DegradableWallManager manager;
    private final DegradableWallAtlas atlas;

    public DegradableWallRenderer(DegradableWallManager manager, DegradableWallAtlas atlas) {
        this.manager = manager;
        this.atlas = atlas;
    }

    public void render(SpriteBatch batch) {
        for (DegradableWall wall : manager.getWalls()) {
            if (wall.getStage() == DestroyStage.DESTROYED) continue;

            TextureRegion region = atlas.getRegionForStage(wall.getStage());
            if (region != null) {
                Rectangle rect = wall.getGroundData().getRectangle();
                float width = rect.width / MapManager.PPM;
                float height = rect.height / MapManager.PPM;
                float x = rect.x / MapManager.PPM;
                float y = rect.y / MapManager.PPM;

                batch.draw(region, x, y, width, height);
            }
        }
    }
}

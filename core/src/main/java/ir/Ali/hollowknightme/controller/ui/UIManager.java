package ir.Ali.hollowknightme.controller.ui;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
public class UIManager {
    private static Skin skin;
    private static TextureAtlas atlas;
    public static void load() {
        atlas = new TextureAtlas(Gdx.files.internal("skin.atlas"));
        skin = new Skin(Gdx.files.internal("skin.json"), atlas);
    }
    public static Skin getSkin() {
        if (skin == null) {
            load();
        }
        return skin;
    }
    public static TextureAtlas getAtlas() {
        if (atlas == null) {
            load();
        }
        return atlas;
    }
    public static void dispose() {
        if (skin != null) {
            skin.dispose();
            skin = null;
        }
        if (atlas != null) {
            atlas.dispose();
            atlas = null;
        }
    }
}

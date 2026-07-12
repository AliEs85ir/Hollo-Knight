package ir.Ali.hollowknightme.view.screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
public abstract class BaseScreen extends ScreenAdapter {
    protected Stage stage;
    public BaseScreen() {
        stage = new Stage(new FitViewport(1920, 1080));
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }
    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    @Override
    public void dispose() {
        stage.dispose();
    }
    public Stage getStage() {
        return stage;
    }
}

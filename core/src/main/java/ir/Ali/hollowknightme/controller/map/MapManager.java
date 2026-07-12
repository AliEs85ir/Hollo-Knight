package ir.Ali.hollowknightme.controller.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Disposable;
import ir.Ali.hollowknightme.model.map.MapData;

public final class MapManager implements Disposable {

    public static final float PPM = 100f;
    private TiledMap currentMap;
    private OrthogonalTiledMapRenderer renderer;
    private MapData mapData;
    private String currentMapPath;

    public void loadMap(String path) {
        dispose();
        this.currentMapPath = path;
        currentMap = new TmxMapLoader().load(path);
        renderer = new OrthogonalTiledMapRenderer(currentMap, 1f / PPM);
        mapData = TiledMapLoader.loadMapData(getLayerObjects("collisions"), getLayerObjects("entities"));
    }

    private MapObjects getLayerObjects(String name) {
        return currentMap.getLayers().get(name) != null ? currentMap.getLayers().get(name).getObjects() : null;
    }

    public void render(OrthographicCamera camera) {
        if (renderer != null) {
            renderer.setView(camera);
            renderer.render();
        }
    }

    public float getMapWidth() {
        return currentMap != null ? (currentMap.getProperties().get("width", Integer.class) * currentMap.getProperties().get("tilewidth", Integer.class)) / PPM : 0;
    }

    public float getMapHeight() {
        return currentMap != null ? (currentMap.getProperties().get("height", Integer.class) * currentMap.getProperties().get("tileheight", Integer.class)) / PPM : 0;
    }

    public MapData getMapData() {
        return mapData;
    }

    public String getCurrentMapPath() {
        return currentMapPath;
    }

    @Override
    public void dispose() {
        if (currentMap != null) currentMap.dispose();
        if (renderer != null) renderer.dispose();
        currentMap = null;
        renderer = null;
        currentMapPath = null;
    }

    public TiledMap getCurrentMap() {
        return currentMap;
    }

    public OrthogonalTiledMapRenderer getRenderer() {
        return renderer;
    }
}

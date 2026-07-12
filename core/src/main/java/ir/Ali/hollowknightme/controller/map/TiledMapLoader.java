package ir.Ali.hollowknightme.controller.map;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PointMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import ir.Ali.hollowknightme.enums.enemy.EnemyType;
import ir.Ali.hollowknightme.model.map.*;

public final class TiledMapLoader {

    private TiledMapLoader() {}

    public static MapData loadMapData(MapObjects collisionObjects, MapObjects entityObjects) {
        MapData mapData = new MapData();

        if (collisionObjects != null) {
            for (MapObject object : collisionObjects) {
                if (object instanceof RectangleMapObject && "ground".equalsIgnoreCase(getClassName(object))) {
                    mapData.addGround(extractGroundData((RectangleMapObject) object));
                }
            }
        }

        if (entityObjects != null) {
            for (MapObject object : entityObjects) {
                String className = getClassName(object).toLowerCase();
                switch (className) {
                    case "spawnplayer" -> mapData.addSpawn(extractSpawnData(object, "PLAYER"));
                    case "spawnenemy" -> mapData.addSpawn(extractSpawnData(object, "ENEMY"));
                    case "transition" -> mapData.addTransition(extractTransitionData((RectangleMapObject) object));
                }
            }
        }
        return mapData;
    }

    private static String getClassName(MapObject object) {
        Object cls = object.getProperties().get("class");
        return cls != null ? cls.toString() : object.getName();
    }

    private static GroundData extractGroundData(RectangleMapObject object) {
        GroundType type = GroundType.valueOf(getString(object, "type", "FLOOR").toUpperCase());
        return new GroundData(
            object.getRectangle(), type,
            getBoolean(object, "isActiveLeft", false),
            getBoolean(object, "isActiveRight", false),
            getBoolean(object, "isActiveTop", false),
            getBoolean(object, "isActiveBottom", false)
        );
    }

    private static SpawnData extractSpawnData(MapObject object, String typeStr) {
        Vector2 position = (object instanceof PointMapObject pointObj) ?
            new Vector2(pointObj.getPoint().x, pointObj.getPoint().y) :
            new Vector2(((RectangleMapObject) object).getRectangle().x, ((RectangleMapObject) object).getRectangle().y);

        return new SpawnData(
            position,
            SpawnType.valueOf(typeStr),
            getBoolean(object, "facingRight", true),
            getBoolean(object, "isStart", false),
            EnemyType.valueOf(getString(object, "enemyType", "CRAWLID").toUpperCase()),
            getInt(object, "rangeID", 0)
        );
    }

    private static TransitionData extractTransitionData(RectangleMapObject object) {
        return new TransitionData(
            object.getRectangle(),
            TransitionType.valueOf(getString(object, "transitionType", "INPUT").toUpperCase())
        );
    }

    private static String getString(MapObject object, String key, String def) {
        Object val = object.getProperties().get(key);
        return val != null ? val.toString().trim() : def;
    }

    private static boolean getBoolean(MapObject object, String key, boolean def) {
        Object val = object.getProperties().get(key);
        if (val instanceof Boolean) return (Boolean) val;
        return val != null ? Boolean.parseBoolean(val.toString().trim()) : def;
    }

    private static int getInt(MapObject object, String key, int def) {
        Object val = object.getProperties().get(key);
        if (val instanceof Integer) return (Integer) val;
        try {
            return val != null ? Integer.parseInt(val.toString().trim()) : def;
        } catch (NumberFormatException e) {
            return def;
        }
    }
}

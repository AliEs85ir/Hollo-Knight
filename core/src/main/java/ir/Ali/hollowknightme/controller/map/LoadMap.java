package ir.Ali.hollowknightme.controller.map;

import ir.Ali.hollowknightme.model.enviroment.Environment;
import ir.Ali.hollowknightme.model.enviroment.Room;

public class LoadMap {
    private MapManager mapManager;
    private final Environment environment;

    private final String ROOM1_PATH = "room1.tmx";
    private final String ROOM2_PATH = "room2.tmx";
    private final String ROOM3_PATH = "room3.tmx";
    private final String ROOM4_PATH = "room4.tmx";
    private final String ROOM5_PATH = "room5.tmx";
    private final String ROOM6_PATH = "bossRoom.tmx";

    public LoadMap(Environment environment , MapManager mapManager) {
        this.environment = environment;
        this.mapManager = mapManager;

        mapManager.loadMap(ROOM1_PATH);
        environment.addRoom(new Room(ROOM1_PATH , mapManager.getMapData() , false));

        MapManager mapManager2 = new MapManager();
        mapManager2.loadMap(ROOM2_PATH);
        environment.addRoom(new Room(ROOM2_PATH , mapManager2.getMapData() , false));
        mapManager2.dispose();

        MapManager mapManager3 = new MapManager();
        mapManager3.loadMap(ROOM3_PATH);
        environment.addRoom(new Room(ROOM3_PATH , mapManager3.getMapData() , false));
        mapManager3.dispose();

        MapManager mapManager4 = new MapManager();
        mapManager4.loadMap(ROOM4_PATH);
        environment.addRoom(new Room(ROOM4_PATH , mapManager4.getMapData() , false));
        mapManager4.dispose();

        MapManager mapManager5 = new MapManager();
        mapManager5.loadMap(ROOM5_PATH);
        environment.addRoom(new Room(ROOM5_PATH , mapManager5.getMapData() , false));
        mapManager5.dispose();

        MapManager mapManager6 = new MapManager();
        mapManager6.loadMap(ROOM6_PATH);
        environment.addRoom(new Room(ROOM6_PATH , mapManager6.getMapData() , true));
        mapManager6.dispose();

//        creatRoom(ROOM1_PATH);
//        creatRoom(ROOM2_PATH);
//        creatRoom(ROOM3_PATH);
//        creatRoom(ROOM4_PATH);
//        creatRoom(ROOM5_PATH);
//        creatRoom(ROOM6_PATH);
    }

    private void creatRoom(String mapPath)
    {
        mapManager.loadMap(mapPath);
        environment.addRoom(new Room(mapPath, mapManager.getMapData(), false));
        mapManager.dispose();
    }
}

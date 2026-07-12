package ir.Ali.hollowknightme.model.enviroment;

import ir.Ali.hollowknightme.model.map.MapData;

public class Room {
    private final String mapPath;
    private final MapData mapData;
    private final RoomState state;
    private final boolean containsBoss;

    public Room(String mapPath, MapData mapData, boolean containsBoss) {
        this.mapPath = mapPath;
        this.mapData = mapData;
        this.state = new RoomState(mapData);
        this.containsBoss = containsBoss;
    }

    public String getMapPath() { return mapPath; }
    public MapData getMapData() { return mapData; }
    public RoomState getState() { return state; }

    public boolean isVisited() { return state.isVisited(); }
    public void setVisited(boolean visited) { state.setVisited(visited); }

    public boolean hasBoss() { return containsBoss; }
    public boolean isBossDefeated() { return state.isBossDefeated(); }
}

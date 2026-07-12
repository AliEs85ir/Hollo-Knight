package ir.Ali.hollowknightme.model.map;

import com.badlogic.gdx.utils.Array;

public class MapData {
    private final Array<GroundData> grounds;
    private final Array<TransitionData> transitions;
    private final Array<SpawnData> spawns;

    public MapData() {
        this.grounds = new Array<>();
        this.transitions = new Array<>();
        this.spawns = new Array<>();
    }

    public void addGround(GroundData ground) { grounds.add(ground); }
    public void addTransition(TransitionData transition) { transitions.add(transition); }
    public void addSpawn(SpawnData spawn) { spawns.add(spawn); }

    public Array<GroundData> getGrounds() { return grounds; }
    public Array<TransitionData> getTransitions() { return transitions; }
    public Array<SpawnData> getSpawns() { return spawns; }

    public void clear() {
        grounds.clear();
        transitions.clear();
        spawns.clear();
    }
}

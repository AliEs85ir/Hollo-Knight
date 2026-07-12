package ir.Ali.hollowknightme.model.enviroment;

import com.badlogic.gdx.math.Vector2;
import ir.Ali.hollowknightme.enums.enemy.EnemyType;

public class EnemyPersistentData {
    private String id;
    private EnemyType type;
    private final Vector2 position;
    private boolean facingRight;
    private int rangeId;
    private float hp;
    private float maxHp;
    private boolean alive;

    public EnemyPersistentData(String id, EnemyType type, Vector2 position, boolean facingRight, int rangeId, float hp, float maxHp) {
        this.id = id;
        this.type = type;
        this.position = new Vector2(position);
        this.facingRight = facingRight;
        this.rangeId = rangeId;
        this.hp = hp;
        this.maxHp = maxHp;
        this.alive = true;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public EnemyType getType() { return type; }
    public void setType(EnemyType type) { this.type = type; }

    public Vector2 getPosition() { return position; }
    public void setPosition(Vector2 pos) { this.position.set(pos); }

    public boolean isFacingRight() { return facingRight; }
    public void setFacingRight(boolean facingRight) { this.facingRight = facingRight; }

    public int getRangeId() { return rangeId; }
    public void setRangeId(int rangeId) { this.rangeId = rangeId; }

    public float getHp() { return hp; }
    public void setHp(float hp) { this.hp = hp; }

    public float getMaxHp() { return maxHp; }
    public void setMaxHp(float maxHp) { this.maxHp = maxHp; }

    public boolean isAlive() { return alive; }
    public void setAlive(boolean alive) { this.alive = alive; }
}

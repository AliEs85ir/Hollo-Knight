package ir.Ali.hollowknightme.model.map;

import com.badlogic.gdx.math.Vector2;
import ir.Ali.hollowknightme.enums.enemy.EnemyType;

public class SpawnData {
    private final Vector2 position;
    private final SpawnType type;
    private final boolean facingRight;
    private final boolean isStart;
    private final EnemyType enemyType;
    private final int rangeId;

    public SpawnData(Vector2 position, SpawnType type, boolean facingRight, boolean isStart, EnemyType enemyType, int rangeId) {
        this.position = position;
        this.type = type;
        this.facingRight = facingRight;
        this.isStart = isStart;
        this.enemyType = enemyType;
        this.rangeId = rangeId;
    }

    public Vector2 getPosition() { return position; }
    public SpawnType getType() { return type; }
    public boolean isFacingRight() { return facingRight; }
    public boolean isStart() { return isStart; }
    public EnemyType getEnemyType() { return enemyType; }
    public int getRangeId() { return rangeId; }
}

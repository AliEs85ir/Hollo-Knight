package ir.Ali.hollowknightme.controller.enemy;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import ir.Ali.hollowknightme.enums.fixture.FixtureType;
import ir.Ali.hollowknightme.controller.camera.CameraShakeManager;
import ir.Ali.hollowknightme.controller.game.GameRunManager;
import ir.Ali.hollowknightme.controller.game.GameStateManager;
import ir.Ali.hollowknightme.enums.game.GameStatus;
import ir.Ali.hollowknightme.model.enviroment.Room;
import ir.Ali.hollowknightme.enums.enemy.EnemyState;
import ir.Ali.hollowknightme.model.enemy.FalseKnight;

public class FalseKnightController extends GroundEnemyController {

    private enum ActionPhase {
        NONE, ANTICIPATION, EXECUTION, RECOVERY
    }

    private final FalseKnight knightBoss;
    private GameRunManager gameRunManager;

    private static final float CLOSE_RANGE = 6f;
    private static final float MID_RANGE = 11f;

    private float decisionTimer;
    private float stepTimer;
    private float hitTimer;
    private int recentHits;

    private float chargeCooldown = 0f;
    private float powerSlamCooldown = 0f;
    private float leapCooldown = 0f;

    private float targetChargeX = 0f;

    private EnemyState currentMove = EnemyState.IDLE;
    private ActionPhase currentPhase = ActionPhase.NONE;

    private final EnemyState[] moveHistory = new EnemyState[]{EnemyState.IDLE, EnemyState.IDLE, EnemyState.IDLE};
    private boolean deathInitialized = false;

    public FalseKnightController(FalseKnight enemy, World world) {
        super(enemy, world);
        this.knightBoss = enemy;
        this.decisionTimer = knightBoss.isPhaseTwo() ? MathUtils.random(0.4f, 0.7f) : MathUtils.random(0.8f, 1.3f);
    }

    public void setGameRunManager(GameRunManager gameRunManager) {
        this.gameRunManager = gameRunManager;
    }

    private void initDeath() {
        if (deathInitialized) return;
        deathInitialized = true;

        this.currentPhase = ActionPhase.NONE;
        this.currentMove = EnemyState.IDLE;
        this.decisionTimer = 999f;

        knightBoss.setMaceActive(false);
        knightBoss.setPowerSlamming(false);

        if (gameRunManager != null) {
            gameRunManager.bossDefeated();
        } else {
            ir.Ali.hollowknightme.model.enviroment.Room currentRoom = GameStateManager.getInstance().getCurrentRoom();
            if (currentRoom != null) {
                currentRoom.getState().setBossDefeated(true);
            }
            GameStateManager.getInstance().setGameStatus(GameStatus.WIN);
        }
    }

    @Override
    public void update(float dt) {
        if (!knightBoss.isAlive()) {
            initDeath();
            super.update(dt);
            if (knightBoss.isGrounded()) {
                knightBoss.getBody().setLinearVelocity(0, 0);
                knightBoss.getBody().setGravityScale(0);
                knightBoss.getBody().setType(com.badlogic.gdx.physics.box2d.BodyDef.BodyType.StaticBody);            } else {
                knightBoss.getBody().setLinearVelocity(0, knightBoss.getBody().getLinearVelocity().y);
            }
            return;
        }

        super.update(dt);
        knightBoss.updateHitFlash(dt);

        if (target == null) return;

        updateCooldowns(dt);

        hitTimer += dt;
        if (hitTimer > 1.5f) {
            recentHits = 0;
        }

        if (knightBoss.getCurrentState() == EnemyState.STUN || knightBoss.getCurrentState() == EnemyState.STUN_RECOVER) {
            handleStunState(dt);
            return;
        }

        if (currentPhase != ActionPhase.NONE) {
            if (currentPhase == ActionPhase.ANTICIPATION) {
                updateFacing();
            }
            processActionPhase(dt);
            return;
        }

        decisionTimer -= dt;

        if (knightBoss.getCurrentState() == EnemyState.RUN) {
            maintainIdleOrRun();
            return;
        }

        if (decisionTimer <= 0) {
            decideNextAction();
        } else {
            maintainIdleOrRun();
        }
    }

    private void updateCooldowns(float dt) {
        if (chargeCooldown > 0) chargeCooldown -= dt;
        if (powerSlamCooldown > 0) powerSlamCooldown -= dt;
        if (leapCooldown > 0) leapCooldown -= dt;
    }

    @Override
    public void playerHitEnemy(float damage, float hitDir) {
        super.playerHitEnemy(damage, hitDir);
        if (!knightBoss.isAlive()) {
            initDeath();
            return;
        }

        EnemyState state = knightBoss.getCurrentState();
        if (state == EnemyState.STUN || state == EnemyState.STUN_RECOVER) {
            return;
        }

        recentHits++;
        hitTimer = 0f;

        if (recentHits >= 3 && canInterruptForDefense()) {
            recentHits = 0;
            interruptWithDefensiveLeap();
        }

        knightBoss.triggerHitFlash();
    }

    @Override
    public void enemyHitPlayer(float knightHitDir) {
        super.enemyHitPlayer(knightHitDir);
        if (!knightBoss.isAlive()) return;

        if (currentMove == EnemyState.CHARGE_RUN && currentPhase == ActionPhase.EXECUTION) {
            stepTimer = 0;
        }
    }

    @Override
    public void enemyHitWall() {
        super.enemyHitWall();
        if (currentMove == EnemyState.CHARGE_RUN && currentPhase == ActionPhase.EXECUTION) {
            stepTimer = 0;
        }
    }

    @Override
    public void enemyLanded() {
        super.enemyLanded();

        if (currentPhase != ActionPhase.EXECUTION) {
            return;
        }

        if (currentMove == EnemyState.OFFENSIVE_LEAP) {
            CameraShakeManager.getInstance().shake(0.2f, 0.2f);
            transitionToPhase(ActionPhase.RECOVERY, EnemyState.ATTACK_RECOVER, 0.4f);
        } else if (currentMove == EnemyState.DEFENSIVE_LEAP) {
            CameraShakeManager.getInstance().shake(0.15f, 0.1f);
            transitionToPhase(ActionPhase.RECOVERY, EnemyState.ATTACK_RECOVER, 0.3f);
        } else if (currentMove == EnemyState.POWER_SLAM) {
            knightBoss.getBody().setLinearVelocity(0, knightBoss.getBody().getLinearVelocity().y);
            transitionToPhase(ActionPhase.EXECUTION, EnemyState.POWER_SLAM, 0.05f);
        }
    }

    private void handleStunState(float dt) {
        stepTimer -= dt;
        if (knightBoss.getCurrentState() == EnemyState.STUN) {
            if (stepTimer <= 0) {
                knightBoss.setState(EnemyState.STUN_RECOVER);
                stepTimer = 1.2f;
            }
        } else if (knightBoss.getCurrentState() == EnemyState.STUN_RECOVER) {
            if (stepTimer <= 0) {
                knightBoss.setPhaseTwo(true);
                resetToIdle();
            }
        }
    }

    private void processActionPhase(float dt) {
        stepTimer -= dt;

        if (currentMove == EnemyState.CHARGE_RUN && currentPhase == ActionPhase.EXECUTION) {
            boolean reachedTarget = (knightBoss.isFacingRight() && knightBoss.getX() >= targetChargeX) ||
                (!knightBoss.isFacingRight() && knightBoss.getX() <= targetChargeX);

            if (reachedTarget || isLedgeAhead()) {
                stepTimer = 0;
            }
        }

        if (stepTimer <= 0) {
            advanceActionPhase();
        }
    }

    private void advanceActionPhase() {
        switch (currentPhase) {
            case ANTICIPATION:
                startExecutionPhase();
                break;
            case EXECUTION:
                if (currentMove == EnemyState.POWER_SLAM) {
                    applyPowerSlamShockwave();
                    transitionToPhase(ActionPhase.RECOVERY, EnemyState.ATTACK_RECOVER, 0.9f);
                } else {
                    knightBoss.setMaceActive(false);
                    knightBoss.setPowerSlamming(false);
                    knightBoss.getBody().setLinearVelocity(0, knightBoss.getBody().getLinearVelocity().y);

                    float recoveryTime = (currentMove == EnemyState.CHARGE_RUN) ? 0.6f : 0.4f;
                    transitionToPhase(ActionPhase.RECOVERY, EnemyState.ATTACK_RECOVER, recoveryTime);
                }
                break;
            case RECOVERY:
            default:
                resetToIdle();
                break;
        }
    }

    private void startExecutionPhase() {
        float gravity = Math.abs(world.getGravity().y * knightBoss.getBody().getGravityScale());
        if (gravity <= 0) gravity = 20.0f;

        switch (currentMove) {
            case MACE_SLAM:
                transitionToPhase(ActionPhase.EXECUTION, EnemyState.MACE_SLAM, 0.5f);
                knightBoss.setMaceActive(true);
                knightBoss.getBody().setLinearVelocity(0, knightBoss.getBody().getLinearVelocity().y);
                CameraShakeManager.getInstance().shake(0.4f, 0.35f);
                break;

            case POWER_SLAM:
                transitionToPhase(ActionPhase.EXECUTION, EnemyState.JUMP, 5.0f);
                knightBoss.setGrounded(false);
                float vyPS = 16.0f;
                float flightTimePS = (2 * vyPS) / gravity;
                float targetVxPS = MathUtils.clamp((target.getX() - knightBoss.getX()) / flightTimePS, -16.0f, 16.0f);
                knightBoss.getBody().setLinearVelocity(targetVxPS, vyPS);
                break;

            case CHARGE_RUN:
                transitionToPhase(ActionPhase.EXECUTION, EnemyState.CHARGE_RUN, 2.0f);
                float dir = knightBoss.isFacingRight() ? 1f : -1f;
                knightBoss.getBody().setLinearVelocity(9.0f * (knightBoss.isPhaseTwo() ? 1.35f : 1.0f) * dir, knightBoss.getBody().getLinearVelocity().y);
                break;

            case OFFENSIVE_LEAP:
                transitionToPhase(ActionPhase.EXECUTION, EnemyState.JUMP, 5.0f);
                knightBoss.setGrounded(false);
                float vyOL = 9.5f;
                float flightTimeOL = (2 * vyOL) / gravity;
                float targetVxOL = MathUtils.clamp((target.getX() - knightBoss.getX()) / flightTimeOL, -12.0f, 12.0f);
                knightBoss.getBody().setLinearVelocity(targetVxOL, vyOL);
                break;

            case DEFENSIVE_LEAP:
                transitionToPhase(ActionPhase.EXECUTION, EnemyState.JUMP, 5.0f);
                knightBoss.setGrounded(false);
                float defDir = knightBoss.isFacingRight() ? -1f : 1f;
                knightBoss.getBody().setLinearVelocity(8.0f * defDir, 9.5f);
                break;

            default:
                resetToIdle();
                break;
        }
    }

    private void applyPowerSlamShockwave() {
        CameraShakeManager.getInstance().shake(1.2f, 0.8f);
        knightBoss.setMaceActive(true);
        knightBoss.setPowerSlamming(true);

        boolean isTargetGrounded = Math.abs(target.getY() - knightBoss.getY()) < 1.5f;

        if (isTargetGrounded) {
            float dx = Math.abs(target.getX() - knightBoss.getX());
            if (dx < 5.0f) {
                float hitDir = target.getX() > knightBoss.getX() ? 1f : -1f;
                enemyHitPlayer(hitDir);
            }
        }
    }

    private void decideNextAction() {
        if (!knightBoss.isPhaseTwo() && knightBoss.getHp() <= knightBoss.getMaxHp() * 0.5f) {
            triggerStun();
            return;
        }

        updateFacing();

        float distX = Math.abs(target.getX() - knightBoss.getX());
        float distY = target.getY() - knightBoss.getY();

        float wMace = 0, wCharge = 0, wLeap = 0, wPowerSlam = 0, wDefensive = 0;

        if (distY > 4.0f) {
            wCharge = 70;
            wLeap = 20;
            if (knightBoss.isPhaseTwo()) wPowerSlam = 10;
        } else if (distX <= CLOSE_RANGE) {
            wMace = 65;
            wCharge = 20;
            wDefensive = 10;
            if (knightBoss.isPhaseTwo()) wPowerSlam = 5;
        } else if (distX <= MID_RANGE) {
            wCharge = 90;
            wLeap = 5;
            wMace = 5;
            if (knightBoss.isPhaseTwo()) wPowerSlam = 10;
        } else {
            wCharge = 90;
            wLeap = 10;
            if (knightBoss.isPhaseTwo()) wPowerSlam = 10;
        }

        if (chargeCooldown > 0) wCharge = 0;
        if (powerSlamCooldown > 0) wPowerSlam = 0;
        if (leapCooldown > 0) wLeap = 0;

        wMace = applyAntiSpamPenalty(EnemyState.MACE_SLAM, wMace);
        wCharge = applyAntiSpamPenalty(EnemyState.CHARGE_RUN, wCharge);
        wLeap = applyAntiSpamPenalty(EnemyState.OFFENSIVE_LEAP, wLeap);
        wPowerSlam = applyAntiSpamPenalty(EnemyState.POWER_SLAM, wPowerSlam);
        wDefensive = applyAntiSpamPenalty(EnemyState.DEFENSIVE_LEAP, wDefensive);

        if (wMace > 0) wMace += MathUtils.random(0, 10);
        if (wCharge > 0) wCharge += MathUtils.random(0, 10);
        if (wLeap > 0) wLeap += MathUtils.random(0, 10);
        if (wPowerSlam > 0) wPowerSlam += MathUtils.random(0, 10);
        if (wDefensive > 0) wDefensive += MathUtils.random(0, 10);

        float total = wMace + wCharge + wLeap + wPowerSlam + wDefensive;

        if (total == 0) {
            beginActionSequence(EnemyState.MACE_SLAM);
            return;
        }

        float rand = MathUtils.random(total);
        EnemyState chosenMove;

        if (rand < wMace) {
            chosenMove = EnemyState.MACE_SLAM;
        } else if (rand < wMace + wCharge) {
            chosenMove = EnemyState.CHARGE_RUN;
        } else if (rand < wMace + wCharge + wLeap) {
            chosenMove = EnemyState.OFFENSIVE_LEAP;
        } else if (rand < wMace + wCharge + wLeap + wPowerSlam) {
            chosenMove = EnemyState.POWER_SLAM;
        } else {
            chosenMove = EnemyState.DEFENSIVE_LEAP;
        }

        pushToHistory(chosenMove);
        beginActionSequence(chosenMove);
    }

    private float applyAntiSpamPenalty(EnemyState state, float currentWeight) {
        if (currentWeight <= 0) return 0;
        int occurrences = 0;
        for (EnemyState pastState : moveHistory) {
            if (pastState == state) {
                occurrences++;
            }
        }
        if (occurrences == 1) return currentWeight * 0.35f;
        if (occurrences >= 2) return currentWeight * 0.02f;
        return currentWeight;
    }

    private void pushToHistory(EnemyState state) {
        moveHistory[0] = moveHistory[1];
        moveHistory[1] = moveHistory[2];
        moveHistory[2] = state;
    }

    private void beginActionSequence(EnemyState move) {
        currentMove = move;
        knightBoss.getBody().setLinearVelocity(0, knightBoss.getBody().getLinearVelocity().y);

        switch (move) {
            case MACE_SLAM:
                transitionToPhase(ActionPhase.ANTICIPATION, EnemyState.ATTACK_ANTIC, 0.4f);
                break;

            case POWER_SLAM:
                powerSlamCooldown = knightBoss.isPhaseTwo() ? 5.0f : 7.0f;
                transitionToPhase(ActionPhase.ANTICIPATION, EnemyState.JUMP_ANTIC, 0.5f);
                break;

            case CHARGE_RUN:
                targetChargeX = target.getX();
                chargeCooldown = knightBoss.isPhaseTwo() ? 3.5f : 5.0f;
                transitionToPhase(ActionPhase.ANTICIPATION, EnemyState.RUN_ANTIC, 0.35f);
                break;

            case OFFENSIVE_LEAP:
                leapCooldown = knightBoss.isPhaseTwo() ? 3.0f : 4.5f;
                transitionToPhase(ActionPhase.ANTICIPATION, EnemyState.JUMP_ANTIC, 0.35f);
                break;

            case DEFENSIVE_LEAP:
                transitionToPhase(ActionPhase.ANTICIPATION, EnemyState.JUMP_ANTIC, 0.2f);
                break;

            default:
                resetToIdle();
                break;
        }
    }

    private void transitionToPhase(ActionPhase phase, EnemyState state, float duration) {
        currentPhase = phase;
        knightBoss.setState(state);
        stepTimer = duration / (knightBoss.isPhaseTwo() ? 1.2f : 0.9f);
    }

    private void interruptWithDefensiveLeap() {
        if (!isDefensiveLeapSafe()) return;
        knightBoss.setMaceActive(false);
        knightBoss.setPowerSlamming(false);
        pushToHistory(EnemyState.DEFENSIVE_LEAP);
        beginActionSequence(EnemyState.DEFENSIVE_LEAP);
    }

    private boolean isDefensiveLeapSafe() {
        float checkOffset = knightBoss.isFacingRight() ? -3.0f : 3.0f;
        float startX = knightBoss.getX();
        float endX = knightBoss.getX() + checkOffset;
        final boolean[] obstacleDetected = {false};

        world.rayCast((fixture, point, normal, fraction) -> {
            Object data = fixture.getUserData();
            if (data == FixtureType.WALL) {
                obstacleDetected[0] = true;
                return 0;
            }
            return -1;
        }, new com.badlogic.gdx.math.Vector2(startX, knightBoss.getY()), new com.badlogic.gdx.math.Vector2(endX, knightBoss.getY()));

        float rayYStart = knightBoss.getY();
        float rayYEnd = knightBoss.getY() - 1.8f;
        final boolean[] groundDetected = {false};

        world.rayCast((fixture, point, normal, fraction) -> {
            Object data = fixture.getUserData();
            if (data == FixtureType.GROUND || data == FixtureType.PLATFORM) {
                groundDetected[0] = true;
                return 0;
            }
            return -1;
        }, new com.badlogic.gdx.math.Vector2(endX, rayYStart), new com.badlogic.gdx.math.Vector2(endX, rayYEnd));

        return !obstacleDetected[0] && groundDetected[0];
    }

    private boolean canInterruptForDefense() {
        return currentPhase == ActionPhase.NONE || currentPhase == ActionPhase.ANTICIPATION ||
            knightBoss.getCurrentState() == EnemyState.IDLE || knightBoss.getCurrentState() == EnemyState.RUN;
    }

    private void triggerStun() {
        knightBoss.setState(EnemyState.STUN);
        knightBoss.getBody().setLinearVelocity(0, knightBoss.getBody().getLinearVelocity().y);
        knightBoss.setMaceActive(false);
        knightBoss.setPowerSlamming(false);
        currentPhase = ActionPhase.NONE;
        currentMove = EnemyState.IDLE;
        stepTimer = 10.0f;
    }

    private void maintainIdleOrRun() {
        updateFacing();
        float distX = Math.abs(target.getX() - knightBoss.getX());

        if (distX > CLOSE_RANGE) {
            if (!isLedgeAhead()) {
                knightBoss.setState(EnemyState.RUN);
                float dir = knightBoss.isFacingRight() ? 1f : -1f;
                knightBoss.getBody().setLinearVelocity(4.0f * (knightBoss.isPhaseTwo() ? 1.35f : 1.0f) * dir, knightBoss.getBody().getLinearVelocity().y);
            } else {
                knightBoss.setState(EnemyState.IDLE);
                knightBoss.getBody().setLinearVelocity(0, knightBoss.getBody().getLinearVelocity().y);
                decisionTimer = 0f;
            }
        } else {
            knightBoss.setState(EnemyState.IDLE);
            knightBoss.getBody().setLinearVelocity(0, knightBoss.getBody().getLinearVelocity().y);
            decisionTimer = 0f;
        }
    }

    private void updateFacing() {
        if (currentPhase == ActionPhase.NONE || currentPhase == ActionPhase.ANTICIPATION) {
            knightBoss.setFacingRight(target.getX() > knightBoss.getX());
        }
    }

    private void resetToIdle() {
        currentPhase = ActionPhase.NONE;
        currentMove = EnemyState.IDLE;
        knightBoss.setState(EnemyState.IDLE);
        knightBoss.setMaceActive(false);
        knightBoss.setPowerSlamming(false);
        knightBoss.getBody().setLinearVelocity(0, knightBoss.getBody().getLinearVelocity().y);

        float baseDecisionTime = knightBoss.isPhaseTwo() ? 0.6f : 1f;
        decisionTimer = baseDecisionTime + MathUtils.random(0.1f, 0.3f);
    }

    @Override
    protected void handleDeath() {
        initDeath();
        if (knightBoss.isGrounded()) {
            knightBoss.getBody().setLinearVelocity(0, 0);
            knightBoss.getBody().setGravityScale(0);
        } else {
            knightBoss.getBody().setLinearVelocity(0, knightBoss.getBody().getLinearVelocity().y);
        }
        super.handleDeath();
    }
}

package ir.Ali.hollowknightme.controller.hud;

import com.badlogic.gdx.utils.Queue;
import ir.Ali.hollowknightme.enums.hud.SoulState;
import ir.Ali.hollowknightme.controller.sound.AudioManager;
import ir.Ali.hollowknightme.enums.sound.SfxType;

public class SoulManager {
    private SoulState currentState;
    private final Queue<SoulState> transitionQueue;
    private int currentSoulValue;

    public SoulManager(int initialSoul) {
        this.transitionQueue = new Queue<>();
        this.currentSoulValue = initialSoul;
        this.currentState = determineTargetLevel(initialSoul);
    }

    public void update(int knightSoul) {
        if (this.currentSoulValue != knightSoul) {
            if (knightSoul > this.currentSoulValue) {
                AudioManager.getInstance().playSfx(SfxType.SOUL);
            }

            SoulState targetState = determineTargetLevel(knightSoul);
            SoulState expectedEndState = getExpectedEndState();

            if (targetState != expectedEndState) {
                queueTransitions(expectedEndState, targetState);
            }
            this.currentSoulValue = knightSoul;
        }
    }

    public SoulState getCurrentRenderState() {
        if (!transitionQueue.isEmpty()) {
            return transitionQueue.first();
        }
        return currentState;
    }

    public void completeCurrentTransition() {
        if (!transitionQueue.isEmpty()) {
            SoulState completedTransition = transitionQueue.removeFirst();
            currentState = getEndStateOfTransition(completedTransition);
        }
    }

    public boolean isTransitioning() {
        return !transitionQueue.isEmpty();
    }

    private SoulState determineTargetLevel(int soul) {
        if (soul <= 32) return SoulState.ZERO;
        if (soul <= 62) return SoulState.ONE;
        if (soul <= 98) return SoulState.TWO;
        return SoulState.THREE;
    }

    private SoulState getExpectedEndState() {
        if (transitionQueue.isEmpty()) return currentState;
        return getEndStateOfTransition(transitionQueue.last());
    }

    private void queueTransitions(SoulState from, SoulState to) {
        int fromLevel = getLevel(from);
        int toLevel = getLevel(to);

        if (fromLevel < toLevel) {
            for (int i = fromLevel; i < toLevel; i++) {
                transitionQueue.addLast(getAscendingTransition(i));
            }
        } else if (fromLevel > toLevel) {
            for (int i = fromLevel; i > toLevel; i--) {
                transitionQueue.addLast(getDescendingTransition(i));
            }
        }
    }

    private int getLevel(SoulState state) {
        if (state == SoulState.ZERO) return 0;
        if (state == SoulState.ONE) return 1;
        if (state == SoulState.TWO) return 2;
        if (state == SoulState.THREE) return 3;
        return 0;
    }

    private SoulState getAscendingTransition(int currentLevel) {
        if (currentLevel == 0) return SoulState.ZERO_TO_ONE;
        if (currentLevel == 1) return SoulState.ONE_TO_TWO;
        if (currentLevel == 2) return SoulState.TWO_TO_THREE;
        return SoulState.TWO_TO_THREE;
    }

    private SoulState getDescendingTransition(int currentLevel) {
        if (currentLevel == 3) return SoulState.THREE_TO_TWO;
        if (currentLevel == 2) return SoulState.TWO_TO_ONE;
        if (currentLevel == 1) return SoulState.ONE_TO_ZERO;
        return SoulState.ONE_TO_ZERO;
    }

    private SoulState getEndStateOfTransition(SoulState transition) {
        if (transition == SoulState.ZERO_TO_ONE) return SoulState.ONE;
        if (transition == SoulState.ONE_TO_TWO) return SoulState.TWO;
        if (transition == SoulState.TWO_TO_THREE) return SoulState.THREE;
        if (transition == SoulState.THREE_TO_TWO) return SoulState.TWO;
        if (transition == SoulState.TWO_TO_ONE) return SoulState.ONE;
        if (transition == SoulState.ONE_TO_ZERO) return SoulState.ZERO;
        return transition;
    }
}

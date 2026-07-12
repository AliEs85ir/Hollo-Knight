package ir.Ali.hollowknightme.view.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ir.Ali.hollowknightme.enums.enemy.EnemyState;
import ir.Ali.hollowknightme.enums.enemy.EnemyType;

import java.util.EnumMap;
import java.util.Map;

public class EnemyAnimationLibrary {
    private final Map<EnemyType, Map<EnemyState, Animation<TextureRegion>>> library;
    private final Map<EnemyType, TextureAtlas> atlases;

    public EnemyAnimationLibrary() {
        this.library = new EnumMap<>(EnemyType.class);
        this.atlases = new EnumMap<>(EnemyType.class);

        for (EnemyType type : EnemyType.values()) {
            library.put(type, new EnumMap<>(EnemyState.class));
        }

        loadAtlases();
        loadAllAnimations();
    }

    private void loadAtlases() {
        atlases.put(EnemyType.CRAWLID, new TextureAtlas(Gdx.files.internal("animations/animation/Crawlid.atlas")));
        atlases.put(EnemyType.HUSK_HORNHEAD, new TextureAtlas(Gdx.files.internal("animations/animation/HuskHornhead.atlas")));
        atlases.put(EnemyType.CRYSTAL_GUARDIAN, new TextureAtlas(Gdx.files.internal("animations/animation/CrystalGuardian.atlas")));
        atlases.put(EnemyType.WINGED_SENTRY, new TextureAtlas(Gdx.files.internal("animations/animation/WingedSentry.atlas")));
        atlases.put(EnemyType.FALSE_KNIGHT, new TextureAtlas(Gdx.files.internal("animations/animation/FalseKnight.atlas")));
    }

    private void loadAllAnimations() {
        loadAnim(EnemyType.CRAWLID, EnemyState.WALK, "Walk", 0.1f, Animation.PlayMode.LOOP);
        loadAnim(EnemyType.CRAWLID, EnemyState.TURN, "Turn", 0.15f, Animation.PlayMode.NORMAL);
        loadAnim(EnemyType.CRAWLID, EnemyState.DEATH_AIR, "Death Air", 0.1f, Animation.PlayMode.NORMAL);
        loadAnim(EnemyType.CRAWLID, EnemyState.DEATH_LAND, "Death Land", 0.1f, Animation.PlayMode.NORMAL);

        loadAnim(EnemyType.HUSK_HORNHEAD, EnemyState.IDLE, "Idle", 0.1f, Animation.PlayMode.LOOP);
        loadAnim(EnemyType.HUSK_HORNHEAD, EnemyState.ATTACK_ANTICIPATE, "Attack Anticipate", 0.1f, Animation.PlayMode.NORMAL);
        loadAnim(EnemyType.HUSK_HORNHEAD, EnemyState.ATTACK_LUNGE, "Attack Lunge", 0.1f, Animation.PlayMode.LOOP);
        loadAnim(EnemyType.HUSK_HORNHEAD, EnemyState.WALK, "Walk", 0.1f, Animation.PlayMode.LOOP);
        loadAnim(EnemyType.HUSK_HORNHEAD, EnemyState.TURN, "Turn", 0.1f, Animation.PlayMode.NORMAL);
        loadAnim(EnemyType.HUSK_HORNHEAD, EnemyState.DEATH_AIR, "Death Air", 0.1f, Animation.PlayMode.NORMAL);
        loadAnim(EnemyType.HUSK_HORNHEAD, EnemyState.DEATH_LAND, "Death Land", 0.1f, Animation.PlayMode.NORMAL);
        loadAnim(EnemyType.HUSK_HORNHEAD, EnemyState.ATTACK_COOLDOWN, "Attack Cooldown", 0.1f, Animation.PlayMode.NORMAL);

        loadAnim(EnemyType.CRYSTAL_GUARDIAN, EnemyState.IDLE, "Idle", 0.1f, Animation.PlayMode.LOOP);
        loadAnim(EnemyType.CRYSTAL_GUARDIAN, EnemyState.EVADE, "Evade", 0.1f, Animation.PlayMode.NORMAL);
        loadAnim(EnemyType.CRYSTAL_GUARDIAN, EnemyState.RUN, "Run", 0.1f, Animation.PlayMode.LOOP);
        loadAnim(EnemyType.CRYSTAL_GUARDIAN, EnemyState.SHOOT, "Shoot", 0.1f, Animation.PlayMode.NORMAL);
        loadAnim(EnemyType.CRYSTAL_GUARDIAN, EnemyState.TURN, "Turn", 0.1f, Animation.PlayMode.NORMAL);
        loadAnim(EnemyType.CRYSTAL_GUARDIAN, EnemyState.DEATH_AIR, "Death Air", 0.1f, Animation.PlayMode.NORMAL);
        loadAnim(EnemyType.CRYSTAL_GUARDIAN, EnemyState.DEATH_LAND, "Death Land", 0.1f, Animation.PlayMode.NORMAL);

        loadAnim(EnemyType.WINGED_SENTRY, EnemyState.IDLE, "Idle", 0.15f, Animation.PlayMode.LOOP);
        loadAnim(EnemyType.WINGED_SENTRY, EnemyState.CHARGE_ANTIC, "Charge Antic", 0.15f, Animation.PlayMode.NORMAL);
        loadAnim(EnemyType.WINGED_SENTRY, EnemyState.CHARGE, "Charge", 0.15f, Animation.PlayMode.LOOP);
        loadAnim(EnemyType.WINGED_SENTRY, EnemyState.DEATH_AIR, "Death Air", 0.1f, Animation.PlayMode.NORMAL);
        loadAnim(EnemyType.WINGED_SENTRY, EnemyState.DEATH_LAND, "Death Land", 0.1f, Animation.PlayMode.NORMAL);
        loadAnim(EnemyType.WINGED_SENTRY, EnemyState.TURN, "Turn To Fly", 0.1f, Animation.PlayMode.NORMAL);

        loadAnim(EnemyType.FALSE_KNIGHT, EnemyState.IDLE, "Idle", 0.15f, Animation.PlayMode.LOOP);
        loadAnim(EnemyType.FALSE_KNIGHT, EnemyState.RUN, "Run", 0.1f, Animation.PlayMode.LOOP);
        loadAnim(EnemyType.FALSE_KNIGHT, EnemyState.CHARGE_RUN, "Run", 0.08f, Animation.PlayMode.LOOP);
        loadAnim(EnemyType.FALSE_KNIGHT, EnemyState.ATTACK_ANTIC, "Attack Antic", 0.1f, Animation.PlayMode.NORMAL);
        loadAnim(EnemyType.FALSE_KNIGHT, EnemyState.RUN_ANTIC, "Run Antic", 0.1f, Animation.PlayMode.NORMAL);
        loadAnim(EnemyType.FALSE_KNIGHT, EnemyState.JUMP_ANTIC, "Jump Antic", 0.1f, Animation.PlayMode.NORMAL);
        loadAnim(EnemyType.FALSE_KNIGHT, EnemyState.ATTACK, "Attack", 0.05f, Animation.PlayMode.NORMAL);
        loadAnim(EnemyType.FALSE_KNIGHT, EnemyState.MACE_SLAM, "Attack", 0.05f, Animation.PlayMode.NORMAL);
        loadAnim(EnemyType.FALSE_KNIGHT, EnemyState.POWER_SLAM, "Attack", 0.05f, Animation.PlayMode.NORMAL);
        loadAnim(EnemyType.FALSE_KNIGHT, EnemyState.JUMP_ATTACK, "Jump Attack", 0.1f, Animation.PlayMode.LOOP);
        loadAnim(EnemyType.FALSE_KNIGHT, EnemyState.OFFENSIVE_LEAP, "Jump", 0.1f, Animation.PlayMode.LOOP);
        loadAnim(EnemyType.FALSE_KNIGHT, EnemyState.JUMP, "Jump", 0.3f, Animation.PlayMode.LOOP);
        loadAnim(EnemyType.FALSE_KNIGHT, EnemyState.DEFENSIVE_LEAP, "Jump", 0.1f, Animation.PlayMode.LOOP);
        loadAnim(EnemyType.FALSE_KNIGHT, EnemyState.LAND, "Land", 0.1f, Animation.PlayMode.NORMAL);
        loadAnim(EnemyType.FALSE_KNIGHT, EnemyState.ATTACK_RECOVER, "Attack Recover", 0.08f, Animation.PlayMode.NORMAL);
        loadAnim(EnemyType.FALSE_KNIGHT, EnemyState.STUN, "Body", 0.15f, Animation.PlayMode.LOOP);
        loadAnim(EnemyType.FALSE_KNIGHT, EnemyState.BODY, "Body", 0.15f, Animation.PlayMode.LOOP);
        loadAnim(EnemyType.FALSE_KNIGHT, EnemyState.STUN_RECOVER, "Stun Recover", 0.1f, Animation.PlayMode.NORMAL);
        loadAnim(EnemyType.FALSE_KNIGHT, EnemyState.DEATH_LAND, "DeathLand", 0.1f, Animation.PlayMode.NORMAL);
        loadAnim(EnemyType.FALSE_KNIGHT, EnemyState.DEATH_AIR, "DeathFall", 0.1f, Animation.PlayMode.NORMAL);
        loadAnim(EnemyType.FALSE_KNIGHT, EnemyState.TURN, "Turn", 0.1f, Animation.PlayMode.NORMAL);
    }

    private void loadAnim(EnemyType type, EnemyState state, String regionName, float frameDuration, Animation.PlayMode mode) {
        TextureAtlas atlas = atlases.get(type);
        if (atlas != null && atlas.findRegions(regionName).size > 0) {
            Animation<TextureRegion> anim = new Animation<>(frameDuration, atlas.findRegions(regionName), mode);
            library.get(type).put(state, anim);
        }
    }

    public Animation<TextureRegion> getAnimation(EnemyType type, EnemyState state) {
        Map<EnemyState, Animation<TextureRegion>> enemyAnims = library.get(type);
        if (enemyAnims == null) return null;
        if (!enemyAnims.containsKey(state)) {
            if (enemyAnims.containsKey(EnemyState.IDLE)) return enemyAnims.get(EnemyState.IDLE);
            if (enemyAnims.containsKey(EnemyState.WALK)) return enemyAnims.get(EnemyState.WALK);
        }
        return enemyAnims.get(state);
    }

    public void dispose() {
        for (TextureAtlas atlas : atlases.values()) {
            atlas.dispose();
        }
        atlases.clear();
    }
}

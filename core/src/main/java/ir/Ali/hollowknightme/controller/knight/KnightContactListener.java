package ir.Ali.hollowknightme.controller.knight;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import ir.Ali.hollowknightme.enums.fixture.FixtureType;
import ir.Ali.hollowknightme.enums.knight.KnightState;
import ir.Ali.hollowknightme.enums.charm.CharmType;
import ir.Ali.hollowknightme.controller.map.DegradableWallManager;
import ir.Ali.hollowknightme.controller.game.GameStateManager;
import ir.Ali.hollowknightme.controller.game.GameRunManager;
import ir.Ali.hollowknightme.controller.cheats.CheatManager;
import ir.Ali.hollowknightme.model.interfaces.Damageable;
import ir.Ali.hollowknightme.model.knight.Knight;
import ir.Ali.hollowknightme.model.knight.Nail;
import ir.Ali.hollowknightme.model.map.GroundData;
import ir.Ali.hollowknightme.model.map.GroundType;
import ir.Ali.hollowknightme.model.map.MapData;
import ir.Ali.hollowknightme.controller.charm.CharmManager;
import ir.Ali.hollowknightme.controller.sound.AudioManager;
import ir.Ali.hollowknightme.enums.sound.SfxType;

public class KnightContactListener implements ContactListener {
    private final Knight knight;
    private final DegradableWallManager degradableWallManager;
    private final KnightManager knightManager;
    private GameRunManager gameRunManager;
    private MapData mapData;

    private enum SurfaceType { GROUND, WALL, CEILING, NONE }

    public KnightContactListener(KnightManager knightManager, DegradableWallManager degradableWallManager , MapData mapData) {
        this.knightManager = knightManager;
        this.degradableWallManager = degradableWallManager;
        this.knight = knightManager.getKnight();
        this.mapData = mapData;
    }

    public void setGameRunManager(GameRunManager gameRunManager) {
        this.gameRunManager = gameRunManager;
    }

    public void updateStates(World world) {
        if (knight == null || world == null) return;

        Body body = knight.getBody();
        if (body == null) {
            knight.setGrounded(false);
            knight.setOnWall(false);
            knight.setOnCeiling(false);
            return;
        }

        boolean isGrounded = false;
        boolean isOnWall = false;
        boolean isOnCeiling = false;

        Array<Contact> contacts = world.getContactList();
        for (int i = 0; i < contacts.size; i++) {
            Contact contact = contacts.get(i);
            if (contact == null || !contact.isTouching() || !contact.isEnabled()) continue;

            if (!isKnightInvolved(contact)) continue;

            Fixture fixA = contact.getFixtureA();
            Fixture fixB = contact.getFixtureB();

            if (fixA == null || fixB == null) continue;
            if (fixA.isSensor() || fixB.isSensor()) continue;
            if (fixA.getUserData() instanceof Nail || fixB.getUserData() instanceof Nail) continue;

            Fixture envFixture = getEnvironmentFixture(contact);
            if (envFixture == null) continue;

            SurfaceType surface = determineSurfaceType(contact, envFixture);
            if (surface == SurfaceType.GROUND) {
                isGrounded = true;
            } else if (surface == SurfaceType.WALL) {
                isOnWall = true;
            } else if (surface == SurfaceType.CEILING) {
                isOnCeiling = true;
            }
        }

        knight.setGrounded(isGrounded);
        if (isGrounded) {
            knight.setCanDoubleJump(true);
        }
        knight.setOnWall(isOnWall);
        knight.setOnCeiling(isOnCeiling);
    }

    @Override
    public void beginContact(Contact contact) {
        if (contact == null || !contact.isEnabled()) return;

        handleSpikeContact(contact);

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if (fixA == null || fixB == null) return;

        if (fixA.getUserData() instanceof Nail || fixB.getUserData() instanceof Nail) {
            handleNailContact(fixA, fixB);
        }
    }

    @Override
    public void endContact(Contact contact) {}

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}

    private void handleNailContact(Fixture fixA, Fixture fixB) {
        Fixture nailFix = fixA.getUserData() instanceof Nail ? fixA : fixB;
        Fixture targetFix = (fixA == nailFix) ? fixB : fixA;
        Nail nail = (Nail) nailFix.getUserData();

        if (!nail.isActive() || nail.isHasHit()) return;

        Object targetData = targetFix.getBody().getUserData();
        Object targetFixData = targetFix.getUserData();

        if (targetData == FixtureType.SPIKE) return;

        if (targetFixData == FixtureType.DEGRADABLE) {
            degradableWallManager.hit(targetFix.getBody());
            AudioManager.getInstance().playSfx(SfxType.NAIL);
            applyNailHitPhysics(nail);
            return;
        }

        if (targetData instanceof Damageable) {
            float calculatedDamage = nail.getDamage();
            if (CharmManager.getInstance().isCharmEquipped(CharmType.UNBREAKABLE_STRENGTH)) {
                calculatedDamage *= 2f;
            }
            ((Damageable) targetData).takeDamage(calculatedDamage);

            int soulGained = CharmManager.getInstance().isCharmEquipped(CharmType.SOUL_CATCHER) ? 22 : 11;
            knight.addSoul(soulGained);

            AudioManager.getInstance().playSfx(SfxType.ENEMY_DAMAGE);
            AudioManager.getInstance().playSfx(SfxType.SOUL);

            applyNailHitPhysics(nail);
            return;
        }

        if (targetFixData == FixtureType.SPIKE && nail.getAttackState() == KnightState.DOWN_SLASH) {
            nail.setHasHit(true);
            float bounceVelocity = CharmManager.getInstance().isCharmEquipped(CharmType.HEAVY_BLOW) ? 16f : 8f;
            knight.getBody().setLinearVelocity(knight.getBody().getLinearVelocity().x, bounceVelocity);
            knight.setCanDoubleJump(true);
            knight.setPogoImmunityTimer(0.15f);
            AudioManager.getInstance().playSfx(SfxType.NAIL);
        }
    }

    private void applyNailHitPhysics(Nail nail) {
        nail.setHasHit(true);
        boolean hasHeavyBlow = CharmManager.getInstance().isCharmEquipped(CharmType.HEAVY_BLOW);

        if (nail.getAttackState() == KnightState.DOWN_SLASH) {
            float bounceVelocity = hasHeavyBlow ? 16f : 8f;
            knight.getBody().setLinearVelocity(knight.getBody().getLinearVelocity().x, bounceVelocity);
            knight.setCanDoubleJump(true);
            knight.setPogoImmunityTimer(0.25f);
        } else {
            knight.applyRecoil();
            if (hasHeavyBlow) {
                float heavyRecoil = knight.isFacingRight() ? -3f : 3f;
                knight.getBody().applyLinearImpulse(new Vector2(heavyRecoil, 0), knight.getBody().getWorldCenter(), true);
            }
        }
    }

    private void handleSpikeContact(Contact contact) {
        if (!isKnightInvolved(contact)) return;
        if (CheatManager.getInstance().isGodMode()) return;

        GroundData groundData = null;
        Object dataA = contact.getFixtureA().getBody().getUserData();
        Object dataB = contact.getFixtureB().getBody().getUserData();

        if (dataA instanceof GroundData) {
            groundData = (GroundData) dataA;
        } else if (dataB instanceof GroundData) {
            groundData = (GroundData) dataB;
        }

        if (groundData == null || groundData.getType() != GroundType.SPIKE) return;
        if (knight.isImmuneToSpikes()) return;
        if (dataA instanceof Nail || dataB instanceof Nail) return;

        float knockbackDir = knight.isFacingRight() ? -1f : 1f;
        knight.takeDamage(1f, knockbackDir, 2f);
        AudioManager.getInstance().playSfx(SfxType.HERO_DAMAGE);

        MapData currentMapData = GameStateManager.getInstance().getCurrentRoom().getMapData();

        if (knight.getCurrentHealth() <= 0) {
            gameRunManager.playerDied();
        } else {
            knightManager.requestRespawn(currentMapData, false);
        }
    }

    private boolean isKnightInvolved(Contact contact) {
        if (knight == null || knight.getBody() == null) return false;
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();
        return bodyA == knight.getBody() || bodyB == knight.getBody();
    }

    private Fixture getEnvironmentFixture(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        Fixture knightFix = (fixA.getBody() == knight.getBody()) ? fixA : fixB;
        Fixture otherFix = (knightFix == fixA) ? fixB : fixA;

        if (isEnvironmentFixture(otherFix)) {
            return otherFix;
        }
        return null;
    }

    private boolean isEnvironmentFixture(Fixture fixture) {
        Object fData = fixture.getUserData();
        Object bData = fixture.getBody().getUserData();

        if (bData instanceof GroundData) {
            return ((GroundData) bData).getType() != GroundType.SPIKE;
        }

        if (fData == FixtureType.WALL || fData == FixtureType.GROUND || fData == FixtureType.PLATFORM) {
            return true;
        }

        if (bData instanceof String) {
            String s = (String) bData;
            return s.equals("floor") || s.equals("wall") || s.equals("ceiling");
        }
        return false;
    }

    private SurfaceType determineSurfaceType(Contact contact, Fixture envFixture) {
        Vector2 worldNormal = contact.getWorldManifold().getNormal();
        float normalX = worldNormal.x;
        float normalY = worldNormal.y;

        if (contact.getFixtureB() == envFixture) {
            normalX = -normalX;
            normalY = -normalY;
        }

        Object bData = envFixture.getBody().getUserData();
        Object fData = envFixture.getUserData();
        GroundData groundData = bData instanceof GroundData ? (GroundData) bData : null;

        if (groundData != null) {
            if (normalY > 0.5f && groundData.isActiveTop()) return SurfaceType.GROUND;
            if (normalY < -0.5f && groundData.isActiveBottom()) return SurfaceType.CEILING;
            if (Math.abs(normalX) > 0.5f) {
                if ((normalX > 0 && groundData.isActiveLeft()) || (normalX < 0 && groundData.isActiveRight())) {
                    return SurfaceType.WALL;
                }
            }
            return SurfaceType.NONE;
        }

        String legacyType = bData instanceof String ? (String) bData : null;
        if (legacyType != null) {
            if ("floor".equals(legacyType) && normalY > 0.5f) return SurfaceType.GROUND;
            if ("wall".equals(legacyType) && Math.abs(normalX) > 0.5f) return SurfaceType.WALL;
            if ("ceiling".equals(legacyType) && normalY < -0.5f) return SurfaceType.CEILING;
        }

        if (fData == FixtureType.GROUND) {
            if (normalY > 0.5f) return SurfaceType.GROUND;
            if (Math.abs(normalX) > 0.5f) return SurfaceType.WALL;
        }
        if (fData == FixtureType.PLATFORM && normalY > 0.5f) return SurfaceType.GROUND;
        if (fData == FixtureType.WALL && Math.abs(normalX) > 0.5f) return SurfaceType.WALL;

        if (normalY > 0.5f) return SurfaceType.GROUND;
        if (normalY < -0.5f) return SurfaceType.CEILING;
        if (Math.abs(normalX) > 0.5f) return SurfaceType.WALL;

        return SurfaceType.NONE;
    }
}

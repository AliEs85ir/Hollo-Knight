package ir.Ali.hollowknightme.controller.charm;

import com.badlogic.gdx.utils.Array;
import ir.Ali.hollowknightme.enums.charm.CharmType;

public class CharmManager {
    private static CharmManager instance;
    private final Array<CharmType> equippedCharms;
    private static final int MAX_NOTCHES = 3;

    private CharmManager() {
        equippedCharms = new Array<>();
    }

    public static CharmManager getInstance() {
        if (instance == null) {
            instance = new CharmManager();
        }
        return instance;
    }

    public boolean toggleCharm(CharmType charm) {
        if (equippedCharms.contains(charm, true)) {
            equippedCharms.removeValue(charm, true);
            return true;
        } else if (equippedCharms.size < MAX_NOTCHES) {
            equippedCharms.add(charm);
            return true;
        }
        return false;
    }

    public void equipAllCharmsCheat() {
        equippedCharms.clear();
        for (CharmType charm : CharmType.values()) {
            equippedCharms.add(charm);
        }
    }

    public boolean isCharmEquipped(CharmType charm) {
        return equippedCharms.contains(charm, true);
    }

    public int getEquippedCount() {
        return equippedCharms.size;
    }

    public int getMaxNotches() {
        return MAX_NOTCHES;
    }

    public void clear() {
        equippedCharms.clear();
    }
}

package ir.Ali.hollowknightme.service;

import ir.Ali.hollowknightme.model.save.GameSaveData;

public interface SaveService {
    void save(GameSaveData data);
    GameSaveData load(int slotNumber);
    boolean hasSaveSlot(int slotNumber);
    void deleteSlot(int slotNumber);
}

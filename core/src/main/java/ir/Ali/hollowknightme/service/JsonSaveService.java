package ir.Ali.hollowknightme.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import ir.Ali.hollowknightme.model.save.GameSaveData;
import ir.Ali.hollowknightme.service.SaveService;

public class JsonSaveService implements SaveService {
    private final Json json = new Json();

    private FileHandle getFile(int slot) {
        return Gdx.files.local("hollow_saves/slot_" + slot + ".json");
    }

    @Override
    public void save(GameSaveData data) {
        FileHandle file = getFile(data.getSlotNumber());
        file.writeString(json.prettyPrint(data), false);
    }

    @Override
    public GameSaveData load(int slotNumber) {
        FileHandle file = getFile(slotNumber);
        if (!file.exists()) return null;
        return json.fromJson(GameSaveData.class, file.readString());
    }

    @Override
    public boolean hasSaveSlot(int slotNumber) {
        return getFile(slotNumber).exists();
    }

    @Override
    public void deleteSlot(int slotNumber) {
        FileHandle file = getFile(slotNumber);
        if (file.exists()) {
            file.delete();
        }
    }
}

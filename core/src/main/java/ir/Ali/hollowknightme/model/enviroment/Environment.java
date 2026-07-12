package ir.Ali.hollowknightme.model.enviroment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Environment {
    private final String name;
    private final ArrayList<Room> rooms;
    private int currentRoomIndex;

    public Environment(String name) {
        this.name = name;
        this.rooms = new ArrayList<>();
        this.currentRoomIndex = 0;
    }

    public void addRoom(Room room) {
        if (room != null) {
            rooms.add(room);
        }
    }

    public Room getCurrentRoom() {
        if (isValidIndex(currentRoomIndex)) {
            return rooms.get(currentRoomIndex);
        }
        return null;
    }

    public void moveToNextRoom() {
        if (isValidIndex(currentRoomIndex + 1)) {
            currentRoomIndex++;
        }
    }

    public void moveToPreviousRoom() {
        if (isValidIndex(currentRoomIndex - 1)) {
            currentRoomIndex--;
        }
    }

    public void moveToRoom(int index) {
        if (isValidIndex(index)) {
            currentRoomIndex = index;
        }
    }

    private boolean isValidIndex(int index) {
        return index >= 0 && index < rooms.size();
    }

    public int getCurrentRoomIndex() { return currentRoomIndex; }
    public List<Room> getRooms() { return Collections.unmodifiableList(rooms); }
    public String getName() { return name; }

    public Room getNextRoom() {
        return rooms.get(currentRoomIndex+1);
    }

    public Room getPreviousRoom() {
        return rooms.get(currentRoomIndex-1);
    }
}

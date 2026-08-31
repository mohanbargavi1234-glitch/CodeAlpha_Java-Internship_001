import java.util.ArrayList;
import java.util.List;

/**
 * Represents the hotel itself: holds the full room inventory and
 * provides search/availability operations.
 */
public class Hotel {

    private final String hotelName;
    private final List<Room> rooms;

    public Hotel(String hotelName) {
        this.hotelName = hotelName;
        this.rooms = new ArrayList<>();
    }

    public String getHotelName() {
        return hotelName;
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public List<Room> getAllRooms() {
        return rooms;
    }

    /** Returns all currently available rooms, optionally filtered by category. */
    public List<Room> searchAvailableRooms(RoomCategory category) {
        List<Room> result = new ArrayList<>();
        for (Room r : rooms) {
            if (r.isAvailable() && (category == null || r.getCategory() == category)) {
                result.add(r);
            }
        }
        return result;
    }

    public Room findRoomByNumber(int roomNumber) {
        for (Room r : rooms) {
            if (r.getRoomNumber() == roomNumber) {
                return r;
            }
        }
        return null;
    }

    /** Populates the hotel with a default set of sample rooms. */
    public void initializeSampleRooms() {
        int roomNo = 101;
        for (int i = 0; i < 5; i++) addRoom(new Room(roomNo++, RoomCategory.STANDARD));
        roomNo = 201;
        for (int i = 0; i < 3; i++) addRoom(new Room(roomNo++, RoomCategory.DELUXE));
        roomNo = 301;
        for (int i = 0; i < 2; i++) addRoom(new Room(roomNo++, RoomCategory.SUITE));
    }
}

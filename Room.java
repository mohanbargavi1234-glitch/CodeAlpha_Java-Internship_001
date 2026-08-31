/**
 * Represents a single room in the hotel.
 */
public class Room {
    private final int roomNumber;
    private final RoomCategory category;
    private final double pricePerNight;
    private boolean available;

    public Room(int roomNumber, RoomCategory category) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.pricePerNight = category.getBasePricePerNight();
        this.available = true;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public RoomCategory getCategory() {
        return category;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return String.format("Room %-4d | %-8s | Rs.%.2f/night | %s",
                roomNumber, category, pricePerNight,
                available ? "AVAILABLE" : "BOOKED");
    }
}

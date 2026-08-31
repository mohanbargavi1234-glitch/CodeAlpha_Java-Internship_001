/**
 * Enum representing the different categories of rooms available in the hotel.
 * Each category has a base price per night.
 */
public enum RoomCategory {
    STANDARD(1500.0),
    DELUXE(2800.0),
    SUITE(5000.0);

    private final double basePricePerNight;

    RoomCategory(double basePricePerNight) {
        this.basePricePerNight = basePricePerNight;
    }

    public double getBasePricePerNight() {
        return basePricePerNight;
    }
}

/**
 * Represents a guest making a reservation.
 */
public class Guest {
    private final String guestId;
    private final String name;
    private final String contact;

    public Guest(String guestId, String name, String contact) {
        this.guestId = guestId;
        this.name = name;
        this.contact = contact;
    }

    public String getGuestId() {
        return guestId;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }

    @Override
    public String toString() {
        return name + " (ID: " + guestId + ", Contact: " + contact + ")";
    }
}

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Represents a single reservation made by a guest for a room.
 */
public class Reservation {

    public enum Status {
        CONFIRMED, CANCELLED
    }

    private final String reservationId;
    private final Guest guest;
    private final Room room;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    private final double totalCost;
    private Status status;
    private String paymentTransactionId;

    public Reservation(String reservationId, Guest guest, Room room,
                        LocalDate checkIn, LocalDate checkOut) {
        this.reservationId = reservationId;
        this.guest = guest;
        this.room = room;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (nights <= 0) nights = 1; // minimum 1 night
        this.totalCost = nights * room.getPricePerNight();
        this.status = Status.CONFIRMED;
    }

    public String getReservationId() {
        return reservationId;
    }

    public Guest getGuest() {
        return guest;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getPaymentTransactionId() {
        return paymentTransactionId;
    }

    public void setPaymentTransactionId(String paymentTransactionId) {
        this.paymentTransactionId = paymentTransactionId;
    }

    public long getNights() {
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        return nights <= 0 ? 1 : nights;
    }

    /** Serializes this reservation to a single CSV line for file persistence. */
    public String toCsv() {
        return String.join(",",
                reservationId,
                guest.getGuestId(),
                guest.getName(),
                guest.getContact(),
                String.valueOf(room.getRoomNumber()),
                room.getCategory().name(),
                checkIn.toString(),
                checkOut.toString(),
                String.format("%.2f", totalCost),
                status.name(),
                paymentTransactionId == null ? "NA" : paymentTransactionId
        );
    }

    @Override
    public String toString() {
        return String.format(
                "Reservation ID: %s%nGuest: %s%nRoom: %d (%s)%nFrom : %s%nTo: %s%nDuration: %d night(s)%nTotal Amount: Rs.%.2f%nStatus: %s",
                reservationId, guest.getName(), room.getRoomNumber(), room.getCategory(),
                checkIn, checkOut, getNights(), totalCost, status);
    }
}

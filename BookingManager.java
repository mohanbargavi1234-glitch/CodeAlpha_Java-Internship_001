import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the lifecycle of reservations: creation, cancellation,
 * payment, and persistence to file. This is the "engine" class that
 * ties Hotel, Guest, Room and Reservation together.
 */
public class BookingManager {

    private final Hotel hotel;
    private final List<Reservation> reservations;
    private int reservationCounter;

    public BookingManager(Hotel hotel) {
        this.hotel = hotel;
        this.reservations = FileStorage.loadAll(hotel);
        this.reservationCounter = reservations.size();
    }

    /**
     * Books a room for a guest, simulates payment, and persists the result.
     * Returns the created Reservation, or null if the room isn't available
     * or payment fails.
     */
    public Reservation bookRoom(Guest guest, int roomNumber, LocalDate checkIn,
                                 LocalDate checkOut, String paymentMethod) {
        Room room = hotel.findRoomByNumber(roomNumber);
        if (room == null) {
            System.out.println("Room #" + roomNumber + " does not exist.");
            return null;
        }
        if (!room.isAvailable()) {
            System.out.println("Room #" + roomNumber + " is currently not available.");
            return null;
        }
        if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
            System.out.println("Check-out date must be after check-in date.");
            return null;
        }

        reservationCounter++;
        String reservationId = "RES" + String.format("%04d", reservationCounter);
        Reservation reservation = new Reservation(reservationId, guest, room, checkIn, checkOut);

        PaymentSimulator.PaymentResult payment =
                PaymentSimulator.processPayment(reservation.getTotalCost(), paymentMethod);

        System.out.println(payment.message);
        if (!payment.success) {
            reservationCounter--; // roll back the counter, booking did not happen
            return null;
        }

        reservation.setPaymentTransactionId(payment.transactionId);
        room.setAvailable(false);
        reservations.add(reservation);
        FileStorage.saveAll(reservations);
        return reservation;
    }

    /** Cancels an existing confirmed reservation and frees up the room. */
    public boolean cancelReservation(String reservationId) {
        for (Reservation r : reservations) {
            if (r.getReservationId().equalsIgnoreCase(reservationId)) {
                if (r.getStatus() == Reservation.Status.CANCELLED) {
                    System.out.println("Reservation is already cancelled.");
                    return false;
                }
                r.setStatus(Reservation.Status.CANCELLED);
                r.getRoom().setAvailable(true);
                FileStorage.saveAll(reservations);
                return true;
            }
        }
        System.out.println("Reservation ID not found.");
        return false;
    }

    public List<Reservation> getAllReservations() {
        return reservations;
    }

    public List<Reservation> getReservationsForGuest(String guestId) {
        List<Reservation> result = new ArrayList<>();
        for (Reservation r : reservations) {
            if (r.getGuest().getGuestId().equalsIgnoreCase(guestId)) {
                result.add(r);
            }
        }
        return result;
    }

    public Reservation findReservation(String reservationId) {
        for (Reservation r : reservations) {
            if (r.getReservationId().equalsIgnoreCase(reservationId)) {
                return r;
            }
        }
        return null;
    }
}

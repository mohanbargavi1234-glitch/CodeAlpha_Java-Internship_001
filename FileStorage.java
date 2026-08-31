import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving and loading reservation data to/from a local text file
 * (simple CSV format) so bookings persist between program runs.
 */
public class FileStorage {

    private static final String FILE_PATH = "reservations.txt";
    private static final String HEADER =
            "reservationId,guestId,guestName,contact,roomNumber,category,checkIn,checkOut,totalCost,status,transactionId";

    /** Overwrites the file with the full current list of reservations. */
    public static void saveAll(List<Reservation> reservations) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))) {
            pw.println(HEADER);
            for (Reservation r : reservations) {
                pw.println(r.toCsv());
            }
        } catch (IOException e) {
            System.out.println("Error saving reservations: " + e.getMessage());
        }
    }

    /**
     * Loads reservations from file. Requires access to the Hotel so room
     * objects can be reattached and marked unavailable for CONFIRMED bookings.
     */
    public static List<Reservation> loadAll(Hotel hotel) {
        List<Reservation> reservations = new ArrayList<>();
        Path path = Paths.get(FILE_PATH);
        if (!Files.exists(path)) {
            return reservations; // no saved data yet
        }
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(",", -1);
                String reservationId = parts[0];
                String guestId = parts[1];
                String guestName = parts[2];
                String contact = parts[3];
                int roomNumber = Integer.parseInt(parts[4]);
                LocalDate checkIn = LocalDate.parse(parts[6]);
                LocalDate checkOut = LocalDate.parse(parts[7]);
                Reservation.Status status = Reservation.Status.valueOf(parts[9]);
                String txnId = parts[10].equals("NA") ? null : parts[10];

                Room room = hotel.findRoomByNumber(roomNumber);
                if (room == null) continue; // room no longer exists, skip

                Guest guest = new Guest(guestId, guestName, contact);
                Reservation res = new Reservation(reservationId, guest, room, checkIn, checkOut);
                res.setStatus(status);
                res.setPaymentTransactionId(txnId);
                reservations.add(res);

                if (status == Reservation.Status.CONFIRMED) {
                    room.setAvailable(false);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading reservations: " + e.getMessage());
        }
        return reservations;
    }
}

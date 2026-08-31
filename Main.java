import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Entry point: console-based menu for the Hotel Reservation System.
 */
public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static Hotel hotel;
    private static BookingManager bookingManager;

    public static void main(String[] args) {
        hotel = new Hotel("AirBNB Grand Hotel");
        hotel.initializeSampleRooms();
        bookingManager = new BookingManager(hotel);

        System.out.println("===========================================================================");
        System.out.println(" Welcome to " + hotel.getHotelName());
        System.out.println("===========================================================================");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1": searchRooms(); break;
                case "2": bookRoom(); break;
                case "3": cancelReservation(); break;
                case "4": viewAllReservations(); break;
                case "5": viewReservationDetails(); break;
                case "6": running = false; break;
                default: System.out.println("Invalid choice. Try again.");
            }
        }
        System.out.println("Thank you for using " + hotel.getHotelName() + ". Feel Free To Visit Again ");
    }

    private static void printMenu() {
        System.out.println("\n--------------------------------- MENU ---------------------------------");
        System.out.println("1. Search available rooms");
        System.out.println("2. Book a room");
        System.out.println("3. Cancel a reservation");
        System.out.println("4. View all reservations");
        System.out.println("5. View reservation details (with payment receipt)");
        System.out.println("6. Exit");
        System.out.print("Enter choice: ");
    }

    private static void searchRooms() {
        System.out.println("\nFilter by category? \n(1) Standard \n(2) Deluxe \n(3) Suite \n(4) All");
        System.out.print("Choice: ");
        String c = sc.nextLine().trim();
        RoomCategory category = switch (c) {
            case "1" -> RoomCategory.STANDARD;
            case "2" -> RoomCategory.DELUXE;
            case "3" -> RoomCategory.SUITE;
            default -> null;
        };
        List<Room> available = hotel.searchAvailableRooms(category);
        if (available.isEmpty()) {
            System.out.println("No available rooms found for that category.");
        } else {
            System.out.println("\nAvailable rooms:");
            for (Room r : available) {
                System.out.println("  " + r);
            }
        }
    }

    private static void bookRoom() {
        try {
            System.out.print("Enter your name: ");
            String name = sc.nextLine().trim();
            System.out.print("Enter contact number: ");
            String contact = sc.nextLine().trim();
            String guestId = "G" + Math.abs((name + contact).hashCode() % 100000);
            Guest guest = new Guest(guestId, name, contact);

            System.out.print("Enter room number to book (see 'Search available rooms'): ");
            int roomNumber = Integer.parseInt(sc.nextLine().trim());

            LocalDate checkIn = readDate("Enter check-in date (YYYY-MM-DD): ");
            LocalDate checkOut = readDate("Enter check-out date (YYYY-MM-DD): ");

            System.out.print("Enter payment method (CARD/UPI/CASH): ");
            String method = sc.nextLine().trim();

            Reservation reservation = bookingManager.bookRoom(guest, roomNumber, checkIn, checkOut, method);
            if (reservation != null) {
                System.out.println("\nBooking confirmed!");
                System.out.println("Reservation ID: " + reservation.getReservationId());
                System.out.println("Guest: " + reservation.getGuest().getName());
                System.out.println("Room: " + reservation.getRoom().getRoomNumber() + " (" + reservation.getRoom().getCategory() + ")");
                System.out.println("From : " + reservation.getCheckIn());
                System.out.println("To: " + reservation.getCheckOut());
                System.out.println("Duration: " + reservation.getNights() + " night(s)");
                System.out.println("Total Amount: Rs." + String.format("%.2f", reservation.getTotalCost()));
                System.out.println("Status: " + reservation.getStatus());
                System.out.println("Transaction ID: " + reservation.getPaymentTransactionId());
            } else {
                System.out.println("Booking could not be completed.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid room number entered.");
        }
    }

    private static LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid format. Please use YYYY-MM-DD.");
            }
        }
    }

    private static void cancelReservation() {
        System.out.print("Enter reservation ID to cancel: ");
        String id = sc.nextLine().trim();
        boolean cancelled = bookingManager.cancelReservation(id);
        if (cancelled) {
            System.out.println("Reservation " + id + " has been cancelled. Room is now available again.");
        }
    }

    private static void viewAllReservations() {
        List<Reservation> all = bookingManager.getAllReservations();
        if (all.isEmpty()) {
            System.out.println("No reservations found.");
            return;
        }
        System.out.println("\nAll reservations:");
        for (Reservation r : all) {
            System.out.println("Reservation Details");
            System.out.println("Reservation No.: " + r.getReservationId());
            System.out.println("Guest: " + r.getGuest().getName());
            System.out.println("Room: " + r.getRoom().getRoomNumber() + " (" + r.getRoom().getCategory() + ")");
            System.out.println("From: " + r.getCheckIn());
            System.out.println("To: " + r.getCheckOut());
            System.out.println("Duration: " + r.getNights() + " night(s)");
            System.out.println("Total Amount : Rs." + String.format("%.2f", r.getTotalCost()));
            System.out.println("Status: " + r.getStatus());
        }
    }

    private static void viewReservationDetails() {
        System.out.print("Enter reservation ID: ");
        String id = sc.nextLine().trim();
        Reservation r = bookingManager.findReservation(id);
        if (r == null) {
            System.out.println("Reservation not found.");
            return;
        }
        System.out.println("\n----- Reservation Receipt -----");
        System.out.println("Reservation ID : " + r.getReservationId());
        System.out.println("Guest          : " + r.getGuest());
        System.out.println("Room           : #" + r.getRoom().getRoomNumber() + " (" + r.getRoom().getCategory() + ")");
        System.out.println("Check-in       : " + r.getCheckIn());
        System.out.println("Check-out      : " + r.getCheckOut());
        System.out.println("Nights         : " + r.getNights());
        System.out.println("Total Cost     : Rs." + String.format("%.2f", r.getTotalCost()));
        System.out.println("Status         : " + r.getStatus());
        System.out.println("Transaction ID : " + (r.getPaymentTransactionId() == null ? "N/A" : r.getPaymentTransactionId()));
        System.out.println("--------------------------------");
    }
}

# Hotel Reservation System (Java)

A console-based Hotel Reservation System built with OOP principles and file I/O persistence.

## Features
- Room categorization: Standard, Deluxe, Suite (each with its own pricing)
- Search available rooms by category
- Book a room (captures guest details, check-in/check-out dates)
- Payment simulation (95% success rate, generates a transaction ID)
- Cancel an existing reservation (frees up the room again)
- View all reservations / view a detailed receipt for one reservation
- All bookings persist to `reservations.txt` (CSV format) so data survives
  across program runs

## Class design (OOP)
| Class              | Responsibility                                          |
|---------------------|----------------------------------------------------------|
| `RoomCategory`      | Enum: STANDARD / DELUXE / SUITE with base pricing        |
| `Room`              | A single hotel room (number, category, availability)     |
| `Guest`             | A customer making a booking                               |
| `Reservation`       | A booking record (dates, cost, status, CSV serialization)|
| `PaymentSimulator`  | Simulates a payment gateway                                |
| `Hotel`             | Holds room inventory, handles search                       |
| `FileStorage`       | Reads/writes reservations.txt for persistence               |
| `BookingManager`    | Core engine: book / cancel / query reservations             |
| `Main`              | Console menu tying everything together                       |

## How to compile & run
```bash
cd HotelReservationSystem/src
javac *.java
java Main
```

## Sample flow
1. Choose `1` to search available rooms (filter by category or view all)
2. Choose `2` to book — enter your name, contact, room number, check-in/out
   dates (YYYY-MM-DD), and payment method
3. Choose `5` to view the receipt for a reservation ID (e.g. `RES0001`)
4. Choose `3` to cancel a reservation — the room becomes available again
5. Choose `4` anytime to see the full reservation list

## Notes / possible extensions
- Currently availability is a simple boolean flag (a room is either free or
  booked) rather than tracking overlapping date ranges — good enough for a
  demo/academic project, but a real system would check date-range conflicts.
- Swap `FileStorage` for a JDBC/SQLite-backed class if the assignment wants
  a real database instead of flat-file storage — the rest of the code
  wouldn't need to change since `BookingManager` only talks to `FileStorage`.
- Could add a Swing or JavaFX GUI on top of `BookingManager` without
  touching the core logic at all.

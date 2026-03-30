import java.util.*;

public class App {
    public static void main(String[] args) {
        BookingRequestQueue queue = new BookingRequestQueue();

        queue.addRequest(new Reservation("Abhi", "Single", "R001"));
        queue.addRequest(new Reservation("Subha", "Single", "R002"));
        queue.addRequest(new Reservation("Vanmathi", "Suite", "R003"));

        RoomInventory inventory = new RoomInventory();
        inventory.addRooms("Single", 2);
        inventory.addRooms("Suite", 1);

        RoomAllocator allocator = new RoomAllocator(inventory);
        BookingHistory history = new BookingHistory();

        System.out.println("Room Allocation Processing");

        while (!queue.isEmpty()) {
            Reservation r = queue.processRequest();
            String roomId = allocator.allocateRoom(r.getRoomType());

            if (roomId != null) {
                System.out.println("Booking confirmed for Guest: "
                        + r.getGuestName() + ", Room ID: " + roomId);

                history.addBooking(r, roomId);
            }
        }

        System.out.println("\nBooking History Report");
        BookingReportService reportService = new BookingReportService(history);
        reportService.generateReport();
    }
}

class Reservation {
    private String guestName;
    private String roomType;
    private String reservationId;

    public Reservation(String guestName, String roomType, String reservationId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.reservationId = reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getReservationId() {
        return reservationId;
    }
}

class BookingRequestQueue {
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
    }

    public Reservation processRequest() {
        return requestQueue.poll();
    }

    public boolean isEmpty() {
        return requestQueue.isEmpty();
    }
}

class RoomInventory {
    private Map<String, Integer> availability = new HashMap<>();

    public void addRooms(String type, int count) {
        availability.put(type, count);
    }

    public int getAvailable(String type) {
        return availability.getOrDefault(type, 0);
    }

    public void decrease(String type) {
        availability.put(type, getAvailable(type) - 1);
    }
}

class RoomAllocator {
    private RoomInventory inventory;
    private Map<String, Integer> counters = new HashMap<>();
    private Set<String> usedIds = new HashSet<>();

    public RoomAllocator(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public String allocateRoom(String type) {
        if (inventory.getAvailable(type) <= 0) return null;

        int count = counters.getOrDefault(type, 0) + 1;
        String roomId = type + "-" + count;

        if (usedIds.contains(roomId)) return null;

        usedIds.add(roomId);
        counters.put(type, count);
        inventory.decrease(type);

        return roomId;
    }
}

class BookingHistory {
    private List<String> confirmedBookings = new ArrayList<>();

    public void addBooking(Reservation reservation, String roomId) {
        confirmedBookings.add("Reservation ID: " + roomId +
                ", Guest: " + reservation.getGuestName() +
                ", Room Type: " + reservation.getRoomType());
    }

    public List<String> getBookings() {
        return confirmedBookings;
    }
}

class BookingReportService {
    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    public void generateReport() {
        for (String booking : history.getBookings()) {
            System.out.println(booking);
        }
        System.out.println("Total Bookings: " + history.getBookings().size());
    }
}

import java.util.*;

public class App {
    public static void main(String[] args) {
        RoomInventory inventory = new RoomInventory();
        inventory.addRooms("Single", 6);

        RoomAllocator allocator = new RoomAllocator(inventory);
        BookingHistory history = new BookingHistory();
        CancellationService cancellationService = new CancellationService(inventory, history);

        Reservation r1 = new Reservation("Abhi", "Single", "R001");
        String roomId1 = allocator.allocateRoom(r1.getRoomType());
        if (roomId1 != null) {
            history.addBooking(r1, roomId1);
        }

        System.out.println("Booking Cancellation");
        try {
            cancellationService.cancelBooking(roomId1, r1.getRoomType());
        } catch (Exception e) {
            System.out.println("Cancellation failed: " + e.getMessage());
        }
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

class RoomInventory {
    private Map<String, Integer> availability = new HashMap<>();

    public void addRooms(String type, int count) {
        availability.put(type, count);
    }

    public int getAvailable(String type) {
        return availability.getOrDefault(type, 0);
    }

    public void increase(String type) {
        availability.put(type, getAvailable(type) + 1);
    }

    public void decrease(String type) {
        int current = getAvailable(type);
        if (current <= 0) throw new IllegalStateException("No rooms available for type: " + type);
        availability.put(type, current - 1);
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

    public boolean hasBooking(String roomId) {
        return confirmedBookings.stream().anyMatch(b -> b.contains(roomId));
    }

    public void removeBooking(String roomId) {
        confirmedBookings.removeIf(b -> b.contains(roomId));
    }
}

class CancellationService {
    private RoomInventory inventory;
    private BookingHistory history;
    private Stack<String> rollbackStack = new Stack<>();

    public CancellationService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    public void cancelBooking(String roomId, String roomType) throws Exception {
        if (!history.hasBooking(roomId)) {
            throw new Exception("Reservation does not exist or already cancelled.");
        }
        history.removeBooking(roomId);
        rollbackStack.push(roomId);
        inventory.increase(roomType);

        System.out.println("Booking cancelled successfully. Inventory restored for room type: " + roomType);
        System.out.println("Rollback History (Most Recent First): Released Reservation ID: " + rollbackStack.peek());
        System.out.println("Updated " + roomType + " Room Availability: " + inventory.getAvailable(roomType));
    }
}

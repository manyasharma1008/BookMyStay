import java.util.*;

public class App {

    public static void main(String[] args) {

        BookingRequestQueue queue = new BookingRequestQueue();

        queue.addRequest(new Reservation("Abhi", "Single"));
        queue.addRequest(new Reservation("Subha", "Single"));
        queue.addRequest(new Reservation("Vanmathi", "Suite"));

        RoomInventory inventory = new RoomInventory();
        inventory.addRooms("Single", 2);
        inventory.addRooms("Suite", 1);

        RoomAllocator allocator = new RoomAllocator(inventory);

        System.out.println("Room Allocation Processing");

        while (!queue.isEmpty()) {
            Reservation r = queue.processRequest();
            String roomId = allocator.allocateRoom(r.getRoomType());

            if (roomId != null) {
                System.out.println("Booking confirmed for Guest: " 
                    + r.getGuestName() + ", Room ID: " + roomId);
            }
        }
    }
}

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
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
    private Map<String, Set<String>> allocatedRooms = new HashMap<>();
    private Set<String> usedIds = new HashSet<>();
    private Map<String, Integer> counters = new HashMap<>();

    public RoomAllocator(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public String allocateRoom(String type) {

        if (inventory.getAvailable(type) <= 0) return null;

        int count = counters.getOrDefault(type, 0) + 1;
        String roomId = type + "-" + count;

        if (usedIds.contains(roomId)) return null;

        usedIds.add(roomId);

        allocatedRooms.putIfAbsent(type, new HashSet<>());
        allocatedRooms.get(type).add(roomId);

        counters.put(type, count);
        inventory.decrease(type);

        return roomId;
    }
}
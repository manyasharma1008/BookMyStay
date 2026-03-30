import java.util.*;

public class App {
    public static void main(String[] args) {
        RoomInventory inventory = new RoomInventory();
        inventory.addRooms("Single", 5);
        inventory.addRooms("Double", 3);
        inventory.addRooms("Suite", 2);

        BookingRequestQueue queue = new BookingRequestQueue();
        queue.addRequest(new Reservation("Abhi", "Single", "R001"));
        queue.addRequest(new Reservation("Vanmathi", "Double", "R002"));
        queue.addRequest(new Reservation("Kural", "Suite", "R003"));
        queue.addRequest(new Reservation("Subha", "Single", "R004"));

        RoomAllocator allocator = new RoomAllocator(inventory);

        Thread t1 = new Thread(new BookingProcessor(queue, allocator));
        Thread t2 = new Thread(new BookingProcessor(queue, allocator));

        System.out.println("Concurrent Booking Simulation");
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nRemaining Inventory:");
        System.out.println("Single: " + inventory.getAvailable("Single"));
        System.out.println("Double: " + inventory.getAvailable("Double"));
        System.out.println("Suite: " + inventory.getAvailable("Suite"));
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

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public String getReservationId() { return reservationId; }
}

class BookingRequestQueue {
    private Queue<Reservation> requestQueue = new LinkedList<>();

    public synchronized void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
    }

    public synchronized Reservation processRequest() {
        return requestQueue.poll();
    }

    public synchronized boolean isEmpty() {
        return requestQueue.isEmpty();
    }
}

class RoomInventory {
    private Map<String, Integer> availability = new HashMap<>();

    public synchronized void addRooms(String type, int count) {
        availability.put(type, count);
    }

    public synchronized int getAvailable(String type) {
        return availability.getOrDefault(type, 0);
    }

    public synchronized void decrease(String type) {
        int current = getAvailable(type);
        if (current > 0) {
            availability.put(type, current - 1);
        }
    }
}

class RoomAllocator {
    private RoomInventory inventory;
    private Map<String, Integer> counters = new HashMap<>();
    private Set<String> usedIds = new HashSet<>();

    public RoomAllocator(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public synchronized String allocateRoom(String type) {
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

class BookingProcessor implements Runnable {
    private BookingRequestQueue queue;
    private RoomAllocator allocator;

    public BookingProcessor(BookingRequestQueue queue, RoomAllocator allocator) {
        this.queue = queue;
        this.allocator = allocator;
    }

    @Override
    public void run() {
        while (!queue.isEmpty()) {
            Reservation r = queue.processRequest();
            if (r != null) {
                String roomId = allocator.allocateRoom(r.getRoomType());
                if (roomId != null) {
                    System.out.println("Booking confirmed for Guest: " + r.getGuestName() + ", Room ID: " + roomId);
                }
            }
        }
    }
}

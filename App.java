import java.util.*;

public class App {
    public static void main(String[] args) {
        RoomInventory inventory = new RoomInventory();
        inventory.addRooms("Single", 2);

        RoomAllocator allocator = new RoomAllocator(inventory);
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        Reservation r = new Reservation("Abhi", "Single", "R001");
        String roomId = allocator.allocateRoom(r.getRoomType());

        if (roomId != null) {
            serviceManager.addService(roomId, new Service("Spa Access", 1500));

            double totalAdditionalCost = serviceManager.calculateAdditionalCost(roomId);

            System.out.println("Add-On Service Selection");
            System.out.println("Reservation ID: " + roomId);
            System.out.println("Total Add-On Cost: " + totalAdditionalCost);
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

class Service {
    private String name;
    private double cost;

    public Service(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }
}

class AddOnServiceManager {
    private Map<String, List<Service>> reservationServices = new HashMap<>();

    public void addService(String reservationId, Service service) {
        reservationServices.putIfAbsent(reservationId, new ArrayList<>());
        reservationServices.get(reservationId).add(service);
    }

    public double calculateAdditionalCost(String reservationId) {
        return reservationServices.getOrDefault(reservationId, Collections.emptyList())
                .stream()
                .mapToDouble(Service::getCost)
                .sum();
    }
}

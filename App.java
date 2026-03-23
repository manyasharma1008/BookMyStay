import java.util.HashMap;
import java.util.Map;

public class App {

    public static abstract class Room {
        protected int numberOfBeds;
        protected int squareFeet;
        protected double pricePerNight;

        public Room(int numberOfBeds, int squareFeet, double pricePerNight) {
            this.numberOfBeds = numberOfBeds;
            this.squareFeet = squareFeet;
            this.pricePerNight = pricePerNight;
        }

        public void displayRoomDetails() {
            System.out.println("Beds: " + numberOfBeds);
            System.out.println("Size: " + squareFeet + " sqft");
            System.out.println("Price per night: " + pricePerNight);
        }
    }

    public static class SingleRoom extends Room {
        public SingleRoom() {
            super(1, 250, 1500.0);
        }
    }

    public static class DoubleRoom extends Room {
        public DoubleRoom() {
            super(2, 400, 2500.0);
        }
    }

    public static class Suite extends Room {
        public Suite() {
            super(3, 750, 5000.0);
        }
    }

    public static class RoomInventory {
        private Map<String, Integer> roomAvailability;

        public RoomInventory() {
            roomAvailability = new HashMap<>();
        }

        public void initializeInventory(String roomType, int available) {
            roomAvailability.put(roomType, available);
        }

        public Map<String, Integer> getRoomAvailability() {
            return roomAvailability;
        }
    }

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        inventory.initializeInventory("Single Room", 5);
        inventory.initializeInventory("Double Room", 3);
        inventory.initializeInventory("Suite Room", 2);

        System.out.println("Room Search\n");

        Map<String, Integer> data = inventory.getRoomAvailability();

        if (data.get("Single Room") > 0) {
            System.out.println("Single Room:");
            SingleRoom s = new SingleRoom();
            s.displayRoomDetails();
            System.out.println("Available: " + data.get("Single Room") + "\n");
        }

        if (data.get("Double Room") > 0) {
            System.out.println("Double Room:");
            DoubleRoom d = new DoubleRoom();
            d.displayRoomDetails();
            System.out.println("Available: " + data.get("Double Room") + "\n");
        }

        if (data.get("Suite Room") > 0) {
            System.out.println("Suite Room:");
            Suite suite = new Suite();
            suite.displayRoomDetails();
            System.out.println("Available: " + data.get("Suite Room"));
        }
    }
}
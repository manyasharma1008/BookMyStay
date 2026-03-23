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
            System.out.println("Number of Beds: " + numberOfBeds);
            System.out.println("SquareFeet: " + squareFeet);
            System.out.println("Price per Night: " + pricePerNight);
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

        public void updateAvailability(String roomType, int count) {
            roomAvailability.put(roomType, count);
        }
    }

    public static void main(String[] args) {
        System.out.println("Hotel Room Inventory Status");
        
        System.out.println("Single Room:");
        SingleRoom singleRoom = new SingleRoom();
        singleRoom.displayRoomDetails();

        RoomInventory inventory = new RoomInventory();
        inventory.initializeInventory("Single Room", 5);
        System.out.println(inventory.getRoomAvailability());

        System.out.println("\nDouble Room:");
        DoubleRoom doubleRoom = new DoubleRoom();
        doubleRoom.displayRoomDetails();
        inventory.initializeInventory("Double Room", 3);
        System.out.println(inventory.getRoomAvailability());

        System.out.println("\nSuite:");
        Suite suite = new Suite();
        suite.displayRoomDetails();
        inventory.initializeInventory("Suite", 2);
        System.out.println(inventory.getRoomAvailability());
    }
}

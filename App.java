import java.io.*;
import java.util.*;

public class App {
    public static void main(String[] args) {
        RoomInventory inventory = PersistenceService.loadInventory("inventory.dat");

        System.out.println("System Recovery");
        if (inventory == null) {
            System.out.println("No valid inventory data found. Starting fresh.\n");
            inventory = new RoomInventory();
            inventory.addRooms("Single", 5);
            inventory.addRooms("Double", 3);
            inventory.addRooms("Suite", 2);
        }

        System.out.println("Current Inventory:");
        System.out.println("Single: " + inventory.getAvailable("Single"));
        System.out.println("Double: " + inventory.getAvailable("Double"));
        System.out.println("Suite: " + inventory.getAvailable("Suite"));

        PersistenceService.saveInventory(inventory, "inventory.dat");
        System.out.println("Inventory saved successfully.");
    }
}

class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Integer> availability = new HashMap<>();

    public void addRooms(String type, int count) {
        availability.put(type, count);
    }

    public int getAvailable(String type) {
        return availability.getOrDefault(type, 0);
    }
}

class PersistenceService {
    public static void saveInventory(RoomInventory inventory, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(inventory);
        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }

    public static RoomInventory loadInventory(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (RoomInventory) ois.readObject();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading inventory: " + e.getMessage());
            return null;
        }
    }
}

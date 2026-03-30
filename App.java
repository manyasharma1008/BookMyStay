import java.util.*;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Booking Validation");
        System.out.print("Enter guest name: ");
        String guestName = sc.nextLine();

        System.out.print("Enter room type (Single/Double/Suite): ");
        String roomType = sc.nextLine();

        try {
            InvalidBookingValidator.validateRoomType(roomType);
            System.out.println("Booking confirmed for Guest: " + guestName + ", Room Type: " + roomType);
        } catch (InvalidBookingException e) {
            System.out.println("Booking failed: " + e.getMessage());
        }

        sc.close();
    }
}

class InvalidBookingValidator {
    public static void validateRoomType(String roomType) throws InvalidBookingException {
        List<String> validTypes = Arrays.asList("Single", "Double", "Suite");
        if (!validTypes.contains(roomType)) {
            throw new InvalidBookingException("Invalid room type selected.");
        }
    }
}

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

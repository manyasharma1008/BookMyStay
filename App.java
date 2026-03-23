public class App {
    public static  abstract class Room{
        protected int  numberOfBeds;
        protected int squareFeet;
        protected double pricePerNight;

        public Room(int numberOfBeds , int squareFeet, double pricePerNight){
            this.numberOfBeds = numberOfBeds;
            this.squareFeet = squareFeet;
            this.pricePerNight = pricePerNight;
        }

        public void displayRoomDetails(){

            System.out.println("Number of Beds: " + numberOfBeds);
            System.out.println("SquareFeet: " + squareFeet);
            System.out.println("Price per Night: " + pricePerNight);
        }

    }

    public static class SingleRoom extends Room{

        public SingleRoom(){
            super(1,250, 1500.0);
        }

    }

    public static class DoubleRoom extends Room{
        public DoubleRoom(){super(2, 400, 2500.0);}

    }

    public static class Suite extends Room{
        public Suite(){
            super(3, 750, 5000.0);
        }
    }


    public static void main(String[] args){
        System.out.println("Single Room:");
        SingleRoom r1 = new SingleRoom();
        r1.displayRoomDetails();
        System.out.println("Double Room:");
        DoubleRoom r2 = new DoubleRoom();
        r2.displayRoomDetails();
        System.out.println("Suite:");
        Suite r3 = new Suite();
        r3.displayRoomDetails();
    }
}


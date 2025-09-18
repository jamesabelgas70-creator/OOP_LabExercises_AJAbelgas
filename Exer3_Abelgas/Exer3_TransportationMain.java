

public class Exer3_TransportationMain {
    public static void main(String[] args) {
        // Air Transport
        Exer3_Helicopter heli = new Exer3_Helicopter("Apache Helicopter", 5);
        Exer3_Airplane plane = new Exer3_Airplane("Boeing 747", 416);
        Exer3_SpaceShuttle shuttle = new Exer3_SpaceShuttle("Discovery", 7);

        // Land Transport
        Exer3_Truck truck = new Exer3_Truck("Freight Truck", 2);
        Exer3_SUV suv = new Exer3_SUV("Toyota Fortuner", 7);
        Exer3_Tricycle tricycle = new Exer3_Tricycle("Honda Tricycle", 3);
        Exer3_Motorcycle motorcycle = new Exer3_Motorcycle("Yamaha Motorbike", 2);
        Exer3_Kariton kariton = new Exer3_Kariton("Traditional Kariton", 4);

        // Water Transport
        Exer3_Boat boat = new Exer3_Boat("Fishing Boat", 10);
        Exer3_Submarine submarine = new Exer3_Submarine("Nautilus", 15);

        // Display all
        System.out.println("---- Air Transport ----");
        heli.displayInfo();
        plane.displayInfo();
        shuttle.displayInfo();

        System.out.println("\n---- Land Transport ----");
        truck.displayInfo();
        suv.displayInfo();
        tricycle.displayInfo();
        motorcycle.displayInfo();
        kariton.displayInfo();

        System.out.println("\n---- Water Transport ----");
        boat.displayInfo();
        submarine.displayInfo();
    }
}
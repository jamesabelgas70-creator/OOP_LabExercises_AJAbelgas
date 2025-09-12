public class Exer2_CarTester{
  
    public static void main(String[] args){
        Exer2_Car c1 = Exer2_Car.createToyota();
        Exer2_Car c2 = Exer2_Car.createHonda();
        Exer2_Car c3 = Exer2_Car.createToyotaVan();

        c1.displayDetails();
        c2.displayDetails();   
        c3.displayDetails();
    }
}
public class Exer2_Car {
    private String Brand;
    private String model;  
    private String color;
    private String ChasissNumber;
    private String PlateNumber;   


    public static Exer2_Car createToyota() {
        return new Exer2_Car("Toyota", 
                       "Vios", 
                       "White", 
                       "1234567890",
                       "ABC-1234");
    }

    public static Exer2_Car createHonda() {
        return new Exer2_Car("Honda", 
                       "Civic", 
                       "Black", 
                       "0987654321", 
                       "XYZ-5678");
    }
    public static Exer2_Car createToyotaVan() {
        return new Exer2_Car("Toyota", 
                       "Alphard", 
                       "White", 
                       "987654331",
                       "EFG-4321");
    }
    public Exer2_Car(String Brand, String model, String color, String ChasissNumber, String PlateNumber){
        this.Brand = Brand;
        this.model = model;
        this.color = color;
        this.ChasissNumber = ChasissNumber;
        this.PlateNumber = PlateNumber;
    }
    public void displayDetails(){
        String details = " ";
        details += "Brand: " + this.Brand + "\n";
        details += " Model: " + this.model + "\n";
        details += " Color: " + this.color + "\n";
        details += " Chasiss Number: " + this.ChasissNumber + "\n";
        details += " Plate Number: " + this.PlateNumber + "\n";
        System.out.println(details);
    }
}
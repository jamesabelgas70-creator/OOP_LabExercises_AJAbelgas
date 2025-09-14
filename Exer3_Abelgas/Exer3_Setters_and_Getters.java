package Exer3_Abelgas;

// Class to represent SDG Health Objectives
class HealthObjective {
    private String objectiveCode;
    private String description;

    // Constructor
    public HealthObjective(String objectiveCode, String description) {
        this.objectiveCode = objectiveCode;
        this.description = description;
    }

    // Getter for objectiveCode
    public String getObjectiveCode() {
        return objectiveCode;
    }

    // Setter for objectiveCode
    public void setObjectiveCode(String objectiveCode) {
        this.objectiveCode = objectiveCode;
    }

    // Getter for description
    public String getDescription() {
        return description;
    }

    // Setter for description
    public void setDescription(String description) {
        this.description = description;
    }

    // Display Objective
    public void displayObjective() {
        System.out.println(objectiveCode + ": " + description);
    }
}

// Main class
public class Exer3_Setters_and_Getters {
    public static void main(String[] args) {
        System.out.println("======================================================================================");
        System.out.println("      SDG Health Objectives List");
        System.out.println("======================================================================================\n");

        // Creating 3 health objectives with setters and getters
        HealthObjective obj1 = new HealthObjective(
            "3.c",
            "Substantially increase health financing and the recruitment, development, training and retention of the health workforce in developing countries, especially in least developed countries and small island developing States"
        );

        HealthObjective obj2 = new HealthObjective(
            "3.d",
            "Strengthen the capacity of all countries, in particular developing countries, for early warning, risk reduction and management of national and global health risks"
        );

        HealthObjective obj3 = new HealthObjective(
            "3.5",
            "Strengthen the prevention and treatment of substance abuse, including narcotic drug abuse and harmful use of alcohol"
        );

        // Displaying objectives
        obj1.displayObjective();
        System.out.println("--------------------------------------------------------------------------------------");
        obj2.displayObjective();
        System.out.println("--------------------------------------------------------------------------------------");
        obj3.displayObjective();

        // Example of using setters
        obj3.setDescription("Updated: Focus on preventing harmful substance abuse globally.");
        System.out.println("\nAfter update:");
        System.out.println("--------------------------------------------------------------------------------------");
        obj3.displayObjective();
        System.out.println("======================================================================================");
    }
}
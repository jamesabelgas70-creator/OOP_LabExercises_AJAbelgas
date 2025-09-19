class Patient extends User {
    String condition;

    public Patient(String name, int age, String email, String condition) {
        super(name, age, email);
        this.condition = condition;
    }

    public void displayPatientInfo() {
        displayInfo();
        System.out.println("Condition: " + condition);
    }
}
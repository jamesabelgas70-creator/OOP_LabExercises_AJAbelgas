class Counselor extends User {
    String specialization;

    public Counselor(String name, int age, String email, String specialization) {
        super(name, age, email);
        this.specialization = specialization;
    }

    public void displayCounselorInfo() {
        displayInfo();
        System.out.println("Specialization: " + specialization);
    }
}
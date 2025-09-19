public class ProjectTester {
    public static void main(String[] args) {
        // Create Users
        Patient p1 = new Patient("Alice", 25, "alice@email.com", "Anxiety");
        Counselor c1 = new Counselor("Dr. Bob", 40, "drbob@email.com", "Cognitive Behavioral Therapy");

        // Display User Info
        System.out.println("======== Patient Info =======");
        p1.displayPatientInfo();

        System.out.println("\n======== Counselor Info ========");
        c1.displayCounselorInfo();

        // Create Services
        ChatSupport chat = new ChatSupport("MindEase Chat");
        TherapySession therapy = new TherapySession("One-on-One Counseling");
        StressTracker tracker = new StressTracker("Daily Stress Log");

        // Use Services
        System.out.println("\n======== Services ========");
        chat.provideService();
        therapy.provideService();
        tracker.provideService();
    }
}

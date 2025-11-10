public class Exer4_Polymorphism_Tester {
    public static void main(String[] args) {
        // Create Users
        Patient p1 = new Patient("Alice", 25, "alice@email.com", "Anxiety");
        Counselor c1 = new Counselor("Dr. Bob", 40, "drbob@email.com", "Cognitive Behavioral Therapy");

        // Display User Info
        System.out.println("======== Patient Info =======");
        p1.displayPatientInfo();

        System.out.println("\n======== Counselor Info =======");
        c1.displayCounselorInfo();

        // Polymorphism (Overriding)
        SupportService chat = new ChatSupport("MindEase Chat");
        SupportService therapy = new TherapySession("One-on-One Counseling");
        SupportService tracker = new StressTracker("Daily Stress Log");

        System.out.println("\n===== Runtime Polymorphism (Overriding) =====");
        chat.provideService();
        therapy.provideService();
        tracker.provideService();

        System.out.println("\n===== Compile-time Polymorphism (Overloading) =====");
        chat.provideService(p1);
        therapy.provideService(c1, 45);

        System.out.println("\n===== Upcasting Example =====");
        SupportService upcasted = new ChatSupport("Upcasted Chat");
        upcasted.provideService();

        System.out.println("\n===== Downcasting Example =====");
        if (upcasted instanceof ChatSupport chatService) {
            chatService.provideService();
        }
    }
}

public class SupportService {
    String serviceName;

    public SupportService(String serviceName) {
        this.serviceName = serviceName;
    }

    // Base method
    public void provideService() {
        System.out.println("Providing service: " + serviceName);
    }

    // Overloaded method 1
    public void provideService(User user) {
        System.out.println("Providing " + serviceName + " to " + user.name);
    }

    // Overloaded method 2
    public void provideService(User user, int duration) {
        System.out.println("Providing " + serviceName + " to " + user.name + " for " + duration + " minutes.");
    }
}

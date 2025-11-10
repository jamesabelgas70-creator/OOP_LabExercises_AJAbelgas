public class TherapySession extends SupportService {
    public TherapySession(String serviceName) {
        super(serviceName);
    }

    @Override
    public void provideService() {
        System.out.println("Conducting therapy session: " + serviceName);
    }
}

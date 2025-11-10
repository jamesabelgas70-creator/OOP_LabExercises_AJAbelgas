public class ChatSupport extends SupportService {
    public ChatSupport(String serviceName) {
        super(serviceName);
    }

    @Override
    public void provideService() {
        System.out.println("Starting live chat support: " + serviceName);
    }
}

class ChatSupport extends SupportService {
    public ChatSupport(String serviceName) {
        super(serviceName);
    }

    @Override
    public void provideService() {
        System.out.println("Starting chat support: " + serviceName);
    }
}
class SupportService {
    String serviceName;

    public SupportService(String serviceName) {
        this.serviceName = serviceName;
    }

    public void provideService() {
        System.out.println("Providing service: " + serviceName);
    }
}
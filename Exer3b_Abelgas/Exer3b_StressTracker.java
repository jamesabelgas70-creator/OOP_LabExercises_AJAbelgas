class StressTracker extends SupportService {
    public StressTracker(String serviceName) {
        super(serviceName);
    }

    @Override
    public void provideService() {
        System.out.println("Tracking stress level with: " + serviceName);
    }
}
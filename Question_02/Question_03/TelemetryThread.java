public class TelemetryThread extends Thread {

    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            int cpuUsage = 20 + (i * 5);
            int memoryUsage = 40 + (i * 4);

public class TelemetryThread extends Thread {

    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            int cpuUsage = 20 + (i * 5);
            int memoryUsage = 40 + (i * 4);

            System.out.println(
                Thread.currentThread().getName()
                + " | Telemetry Iteration: " + i
                + " | CPU Usage: " + cpuUsage + "%"
                + " | Memory Usage: " + memoryUsage + "%"
            );
             try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Telemetry thread interrupted.");
                return;
            }
        }
    }
}

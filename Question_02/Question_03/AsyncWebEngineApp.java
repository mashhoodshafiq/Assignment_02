public class AsyncWebEngineApp {

    public static void main(String[] args) {
        System.out.println(
            "Main thread name: "
            + Thread.currentThread().getName()
        );

         // Create the telemetry thread
        TelemetryThread telemetry = new TelemetryThread();
        telemetry.setName("Telemetry-Worker");

        // Create the log processor thread
        LogProcessorTask logTask = new LogProcessorTask();
        Thread logThread = new Thread(logTask);
        logThread.setName("Log-Worker");

         // Start both threads
        telemetry.start();
        logThread.start();

        // Display thread execution states
        System.out.println(
            "Telemetry-Worker state: " + telemetry.getState()
        );

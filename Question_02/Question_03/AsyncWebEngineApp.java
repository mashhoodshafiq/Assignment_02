public class AsyncWebEngineApp {

    public static void main(String[] args) {
        System.out.println(
            "Main thread name: "
            + Thread.currentThread().getName()
        );

         // Create the telemetry thread
        TelemetryThread telemetry = new TelemetryThread();
        telemetry.setName("Telemetry-Worker");

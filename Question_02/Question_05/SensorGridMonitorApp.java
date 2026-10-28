import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.Random;

public class SensorGridMonitorApp {

    public static void main(String[] args) throws Exception {

        final int SIZE = 50000;
        final int WORKERS = 4;

        double[] sensorData = new double[SIZE];
        Random random = new Random();

        // Generate synthetic sensor readings
        for (int i = 0; i < SIZE; i++) {
            sensorData[i] = 10 + random.nextDouble() * 100;
        }

        ExecutorService executor =
                Executors.newFixedThreadPool(WORKERS);

        try {
            int chunkSize = SIZE / WORKERS;

            // Store the four segment boundaries
            int[] starts = new int[WORKERS];
            int[] ends = new int[WORKERS];

            for (int i = 0; i < WORKERS; i++) {
                starts[i] = i * chunkSize;
                ends[i] = (i == WORKERS - 1)
                        ? SIZE
                        : starts[i] + chunkSize;
            }

            // Submit minimum and maximum tasks for each segment
            @SuppressWarnings("unchecked")
            Future<Double>[] minResults = new Future[WORKERS];

            @SuppressWarnings("unchecked")
            Future<Double>[] maxResults = new Future[WORKERS];

            long startTime = System.nanoTime();

            for (int i = 0; i < WORKERS; i++) {

                final int start = starts[i];
                final int end = ends[i];

                minResults[i] = executor.submit(
                    () -> new ParallelMinSearch(
                        sensorData, start, end
                    ).processGrid()
                );

                maxResults[i] = executor.submit(
                    () -> new ParallelMaxSearch(
                        sensorData, start, end
                    ).processGrid()
                );
            }

            // Combine the partial results
            double globalMin = Double.POSITIVE_INFINITY;
            double globalMax = Double.NEGATIVE_INFINITY;

            for (int i = 0; i < WORKERS; i++) {
                globalMin = Math.min(
                    globalMin, minResults[i].get()
                );

                globalMax = Math.max(
                    globalMax, maxResults[i].get()
                );
            }

            long endTime = System.nanoTime();

            double executionTime =
                    (endTime - startTime) / 1_000_000.0;

            System.out.println(
                "=== Sensor Grid Monitoring Results ==="
            );

            System.out.println(
                "Total sensor readings: " + SIZE
            );

            System.out.println(
                "Worker threads: " + WORKERS
            );

            System.out.printf(
                "Global minimum temperature: %.2f%n",
                globalMin
            );

            System.out.printf(
                "Global maximum vibration: %.2f%n",
                globalMax
            );

            System.out.printf(
                "Parallel execution time: %.3f ms%n",
                executionTime
            );

            System.out.println(
                "All sensor segments processed successfully."
            );

        } finally {
            executor.shutdown();
        }
    }
}


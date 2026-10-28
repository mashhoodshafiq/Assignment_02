import java.util.concurrent.ForkJoinPool;
import java.util.Random;

public class ForkJoinPortfolioApp {

    public static void main(String[] args) {

        final int SIZE = 10000;
        final double RISK_CAP = 1000.0;

        double[] assets = new double[SIZE];
        Random random = new Random(42);

        // Generate synthetic asset values
        for (int i = 0; i < SIZE; i++) {
            assets[i] = 500 + random.nextDouble() * 1500;
        }

        ForkJoinPool pool = new ForkJoinPool();

        try {
            // Sequential calculation for comparison
            long serialStart = System.nanoTime();

            double serialRisk = 0.0;
            for (double asset : assets) {
                serialRisk += asset;
            }

            long serialEnd = System.nanoTime();

            // Parallel risk calculation
            long parallelStart = System.nanoTime();

            ParallelRiskCalculator riskTask =
                    new ParallelRiskCalculator(assets, 0, SIZE);

            double totalRisk = pool.invoke(riskTask);

            long parallelEnd = System.nanoTime();

            // Apply the stress test in parallel
            long stressStart = System.nanoTime();

            PortfolioThresholdTask stressTask =
                    new PortfolioThresholdTask(
                            assets, RISK_CAP, 0, SIZE);

            pool.invoke(stressTask);

            long stressEnd = System.nanoTime();

             // Calculate the adjusted total after stress testing
            double adjustedRisk = 0.0;
            for (double asset : assets) {
                adjustedRisk += asset;
            }

            // Display results
            System.out.println("=== Portfolio Assessment Results ===");
            System.out.println("Number of assets: " + SIZE);
            System.out.printf("Original total risk score: %.2f%n", totalRisk);
            System.out.printf("Serial total risk score: %.2f%n", serialRisk);
            System.out.printf("Adjusted total after stress test: %.2f%n",
                    adjustedRisk);

            System.out.println("\n=== Performance Metrics ===");

            double serialMs = (serialEnd - serialStart) / 1_000_000.0;
            double parallelMs =
                    (parallelEnd - parallelStart) / 1_000_000.0;
            double stressMs =
                    (stressEnd - stressStart) / 1_000_000.0;

            System.out.printf("Serial calculation time: %.3f ms%n", serialMs);
            System.out.printf("Parallel calculation time: %.3f ms%n",
                    parallelMs);
            System.out.printf("Parallel stress-test time: %.3f ms%n",
                    stressMs);

            if (parallelMs > 0) {
                System.out.printf("Speedup: %.2fx%n",
                        serialMs / parallelMs);
            }

            System.out.println("ForkJoinPool parallelism: "
                    + pool.getParallelism());

        } finally {
            pool.shutdown();
        }
    }
}


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


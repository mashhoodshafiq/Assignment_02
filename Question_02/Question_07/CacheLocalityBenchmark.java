import java.util.Random;

public class CacheLocalityBenchmark {

    private static final int N = 2000;
    private static final int ITERATIONS = 10;

    private static final double[][] matrix = new double[N][N];

    // Stores the result so the calculated sum remains observable.
    private static volatile double resultSink;

    // Fill the matrix before benchmarking.
    private static void initializeMatrix() {
        Random random = new Random(42);

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                matrix[i][j] = random.nextDouble();
            }
        }
    }

    // Traverse the matrix row by row.
    public static double traverseRowMajor() {
        double sum = 0.0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                sum += matrix[i][j];
            }
        }

        resultSink = sum;
        return sum;
    }

    // Traverse the matrix column by column.
    public static double traverseColumnMajor() {
        double sum = 0.0;

        for (int j = 0; j < N; j++) {
            for (int i = 0; i < N; i++) {
                sum += matrix[i][j];
            }
        }

        resultSink = sum;
        return sum;
    }

    public static void main(String[] args) {
        initializeMatrix();

        // Warm up the JVM before recording timings.
        for (int i = 0; i < 3; i++) {
            traverseRowMajor();
            traverseColumnMajor();
        }

        long rowMajorTotal = 0;
        long columnMajorTotal = 0;

        for (int iteration = 1; iteration <= ITERATIONS; iteration++) {

            long start = System.nanoTime();
            double rowSum = traverseRowMajor();
            long rowTime = System.nanoTime() - start;
            rowMajorTotal += rowTime;

            start = System.nanoTime();
            double columnSum = traverseColumnMajor();
            long columnTime = System.nanoTime() - start;
            columnMajorTotal += columnTime;

            System.out.printf(
                "Iteration %2d | Row-Major: %8.3f ms | "
                + "Column-Major: %8.3f ms%n",
                iteration,
                rowTime / 1_000_000.0,
                columnTime / 1_000_000.0
            );

            // Both traversals should sum the same matrix values.
            if (Math.abs(rowSum - columnSum) > 1e-6) {
                System.out.println("Warning: sums differ!");
            }
        }

        double rowAverageMs =
            rowMajorTotal / (double) ITERATIONS / 1_000_000.0;

        double columnAverageMs =
            columnMajorTotal / (double) ITERATIONS / 1_000_000.0;

        double speedup = columnAverageMs / rowAverageMs;

        System.out.println("\n===== Benchmark Summary =====");
        System.out.printf("Matrix size: %d x %d%n", N, N);
        System.out.printf("Iterations: %d%n", ITERATIONS);
        System.out.printf("Average Row-Major time: %.3f ms%n",
                          rowAverageMs);
        System.out.printf("Average Column-Major time: %.3f ms%n",
                          columnAverageMs);
        System.out.printf("Row-Major speedup: %.2fx%n", speedup);

        // Keep the result observable.
        System.out.println("Result check: " + resultSink);
    }
}

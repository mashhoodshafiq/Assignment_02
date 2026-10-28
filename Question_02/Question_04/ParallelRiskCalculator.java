import java.util.concurrent.RecursiveTask;

public class ParallelRiskCalculator extends RecursiveTask<Double> {

    private static final int THRESHOLD = 1000;

    private final double[] assets;
    private final int start;
    private final int end;

    public ParallelRiskCalculator(double[] assets, int start, int end) {
        this.assets = assets;
        this.start = start;
        this.end = end;
    }
     @Override
    protected Double compute() {

        // Base case: process the segment sequentially
        if (end - start < THRESHOLD) {
            double sum = 0.0;

            for (int i = start; i < end; i++) {
                sum += assets[i];
            }


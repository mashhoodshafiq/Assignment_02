import java.util.concurrent.RecursiveAction;

public class PortfolioThresholdTask extends RecursiveAction {

    private static final int THRESHOLD = 1000;

    private final double[] assets;
    private final double riskCap;
    private final int start;
    private final int end;

    public PortfolioThresholdTask(
            double[] assets,
            double riskCap,
            int start,
            int end) {

        this.assets = assets;
        this.riskCap = riskCap;
        this.start = start;
        this.end = end;
    }

     @Override
    protected void compute() {

        // Process small segments directly
        if (end - start < THRESHOLD) {
        
            for (int i = start; i < end; i++) {
                if (assets[i] > riskCap) {
                    assets[i] *= 0.95; // Apply 5% penalty
                }
            }

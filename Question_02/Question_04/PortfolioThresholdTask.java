import java.util.concurrent.RecursiveAction;

public class PortfolioThresholdTask extends RecursiveAction {

    private static final int THRESHOLD = 1000;

    private final double[] assets;
    private final double riskCap;
    private final int start;
    private final int end;
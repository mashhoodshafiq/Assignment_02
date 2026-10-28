public class ParallelMaxSearch extends SensorSearchEngine {

    public ParallelMaxSearch(
            double[] sensorData,
            int startIdx,
            int endIdx) {

        super(sensorData, startIdx, endIdx);
    }

    @Override
    public double processGrid() {

        double max = Double.NEGATIVE_INFINITY;

        for (int i = startIdx; i < endIdx; i++) {
            if (sensorData[i] > max) {
                max = sensorData[i];
            }
        }

        return max;
    }
}


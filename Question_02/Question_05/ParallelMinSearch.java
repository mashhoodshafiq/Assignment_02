public class ParallelMinSearch extends SensorSearchEngine {

    public ParallelMinSearch(
            double[] sensorData,
            int startIdx,
            int endIdx) {

        super(sensorData, startIdx, endIdx);
    }

    @Override
    public double processGrid() {

        double min = Double.POSITIVE_INFINITY;

        for (int i = startIdx; i < endIdx; i++) {
            if (sensorData[i] < min) {
                min = sensorData[i];
            }
        }

        return min;
    }
}


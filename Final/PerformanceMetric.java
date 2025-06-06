package Final;

/**
 * Performance metric data structure for tracking system performance
 */
public class PerformanceMetric {
    private final long timestamp;
    private final int totalOperations;
    private final double consistencyViolations;
    private final double averageLatency;

    public PerformanceMetric(long timestamp, int totalOperations, 
                           double consistencyViolations, double averageLatency) {
        this.timestamp = timestamp;
        this.totalOperations = totalOperations;
        this.consistencyViolations = consistencyViolations;
        this.averageLatency = averageLatency;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getTotalOperations() {
        return totalOperations;
    }

    public double getConsistencyViolations() {
        return consistencyViolations;
    }

    public double getAverageLatency() {
        return averageLatency;
    }

    @Override
    public String toString() {
        return String.format("Metric[time=%d, ops=%d, violations=%.2f%%, latency=%.2fms]",
                           timestamp, totalOperations, consistencyViolations * 100, averageLatency);
    }
} 
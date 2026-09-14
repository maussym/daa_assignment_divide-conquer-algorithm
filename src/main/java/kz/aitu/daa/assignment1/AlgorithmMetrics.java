package kz.aitu.daa.assignment1;

/** Mutable counters collected during one algorithm run. */
public final class AlgorithmMetrics {
    private long comparisons;
    private long swaps;
    private long recursiveCalls;
    private long allocations;
    private int maxRecursionDepth;

    public void comparison() { comparisons++; }
    public void comparisons(long count) { comparisons += count; }
    public void swap() { swaps++; }
    public void recursiveCall(int depth) {
        recursiveCalls++;
        if (depth > maxRecursionDepth) {
            maxRecursionDepth = depth;
        }
    }
    public void allocation() { allocations++; }
    public void allocations(long count) { allocations += count; }

    public long getComparisons() { return comparisons; }
    public long getSwaps() { return swaps; }
    public long getRecursiveCalls() { return recursiveCalls; }
    public long getAllocations() { return allocations; }
    public int getMaxRecursionDepth() { return maxRecursionDepth; }

    public void reset() {
        comparisons = 0;
        swaps = 0;
        recursiveCalls = 0;
        allocations = 0;
        maxRecursionDepth = 0;
    }
}

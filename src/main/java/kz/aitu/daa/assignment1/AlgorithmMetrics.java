package kz.aitu.daa.assignment1;

/** Mutable counters collected during one algorithm run. */
public final class AlgorithmMetrics {
    private long comparisons;
    private long recursiveCalls;
    private int maxRecursionDepth;

    public void comparison() { comparisons++; }

    public void recursiveCall(int depth) {
        recursiveCalls++;
        maxRecursionDepth = Math.max(maxRecursionDepth, depth);
    }

    public long getComparisons() { return comparisons; }
    public long getRecursiveCalls() { return recursiveCalls; }
    public int getMaxRecursionDepth() { return maxRecursionDepth; }
}

package kz.aitu.daa.assignment1;

/** Linear-time deterministic selection using the Median-of-Medians pivot. */
public final class DeterministicSelector {
    private DeterministicSelector() {}

    /** Returns the element that would appear at zero-based index k after sorting. */
    public static int select(int[] values, int k) {
        return select(values, k, new AlgorithmMetrics());
    }

    public static int select(int[] values, int k, AlgorithmMetrics metrics) {
        if (values == null) {
            throw new IllegalArgumentException("values must not be null");
        }
        if (values.length == 0) {
            throw new IllegalArgumentException("values must not be empty");
        }
        if (k < 0 || k >= values.length) {
            throw new IllegalArgumentException("k out of range: " + k);
        }
        return select(values, 0, values.length - 1, k, 1, new int[2], metrics);
    }

    private static int select(int[] a, int left, int right, int k,
                              int depth, int[] bounds, AlgorithmMetrics metrics) {
        metrics.recursiveCall(depth);
        if (left == right) return a[left];

        int pivot = medianOfMedians(a, left, right, depth + 1, bounds, metrics);
        ArrayTools.partition(a, left, right, pivot, bounds, metrics);
        int less = bounds[0], greater = bounds[1];
        if (k < less) return select(a, left, less - 1, k, depth + 1, bounds, metrics);
        if (k > greater) return select(a, greater + 1, right, k, depth + 1, bounds, metrics);
        return a[k];
    }

    private static int medianOfMedians(int[] a, int left, int right, int depth,
                                       int[] bounds, AlgorithmMetrics metrics) {
        int size = right - left + 1;
        if (size <= 5) {
            ArrayTools.insertionSort(a, left, right, metrics);
            return a[left + size / 2];
        }

        int count = 0;
        for (int start = left; start <= right; start += 5) {
            int end = Math.min(start + 4, right);
            ArrayTools.insertionSort(a, start, end, metrics);
            ArrayTools.swap(a, left + count++, (start + end) / 2);
        }
        int middle = left + count / 2;
        return select(a, left, left + count - 1, middle, depth, bounds, metrics);
    }
}

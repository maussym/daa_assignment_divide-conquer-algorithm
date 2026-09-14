package kz.aitu.daa.assignment1;

/** Merge sort with one reusable buffer and insertion-sort cutoff. */
public final class MergeSorter {
    private static final int CUTOFF = 16;

    private MergeSorter() {}

    public static void sort(int[] values) {
        sort(values, new AlgorithmMetrics());
    }

    public static void sort(int[] values, AlgorithmMetrics metrics) {
        if (values == null) {
            throw new IllegalArgumentException("values must not be null");
        }
        if (values.length < 2) {
            return;
        }

        mergeSort(values, new int[values.length], 0, values.length, 1, metrics);
    }

    private static void mergeSort(int[] a, int[] buffer, int left, int right,
                                  int depth, AlgorithmMetrics metrics) {
        metrics.recursiveCall(depth);
        if (right - left <= CUTOFF) {
            ArrayTools.insertionSort(a, left, right - 1, metrics);
            return;
        }

        int mid = (left + right) >>> 1;
        mergeSort(a, buffer, left, mid, depth + 1, metrics);
        mergeSort(a, buffer, mid, right, depth + 1, metrics);

        metrics.comparison();
        if (a[mid - 1] > a[mid]) merge(a, buffer, left, mid, right, metrics);
    }

    private static void merge(int[] a, int[] buffer, int left, int mid, int right,
                              AlgorithmMetrics metrics) {
        System.arraycopy(a, left, buffer, left, right - left);

        int i = left, j = mid, k = left;
        while (i < mid && j < right) {
            metrics.comparison();
            a[k++] = buffer[i] <= buffer[j] ? buffer[i++] : buffer[j++];
        }
        while (i < mid) a[k++] = buffer[i++];
        while (j < right) a[k++] = buffer[j++];
    }
}

package kz.aitu.daa.assignment1;

/** Merge sort with one reusable buffer and insertion-sort cutoff. */
public final class MergeSorter {
    private static final int INSERTION_SORT_CUTOFF = 16;

    private MergeSorter() {
    }

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

        int[] buffer = new int[values.length];
        metrics.allocation();
        mergeSort(values, buffer, 0, values.length, 1, metrics);
    }

    private static void mergeSort(
            int[] values,
            int[] buffer,
            int left,
            int right,
            int depth,
            AlgorithmMetrics metrics) {
        metrics.recursiveCall(depth);
        int length = right - left;
        if (length <= 1) {
            return;
        }
        if (length <= INSERTION_SORT_CUTOFF) {
            insertionSort(values, left, right, metrics);
            return;
        }

        int mid = left + length / 2;
        mergeSort(values, buffer, left, mid, depth + 1, metrics);
        mergeSort(values, buffer, mid, right, depth + 1, metrics);

        // Already ordered: skip the merge work.
        metrics.comparison();
        if (values[mid - 1] <= values[mid]) {
            return;
        }
        merge(values, buffer, left, mid, right, metrics);
    }

    private static void merge(
            int[] values,
            int[] buffer,
            int left,
            int mid,
            int right,
            AlgorithmMetrics metrics) {
        System.arraycopy(values, left, buffer, left, right - left);

        int i = left;
        int j = mid;
        int out = left;
        while (i < mid && j < right) {
            metrics.comparison();
            if (buffer[i] <= buffer[j]) {
                values[out++] = buffer[i++];
            } else {
                values[out++] = buffer[j++];
            }
        }
        while (i < mid) {
            values[out++] = buffer[i++];
        }
        while (j < right) {
            values[out++] = buffer[j++];
        }
    }

    private static void insertionSort(int[] values, int left, int right, AlgorithmMetrics metrics) {
        for (int i = left + 1; i < right; i++) {
            int key = values[i];
            int j = i - 1;
            while (j >= left) {
                metrics.comparison();
                if (values[j] <= key) {
                    break;
                }
                values[j + 1] = values[j];
                j--;
            }
            values[j + 1] = key;
        }
    }
}

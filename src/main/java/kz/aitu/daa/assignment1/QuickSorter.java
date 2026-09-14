package kz.aitu.daa.assignment1;

import java.util.Random;

/** Randomized in-place quicksort with smaller-side recursion. */
public final class QuickSorter {
    private QuickSorter() {
    }

    public static void sort(int[] values) {
        sort(values, new AlgorithmMetrics(), new Random());
    }

    public static void sort(int[] values, AlgorithmMetrics metrics) {
        // Fixed seed makes experiments reproducible while still selecting randomized pivots.
        sort(values, metrics, new Random(2310L));
    }

    static void sort(int[] values, AlgorithmMetrics metrics, Random random) {
        if (values == null) {
            throw new IllegalArgumentException("values must not be null");
        }
        if (values.length < 2) {
            return;
        }
        quickSort(values, 0, values.length - 1, 1, metrics, random);
    }

    private static void quickSort(
            int[] values,
            int low,
            int high,
            int depth,
            AlgorithmMetrics metrics,
            Random random) {
        metrics.recursiveCall(depth);

        while (low < high) {
            int pivotIndex = low + random.nextInt(high - low + 1);
            int pivot = values[pivotIndex];
            int[] equalRange = partitionThreeWay(values, low, high, pivot, metrics);
            int lt = equalRange[0];
            int gt = equalRange[1];

            int leftSize = lt - low;
            int rightSize = high - gt;

            // Recurse only on the smaller side; continue the larger side in this loop.
            if (leftSize < rightSize) {
                if (leftSize > 1) {
                    quickSort(values, low, lt - 1, depth + 1, metrics, random);
                }
                low = gt + 1;
            } else {
                if (rightSize > 1) {
                    quickSort(values, gt + 1, high, depth + 1, metrics, random);
                }
                high = lt - 1;
            }
        }
    }

    /** Dutch National Flag partition: [< pivot][== pivot][> pivot]. */
    private static int[] partitionThreeWay(
            int[] values,
            int low,
            int high,
            int pivot,
            AlgorithmMetrics metrics) {
        int lt = low;
        int i = low;
        int gt = high;

        while (i <= gt) {
            metrics.comparison();
            if (values[i] < pivot) {
                swap(values, lt++, i++, metrics);
            } else {
                metrics.comparison();
                if (values[i] > pivot) {
                    swap(values, i, gt--, metrics);
                } else {
                    i++;
                }
            }
        }
        return new int[]{lt, gt};
    }

    private static void swap(int[] values, int i, int j, AlgorithmMetrics metrics) {
        if (i == j) {
            return;
        }
        int tmp = values[i];
        values[i] = values[j];
        values[j] = tmp;
        metrics.swap();
    }
}

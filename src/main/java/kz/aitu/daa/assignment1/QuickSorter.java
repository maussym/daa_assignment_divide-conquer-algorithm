package kz.aitu.daa.assignment1;

import java.util.Random;

/** Randomized in-place quicksort with smaller-side recursion. */
public final class QuickSorter {
    private QuickSorter() {}

    public static void sort(int[] values) {
        sort(values, new AlgorithmMetrics(), new Random());
    }

    public static void sort(int[] values, AlgorithmMetrics metrics) {
        sort(values, metrics, new Random(2310L));
    }

    static void sort(int[] values, AlgorithmMetrics metrics, Random random) {
        if (values == null) {
            throw new IllegalArgumentException("values must not be null");
        }
        if (values.length < 2) {
            return;
        }
        quickSort(values, 0, values.length - 1, 1, new int[2], metrics, random);
    }

    private static void quickSort(int[] a, int left, int right, int depth,
                                  int[] bounds, AlgorithmMetrics metrics, Random random) {
        metrics.recursiveCall(depth);
        while (left < right) {
            int pivot = a[left + random.nextInt(right - left + 1)];
            ArrayTools.partition(a, left, right, pivot, bounds);
            int less = bounds[0], greater = bounds[1];

            if (less - left < right - greater) {
                if (left < less - 1)
                    quickSort(a, left, less - 1, depth + 1, bounds, metrics, random);
                left = greater + 1;
            } else {
                if (greater + 1 < right)
                    quickSort(a, greater + 1, right, depth + 1, bounds, metrics, random);
                right = less - 1;
            }
        }
    }
}

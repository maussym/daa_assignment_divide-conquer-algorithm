package kz.aitu.daa.assignment1;

/** Linear-time deterministic selection using the Median-of-Medians pivot. */
public final class DeterministicSelector {
    private DeterministicSelector() {
    }

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
        return selectRange(values, 0, values.length - 1, k, 1, metrics);
    }

    private static int selectRange(
            int[] values,
            int left,
            int right,
            int k,
            int depth,
            AlgorithmMetrics metrics) {
        metrics.recursiveCall(depth);

        if (left == right) {
            return values[left];
        }

        int pivot = medianOfMedians(values, left, right, depth + 1, metrics);
        int[] equalRange = partitionThreeWay(values, left, right, pivot, metrics);

        if (k < equalRange[0]) {
            return selectRange(values, left, equalRange[0] - 1, k, depth + 1, metrics);
        }
        if (k > equalRange[1]) {
            return selectRange(values, equalRange[1] + 1, right, k, depth + 1, metrics);
        }
        return values[k];
    }

    private static int medianOfMedians(
            int[] values,
            int left,
            int right,
            int depth,
            AlgorithmMetrics metrics) {
        int size = right - left + 1;
        if (size <= 5) {
            insertionSort(values, left, right, metrics);
            return values[left + size / 2];
        }

        int medianCount = 0;
        for (int groupStart = left; groupStart <= right; groupStart += 5) {
            int groupEnd = Math.min(groupStart + 4, right);
            insertionSort(values, groupStart, groupEnd, metrics);
            int medianIndex = groupStart + (groupEnd - groupStart) / 2;
            swap(values, left + medianCount, medianIndex, metrics);
            medianCount++;
        }

        int mediansLeft = left;
        int mediansRight = left + medianCount - 1;
        int medianTarget = mediansLeft + medianCount / 2;
        return selectRange(values, mediansLeft, mediansRight, medianTarget, depth, metrics);
    }

    private static int[] partitionThreeWay(
            int[] values,
            int left,
            int right,
            int pivot,
            AlgorithmMetrics metrics) {
        int lt = left;
        int i = left;
        int gt = right;
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

    private static void insertionSort(int[] values, int left, int right, AlgorithmMetrics metrics) {
        for (int i = left + 1; i <= right; i++) {
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

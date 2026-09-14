package kz.aitu.daa.assignment1;

/** Small array operations shared by QuickSort and Median of Medians. */
final class ArrayTools {
    private ArrayTools() {}

    static void partition(int[] a, int left, int right, int pivot,
                          int[] bounds, AlgorithmMetrics metrics) {
        int less = left, i = left, greater = right;
        while (i <= greater) {
            metrics.comparison();
            if (a[i] < pivot) {
                swap(a, less++, i++);
            } else {
                metrics.comparison();
                if (a[i] > pivot) swap(a, i, greater--);
                else i++;
            }
        }
        bounds[0] = less;
        bounds[1] = greater;
    }

    static void insertionSort(int[] a, int left, int right, AlgorithmMetrics metrics) {
        for (int i = left + 1; i <= right; i++) {
            int value = a[i], j = i - 1;
            while (j >= left) {
                metrics.comparison();
                if (a[j] <= value) break;
                a[j + 1] = a[j--];
            }
            a[j + 1] = value;
        }
    }

    static void swap(int[] a, int i, int j) {
        if (i == j) return;
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
}

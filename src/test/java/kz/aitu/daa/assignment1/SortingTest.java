package kz.aitu.daa.assignment1;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.Test;

class SortingTest {
    @Test
    void mergeSortMatchesArraysSortOnEdgeCases() {
        checkMerge(new int[]{});
        checkMerge(new int[]{5});
        checkMerge(new int[]{3, 3, 3, 3});
        checkMerge(new int[]{1, 2, 3, 4, 5});
        checkMerge(new int[]{5, 4, 3, 2, 1});
    }

    @Test
    void quickSortMatchesArraysSortOnEdgeCases() {
        checkQuick(new int[]{});
        checkQuick(new int[]{5});
        checkQuick(new int[]{3, 3, 3, 3});
        checkQuick(new int[]{1, 2, 3, 4, 5});
        checkQuick(new int[]{5, 4, 3, 2, 1});
    }

    @Test
    void bothSortsMatchArraysSortOnRandomInputs() {
        Random random = new Random(42);
        for (int test = 0; test < 100; test++) {
            int n = random.nextInt(500);
            int[] values = new int[n];
            for (int i = 0; i < n; i++) {
                values[i] = random.nextInt(101) - 50;
            }
            checkMerge(values);
            checkQuick(values);
        }
    }

    private static void checkMerge(int[] input) {
        int[] expected = input.clone();
        int[] actual = input.clone();
        Arrays.sort(expected);
        MergeSorter.sort(actual);
        assertArrayEquals(expected, actual);
    }

    private static void checkQuick(int[] input) {
        int[] expected = input.clone();
        int[] actual = input.clone();
        Arrays.sort(expected);
        QuickSorter.sort(actual);
        assertArrayEquals(expected, actual);
    }
}

package kz.aitu.daa.assignment1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.Test;

class DeterministicSelectorTest {
    @Test
    void matchesSortedReferenceForAtLeastOneHundredRandomTests() {
        Random random = new Random(2310);
        for (int test = 0; test < 150; test++) {
            int n = 1 + random.nextInt(300);
            int[] input = new int[n];
            for (int i = 0; i < n; i++) {
                input[i] = random.nextInt(61) - 30; // intentionally many duplicates
            }
            int k = random.nextInt(n);

            int[] expected = input.clone();
            Arrays.sort(expected);
            int[] actual = input.clone();
            assertEquals(expected[k], DeterministicSelector.select(actual, k));
        }
    }

    @Test
    void rejectsInvalidK() {
        assertThrows(IllegalArgumentException.class,
                () -> DeterministicSelector.select(new int[]{1, 2, 3}, -1));
        assertThrows(IllegalArgumentException.class,
                () -> DeterministicSelector.select(new int[]{1, 2, 3}, 3));
    }
}

package kz.aitu.daa.assignment1;

import java.util.Arrays;
import java.util.Random;

/** Dependency-free correctness check; JUnit tests in src/test provide the formal test suite. */
public final class SelfTest {
    private SelfTest() {
    }

    public static void main(String[] args) {
        testSorting();
        testSelection();
        testClosestPair();
        System.out.println("All self-tests passed.");
    }

    private static void testSorting() {
        String[] types = {"random", "sorted", "reverse", "duplicates"};
        int checks = 0;
        for (String type : types) {
            for (int n : new int[]{0, 1, 10, 100, 1000}) {
                int[] input = Experiment.generateArray(n, type, 100L + n + type.hashCode());
                int[] expected = input.clone();
                Arrays.sort(expected);

                int[] merge = input.clone();
                MergeSorter.sort(merge);
                require(Arrays.equals(expected, merge), "MergeSort failed");

                int[] quick = input.clone();
                QuickSorter.sort(quick);
                require(Arrays.equals(expected, quick), "QuickSort failed");
                checks += 2;
            }
        }
        System.out.println("Sorting reference checks: " + checks + " passed");
    }

    private static void testSelection() {
        Random random = new Random(2310L);
        int checks = 150;
        for (int test = 0; test < checks; test++) {
            int n = 1 + random.nextInt(400);
            int[] input = new int[n];
            for (int i = 0; i < n; i++) {
                input[i] = random.nextInt(101) - 50;
            }
            int k = random.nextInt(n);
            int[] reference = input.clone();
            Arrays.sort(reference);
            int actual = DeterministicSelector.select(input.clone(), k);
            require(actual == reference[k], "Deterministic Select failed");
        }
        System.out.println("Deterministic Select random checks: " + checks + " passed");
    }

    private static void testClosestPair() {
        Random random = new Random(99L);
        int checks = 40;
        for (int test = 0; test < checks; test++) {
            int n = 2 + random.nextInt(300);
            Point[] points = new Point[n];
            for (int i = 0; i < n; i++) {
                points[i] = new Point(random.nextDouble() * 10_000, random.nextDouble() * 10_000);
            }
            double expected = ClosestPairSolver.bruteForce(points).distance();
            double actual = ClosestPairSolver.solve(points).distance();
            require(Math.abs(expected - actual) <= 1e-9, "Closest Pair failed");
        }
        System.out.println("Closest Pair brute-force checks: " + checks + " passed");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}

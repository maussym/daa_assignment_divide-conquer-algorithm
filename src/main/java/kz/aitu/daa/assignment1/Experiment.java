package kz.aitu.daa.assignment1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/** Runs repeatable timing/recursion experiments and writes results/results.csv. */
public final class Experiment {
    private static final int[] SORT_SIZES = {100, 1_000, 10_000, 50_000};
    private static final int[] SELECT_SIZES = {100, 1_000, 10_000, 50_000};
    private static final int[] POINT_SIZES = {100, 1_000, 10_000, 30_000};
    private static final String[] INPUT_TYPES = {"random", "sorted", "reverse", "duplicates"};
    private static final int REPETITIONS = 5;

    private Experiment() {
    }

    public static void main(String[] args) throws IOException {
        Locale.setDefault(Locale.US);
        warmUpJvm();

        List<String> rows = new ArrayList<>();
        rows.add("algorithm,input_type,n,time_ns,max_recursion_depth,comparisons,recursive_calls");

        for (int n : SORT_SIZES) {
            for (String type : INPUT_TYPES) {
                int[] base = generateArray(n, type, 1000L + n + type.hashCode());
                rows.add(runSort(base, type, true));
                rows.add(runSort(base, type, false));
            }
        }

        for (int n : SELECT_SIZES) {
            int[] base = generateArray(n, "random", 5000L + n);
            rows.add(runSelect(base, "random"));
            int[] duplicates = generateArray(n, "duplicates", 7000L + n);
            rows.add(runSelect(duplicates, "duplicates"));
        }

        for (int n : POINT_SIZES) {
            Point[] points = generatePoints(n, 9000L + n);
            rows.add(runClosestPair(points));
        }

        Path output = Path.of("results", "results.csv");
        Files.createDirectories(output.getParent());
        Files.writeString(output, String.join("\n", rows) + "\n",
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        System.out.println("Wrote " + (rows.size() - 1) + " experiment rows to " + output.toAbsolutePath());
        System.out.println("Each time value is the median of " + REPETITIONS + " runs after a JVM warm-up.");
    }

    private static void warmUpJvm() {
        int[] sample = generateArray(5_000, "random", 12345L);
        for (int i = 0; i < 3; i++) {
            MergeSorter.sort(sample.clone());
            QuickSorter.sort(sample.clone());
            DeterministicSelector.select(sample.clone(), sample.length / 2);
            ClosestPairSolver.solve(generatePoints(2_000, 20000L + i));
        }
    }

    private static String runSort(int[] base, String type, boolean mergeSort) {
        List<RunResult> runs = new ArrayList<>();
        for (int repetition = 0; repetition < REPETITIONS; repetition++) {
            int[] values = base.clone();
            AlgorithmMetrics metrics = new AlgorithmMetrics();
            long start = System.nanoTime();
            if (mergeSort) MergeSorter.sort(values, metrics);
            else QuickSorter.sort(values, metrics);
            long elapsed = System.nanoTime() - start;
            verifySorted(values);
            runs.add(new RunResult(elapsed, metrics));
        }
        return row(mergeSort ? "MergeSort" : "QuickSort", type, base.length, median(runs));
    }

    private static String runSelect(int[] base, String type) {
        int k = base.length / 2;
        int[] reference = base.clone();
        Arrays.sort(reference);
        int expected = reference[k];

        List<RunResult> runs = new ArrayList<>();
        for (int repetition = 0; repetition < REPETITIONS; repetition++) {
            int[] values = base.clone();
            AlgorithmMetrics metrics = new AlgorithmMetrics();
            long start = System.nanoTime();
            int selected = DeterministicSelector.select(values, k, metrics);
            long elapsed = System.nanoTime() - start;
            if (selected != expected) {
                throw new IllegalStateException("DeterministicSelect produced an incorrect result");
            }
            runs.add(new RunResult(elapsed, metrics));
        }
        return row("DeterministicSelect", type, base.length, median(runs));
    }

    private static String runClosestPair(Point[] points) {
        List<RunResult> runs = new ArrayList<>();
        for (int repetition = 0; repetition < REPETITIONS; repetition++) {
            AlgorithmMetrics metrics = new AlgorithmMetrics();
            long start = System.nanoTime();
            ClosestPairSolver.Result result = ClosestPairSolver.solve(points, metrics);
            long elapsed = System.nanoTime() - start;
            if (!Double.isFinite(result.distance())) {
                throw new IllegalStateException("ClosestPair produced a non-finite distance");
            }
            runs.add(new RunResult(elapsed, metrics));
        }
        return row("ClosestPair", "random_points", points.length, median(runs));
    }

    private static RunResult median(List<RunResult> runs) {
        return runs.stream()
                .sorted(Comparator.comparingLong(RunResult::timeNs))
                .skip(runs.size() / 2)
                .findFirst()
                .orElseThrow();
    }

    private static String row(String algorithm, String inputType, int n, RunResult result) {
        AlgorithmMetrics metrics = result.metrics();
        return String.join(",",
                algorithm,
                inputType,
                Integer.toString(n),
                Long.toString(result.timeNs()),
                Integer.toString(metrics.getMaxRecursionDepth()),
                Long.toString(metrics.getComparisons()),
                Long.toString(metrics.getRecursiveCalls()));
    }

    static int[] generateArray(int n, String type, long seed) {
        Random random = new Random(seed);
        int[] values = new int[n];
        switch (type) {
            case "random" -> {
                for (int i = 0; i < n; i++) values[i] = random.nextInt();
            }
            case "sorted" -> {
                for (int i = 0; i < n; i++) values[i] = i;
            }
            case "reverse" -> {
                for (int i = 0; i < n; i++) values[i] = n - i;
            }
            case "duplicates" -> {
                for (int i = 0; i < n; i++) values[i] = random.nextInt(10);
            }
            default -> throw new IllegalArgumentException("Unknown input type: " + type);
        }
        return values;
    }

    static Point[] generatePoints(int n, long seed) {
        Random random = new Random(seed);
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            points[i] = new Point(random.nextDouble() * 1_000_000.0, random.nextDouble() * 1_000_000.0);
        }
        return points;
    }

    private static void verifySorted(int[] values) {
        for (int i = 1; i < values.length; i++) {
            if (values[i - 1] > values[i]) {
                throw new IllegalStateException("Sorting failed at index " + i);
            }
        }
    }

    private record RunResult(long timeNs, AlgorithmMetrics metrics) {
    }
}

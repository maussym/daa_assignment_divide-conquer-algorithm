package kz.aitu.daa.assignment1;

import java.util.Arrays;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) throws Exception {
        if (args.length > 0 && "experiment".equalsIgnoreCase(args[0])) {
            Experiment.main(Arrays.copyOfRange(args, 1, args.length));
            return;
        }

        int[] mergeInput = {8, 3, 5, 1, 7, 2, 2};
        MergeSorter.sort(mergeInput);
        System.out.println("MergeSort: " + Arrays.toString(mergeInput));

        int[] quickInput = {9, 4, 1, 8, 2, 7, 7};
        QuickSorter.sort(quickInput);
        System.out.println("QuickSort: " + Arrays.toString(quickInput));

        int[] selectInput = {9, 4, 1, 8, 2, 7};
        int k = 2;
        int selected = DeterministicSelector.select(selectInput, k);
        System.out.println("Deterministic Select (k=" + k + "): " + selected);

        Point[] points = {
                new Point(1, 2),
                new Point(8, 9),
                new Point(2, 3),
                new Point(15, 20),
                new Point(5, 4)
        };
        ClosestPairSolver.Result pair = ClosestPairSolver.solve(points);
        System.out.println("Closest Pair: " + pair);
        System.out.println("\nRun `mvn exec:java -Dexec.args=experiment` to regenerate results/results.csv.");
    }
}

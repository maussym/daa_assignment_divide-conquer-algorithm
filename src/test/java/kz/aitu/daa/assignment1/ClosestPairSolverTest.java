package kz.aitu.daa.assignment1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Random;
import org.junit.jupiter.api.Test;

class ClosestPairSolverTest {
    @Test
    void worksForTwoPoints() {
        Point[] points = {new Point(0, 0), new Point(3, 4)};
        assertEquals(5.0, ClosestPairSolver.solve(points).distance(), 0.0);
    }

    @Test
    void rejectsFewerThanTwoPoints() {
        assertThrows(IllegalArgumentException.class,
                () -> ClosestPairSolver.solve(new Point[0]));
        assertThrows(IllegalArgumentException.class,
                () -> ClosestPairSolver.solve(new Point[]{new Point(1, 1)}));
    }

    @Test
    void findsSimpleClosestPair() {
        Point[] points = {
                new Point(1, 2),
                new Point(8, 9),
                new Point(2, 3),
                new Point(15, 20)
        };
        assertEquals(Math.sqrt(2.0), ClosestPairSolver.solve(points).distance(), 1e-12);
    }

    @Test
    void divideAndConquerMatchesBruteForceForRandomSmallDatasets() {
        Random random = new Random(99);
        for (int test = 0; test < 40; test++) {
            int n = 2 + random.nextInt(300); // well below the assignment's n <= 2,000 limit
            Point[] points = new Point[n];
            for (int i = 0; i < n; i++) {
                points[i] = new Point(random.nextDouble() * 10_000, random.nextDouble() * 10_000);
            }
            double expected = ClosestPairSolver.bruteForce(points).distance();
            double actual = ClosestPairSolver.solve(points).distance();
            assertEquals(expected, actual, 1e-9);
        }
    }

    @Test
    void divideAndConquerMatchesBruteForceAtMaximumRequiredSmallSize() {
        Random random = new Random(2_000);
        Point[] points = new Point[2_000];
        for (int i = 0; i < points.length; i++) {
            points[i] = new Point(random.nextDouble() * 1_000_000, random.nextDouble() * 1_000_000);
        }

        double expected = ClosestPairSolver.bruteForce(points).distance();
        double actual = ClosestPairSolver.solve(points).distance();
        assertEquals(expected, actual, 1e-9);
    }

    @Test
    void duplicatePointsGiveZeroDistance() {
        Point p = new Point(3, 4);
        Point[] points = {p, new Point(10, 10), new Point(3, 4)};
        assertEquals(0.0, ClosestPairSolver.solve(points).distance(), 0.0);
    }
}

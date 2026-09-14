package kz.aitu.daa.assignment1;

import java.util.Arrays;
import java.util.Comparator;

/** Divide-and-conquer solution for the closest pair of points problem. */
public final class ClosestPairSolver {
    private static final Comparator<Point> BY_X = Comparator
            .comparingDouble(Point::x)
            .thenComparingDouble(Point::y);

    private static final Comparator<Point> BY_Y = Comparator
            .comparingDouble(Point::y)
            .thenComparingDouble(Point::x);

    private ClosestPairSolver() {
    }

    public static Result solve(Point[] points) {
        return solve(points, new AlgorithmMetrics());
    }

    public static Result solve(Point[] points, AlgorithmMetrics metrics) {
        validate(points);
        if (points.length < 2) {
            throw new IllegalArgumentException("At least two points are required");
        }

        Point[] ordered = points.clone();
        Arrays.sort(ordered, BY_X);

        // Reuse one buffer for merging by y and building the strip.
        Point[] buffer = new Point[points.length];
        metrics.allocations(2);

        return closest(ordered, buffer, 0, ordered.length, 1, metrics);
    }

    /** Slow reference method used by correctness tests. */
    public static Result bruteForce(Point[] points) {
        validate(points);
        if (points.length < 2) {
            throw new IllegalArgumentException("At least two points are required");
        }
        return bruteForceRange(points, 0, points.length, new AlgorithmMetrics());
    }

    /*
     * A range is sorted by x when this method starts.
     * The same range is sorted by y before this method returns.
     */
    private static Result closest(
            Point[] points,
            Point[] buffer,
            int left,
            int right,
            int depth,
            AlgorithmMetrics metrics) {
        metrics.recursiveCall(depth);
        int size = right - left;

        if (size <= 3) {
            Result best = bruteForceRange(points, left, right, metrics);
            Arrays.sort(points, left, right, BY_Y);
            return best;
        }

        int mid = left + size / 2;
        double middleX = points[mid].x();

        Result leftBest = closest(points, buffer, left, mid, depth + 1, metrics);
        Result rightBest = closest(points, buffer, mid, right, depth + 1, metrics);
        Result best = leftBest.distance() <= rightBest.distance() ? leftBest : rightBest;

        // Both halves are now sorted by y, so merge them in linear time.
        mergeByY(points, buffer, left, mid, right, metrics);

        double delta = best.distance();
        int stripSize = 0;
        for (int i = left; i < right; i++) {
            if (Math.abs(points[i].x() - middleX) < delta) {
                buffer[stripSize++] = points[i];
            }
        }

        // The strip is sorted by y. Only the next 7 points are relevant.
        for (int i = 0; i < stripSize; i++) {
            for (int j = i + 1; j < stripSize && j <= i + 7; j++) {
                if (buffer[j].y() - buffer[i].y() >= delta) {
                    break;
                }

                metrics.comparison();
                Result candidate = result(buffer[i], buffer[j]);
                if (candidate.distance() < best.distance()) {
                    best = candidate;
                    delta = candidate.distance();
                }
            }
        }
        return best;
    }

    private static void mergeByY(
            Point[] points,
            Point[] buffer,
            int left,
            int mid,
            int right,
            AlgorithmMetrics metrics) {
        int i = left;
        int j = mid;
        int out = left;

        while (i < mid && j < right) {
            metrics.comparison();
            if (BY_Y.compare(points[i], points[j]) <= 0) {
                buffer[out++] = points[i++];
            } else {
                buffer[out++] = points[j++];
            }
        }
        while (i < mid) {
            buffer[out++] = points[i++];
        }
        while (j < right) {
            buffer[out++] = points[j++];
        }
        System.arraycopy(buffer, left, points, left, right - left);
    }

    private static Result bruteForceRange(
            Point[] points,
            int left,
            int right,
            AlgorithmMetrics metrics) {
        Result best = null;
        for (int i = left; i < right; i++) {
            for (int j = i + 1; j < right; j++) {
                metrics.comparison();
                Result candidate = result(points[i], points[j]);
                if (best == null || candidate.distance() < best.distance()) {
                    best = candidate;
                }
            }
        }
        return best;
    }

    private static Result result(Point first, Point second) {
        double distance = Math.hypot(first.x() - second.x(), first.y() - second.y());
        return new Result(first, second, distance);
    }

    private static void validate(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("points must not be null");
        }
        for (Point point : points) {
            if (point == null) {
                throw new IllegalArgumentException("points must not contain null");
            }
        }
    }

    public record Result(Point first, Point second, double distance) {
        @Override
        public String toString() {
            return first + " <-> " + second + ", distance=" + distance;
        }
    }
}

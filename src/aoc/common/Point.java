package aoc.common;

import org.jetbrains.annotations.NonNls;

import java.util.List;
import java.util.Objects;

/**
 * 2D point.
 */
public class Point {
    private final int x;
    private final int y;
    
    /**
     * Up direction.
     */
    public static final Point UP    = new Point(0, 1);
    /**
     * Down direction.
     */
    public static final Point DOWN  = new Point(0, -1);
    /**
     * Left direction.
     */
    public static final Point LEFT  = new Point(-1, 0);
    /**
     * Right direction.
     */
    public static final Point RIGHT = new Point(1, 0);
    
    /**
     * Creates a new point at origin.
     */
    public Point() {
        this(0, 0);
    }
    
    /**
     * Creates a new point at the specified coordinates.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public Point(final int x, final int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Gets the x coordinate.
     *
     * @return The x coordinate.
     */
    public final int getX() {
        return x;
    }
    
    /**
     * Gets the y coordinate.
     *
     * @return The y coordinate.
     */
    public final int getY() {
        return y;
    }
    
    /**
     * Adds the specified point to this point.
     *
     * @param other The other point to add to this point.
     * @return A new point with the sum of the x and y coordinates of this point and the other point.
     */
    public final Point add(final Point other) {
        return new Point(x + other.x, y + other.y);
    }
    
    /**
     * Gets the Manhattan distance between this point and the specified point.
     *
     * @param other The other point.
     * @return The Manhattan distance between this point and the other point.
     */
    public final int manhattanDistance(final Point other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y);
    }
    
    
    /**
     * Gets all directly neighboring points of this point.
     *
     * @return All directly neighboring points.
     */
    public final List<Point> getNeighbours() {
        return List.of(add(UP), add(RIGHT), add(DOWN), add(LEFT));
    }
    
    /**
     * Gets all adjacent points of this point.
     *
     * @return The neighbours of this point, including diagonals.
     */
    public final List<Point> getAdjacent() {
        return List.of(
                add(UP).add(LEFT),
                add(UP),
                add(UP).add(RIGHT),
                add(LEFT),
                add(RIGHT),
                add(DOWN).add(LEFT),
                add(DOWN),
                add(DOWN).add(RIGHT)
        );
    }
    
    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Point point = (Point) obj;
        return x == point.x && y == point.y;
    }
    
    @Override
    public final int hashCode() {
        return Objects.hash(x, y);
    }
    
    @Override
    public final @NonNls String toString() {
        return "(%d, %d)".formatted(x, y);
    }
}

package aoc.days;

import aoc.common.Day;
import aoc.common.Point;
import org.jetbrains.annotations.NonNls;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <b>Day 09 Rope Bridge:</b><br>
 * Finally, a day where we need a real 2d point class. Through refactoring, the two parts could also be reduced to the
 * same problem, just with different "rope" sizes. But from the top:<br>
 * First we need to keep track of the points visited by the tail (last element of the rope). We do this by using a
 * set of points (removing duplicates). The rope itself is represented by an array of points. Now the rope is moved
 * according to each line of the instruction (simple parsing).<br>
 * The rope is moved by first moving the head (first element of the rope) by the given direction, and on each step
 * aligning the rest of the body one by one. The align method separates the four possible conditions (no movement
 * needed, movement horizontally, movement vertically, movement diagonally).<br>
 * Note that the moveRope method not only returns the newly visited points, but also modifies the rope position.
 */
@NonNls
public final class Day09 implements Day {
    @Override
    public Object part1(final List<String> input) {
        return solveForRopeSize(2, input);
    }
    
    @Override
    public Object part2(final List<String> input) {
        return solveForRopeSize(10, input);
    }
    
    private static int solveForRopeSize(final int ropeSize, final Iterable<String> input) {
        final var tailVisited = new HashSet<Point>();
        final var rope        = new Point[ropeSize];
        Arrays.fill(rope, new Point());
        
        for (final var line : input) {
            final var   parts     = line.split(" ");
            final Point direction = getDirection(parts);
            final var   distance  = Integer.parseInt(parts[1]);
            
            tailVisited.addAll(moveRope(rope, direction, distance));
        }
        
        return tailVisited.size();
    }
    
    private static Point getDirection(final String[] parts) {
        return switch (parts[0]) {
            case "U" -> Point.UP;
            case "D" -> Point.DOWN;
            case "L" -> Point.LEFT;
            case "R" -> Point.RIGHT;
            default -> throw new IllegalArgumentException("Unknown direction: %s".formatted(parts[0]));
        };
    }
    
    private static Set<Point> moveRope(final Point[] rope, final Point direction, final int distance) {
        final var tailVisited = new HashSet<Point>();
        
        for (var i = 0; i < distance; i++) {
            rope[0] = rope[0].add(direction);
            for (var part = 1; part < rope.length; part++) {
                rope[part] = align(rope[part], rope[part - 1]);
            }
            tailVisited.add(rope[rope.length - 1]);
        }
        
        return tailVisited;
    }
    
    @SuppressWarnings("FeatureEnvy")
    private static Point align(final Point tail, final Point head) {
        // If we are close, we do not move the tail.
        if (tail.equals(head) || tail.getAdjacent().contains(head)) {
            return tail;
        }
        // Same x, move vertically.
        if (tail.getX() == head.getX()) {
            return tail.add(tail.getY() < head.getY() ? Point.UP : Point.DOWN);
        }
        // Same y, move horizontally.
        if (tail.getY() == head.getY()) {
            return tail.add(tail.getX() < head.getX() ? Point.RIGHT : Point.LEFT);
        }
        
        // Different x and y, move diagonally.
        return tail.add(tail.getY() < head.getY() ? Point.UP : Point.DOWN)
                   .add(tail.getX() < head.getX() ? Point.RIGHT : Point.LEFT);
    }
}

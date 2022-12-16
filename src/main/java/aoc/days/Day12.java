package aoc.days;

import aoc.common.Day;
import aoc.common.Point;
import org.jetbrains.annotations.NonNls;

import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

/**
 * <b>Day 12: Hill Climbing Algorithm</b><br>
 * &mdash; <i>Hello old friend Dijkstra, I've missed you.</i><br>
 * As the search space is rather small, the concrete implementation (i.e. using a priority queue or a normal queue) does
 * not really matter, but this should be a more or less optimal solution.<br>
 * Part 1 is a straightforward shortest path problem and part 2 just searches for more solutions with different starting
 * points. One thing to consider in part 2 is that some starting points are "islands" in regards of the rules and will
 * not have a solution.<br>
 * However, after finding a solution brute force with all the starting points I realized, that you can just reverse the
 * search and start at the end and find the shortest path to the start. This is much faster for part 2 where you now
 * definitely need a priority queue and can just stop after finding the first "lowest" tile. If you would not use a
 * priority queue, the order of which you check the adjacent tiles would matter and you could find wrong solutions.<br>
 * Now the only difference between part 1 and part 2 is the break-condition in the BFS search.
 *
 * @see <a href="https://adventofcode.com/2022/day/12">Day 12: Hill Climbing Algorithm</a>
 */
@NonNls
public final class Day12 implements Day {
    @Override
    public Object part1(final List<String> input) {
        return getShortestPathReverse(getTrail(input), false);
    }
    
    @Override
    public Object part2(final List<String> input) {
        return getShortestPathReverse(getTrail(input), true);
    }
    
    private static int getShortestPathReverse(final Trail trail, final boolean part2) {
        final var visited = new HashMap<Point, Integer>();
        final var queue   = new PriorityQueue<Node>();
        
        queue.add(new Node(trail.end(), 0));
        
        while (!queue.isEmpty()) {
            final var node = queue.poll();
            
            if (part2 ? trail.map().get(node.point()) == 0 : node.point().equals(trail.start())) {
                return node.distance();
            }
            
            if (visited.getOrDefault(node.point(), Integer.MAX_VALUE) <= node.distance()) {
                continue;
            }
            
            visited.put(node.point(), node.distance());
            
            final var minElevation = trail.map().get(node.point()) - 1;
            
            for (final var p : node.point().getNeighbours()) {
                if (trail.map().getOrDefault(p, Integer.MIN_VALUE) >= minElevation) {
                    queue.add(new Node(p, node.distance() + 1));
                }
            }
        }
        
        throw new IllegalStateException("No solution found");
    }
    
    private static Trail getTrail(final List<String> input) {
        final var map = new HashMap<Point, Integer>();
        
        Point start = null, end = null;
        
        for (int y = 0; y < input.size(); y++) {
            final var line = input.get(y);
            
            for (int x = 0; x < line.length(); x++) {
                var c = line.charAt(x);
                if (c == 'S') { //NON-NLS
                    start = new Point(x, y);
                    c = 'a'; //NON-NLS
                }
                if (c == 'E') { //NON-NLS
                    end = new Point(x, y);
                    c = 'z'; //NON-NLS
                }
                map.put(new Point(x, y), c - 'a'); //NON-NLS
            }
        }
        
        if (start == null || end == null) {
            throw new IllegalStateException("No start or end");
        }
        
        return new Trail(map, start, end);
    }
    
    private record Node(Point point, int distance) implements Comparable<Node> {
        @Override
        public int compareTo(final Node o) {
            return Integer.compare(distance, o.distance);
        }
    }
    
    private record Trail(HashMap<Point, Integer> map, Point start, Point end) {
    }
}

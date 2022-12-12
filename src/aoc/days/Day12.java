package aoc.days;

import aoc.common.Day;
import aoc.common.Point;
import org.jetbrains.annotations.NonNls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * <b>Day 12: Hill Climbing Algorithm</b><br>
 * Hello old friend Dijkstra, I've missed you.<br>
 * As the search space is rather small, the concrete implementation (i.e. using a priority queue or a normal queue) does
 * not really matter, but this should be a more or less optimal solution.<br>
 * Part 1 is a straightforward shortest path problem and part 2 just searches for more solutions with different starting
 * points. One thing to consider in part 2 is that some starting points are "islands" in regards of the rules and will
 * not have a solution.<br>
 * I cannot find a faster solution for part 2 than trying out every possible starting point. Even when limiting the
 * the search by the current best path solution, the runtime increase seems to be due to the amount of possible starting
 * points and not because some paths are long. May be there is another analytical solution to reduce the search space
 * and therefore also the runtime, but as the current solution is still way bellow 1 second runtime (which is the
 * general "cut-off" point for AoC solutions) it is probably ok.
 *
 * @see <a href="https://adventofcode.com/2022/day/12">Day 12: Hill Climbing Algorithm</a>
 */
@NonNls
public final class Day12 implements Day {
    @Override
    public Object part1(final List<String> input) {
        return getShortestPath(getTrail(input));
    }
    
    @Override
    public Object part2(final List<String> input) {
        final var trail = getTrail(input);
        final var starts = trail.map()
                                .entrySet()
                                .stream()
                                .filter(e -> e.getValue() == 0)
                                .map(Map.Entry::getKey)
                                .toArray(Point[]::new);
        
        var bestPath = Integer.MAX_VALUE;
        
        for (final var start : starts) {
            final var currentPath = getShortestPath(new Trail(trail.map(), start, trail.end()));
            
            if (currentPath != -1 && currentPath < bestPath) {
                bestPath = currentPath;
            }
        }
        
        return bestPath;
    }
    
    private static int getShortestPath(final Trail trail) {
        final var visited = new HashMap<Point, Integer>();
        final var queue   = new PriorityQueue<Node>();
        
        queue.add(new Node(trail.start(), 0));
        
        while (!queue.isEmpty()) {
            final var node = queue.poll();
            
            if (visited.containsKey(node.point()) && visited.get(node.point()) <= node.distance()) {
                continue;
            }
            
            visited.put(node.point(), node.distance());
            
            if (node.point().equals(trail.end())) {
                return node.distance();
            }
            
            final var maxDistance = trail.map().get(node.point()) + 1;
            
            for (final var p : node.point().getNeighbours()) {
                if (trail.map().getOrDefault(p, Integer.MAX_VALUE) <= maxDistance) {
                    queue.add(new Node(p, node.distance() + 1));
                }
            }
        }
        
        return -1;
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

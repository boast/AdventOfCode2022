package aoc.days;

import aoc.common.Day;
import aoc.common.Point;
import org.jetbrains.annotations.NonNls;

import java.util.*;

/**
 * <b>Day 24: Blizzard Basin</b><br>
 * Off by one or two...<br>
 * Sadly, today is not a complete solve - yet. I am off by 2 on part 2 and have not yet found the reason for this, as
 * the test case works fine and correct. Even with comparing with actual solutions from reddit I am not sure what is
 * wrong.<br>
 * UPDATE: I found the error, the target position was wrong and I had an additional off-by-one error by not counting the
 * final step correctly. The test case worked by accident.<br>
 * Part 1 and 2 can be done easily by BFS over all possible paths and I calculate the blizzards on each turn. This could
 * be optimized by calculating the blizzards cyclic states, but I do not think this is actually required, as the space
 * is not that big.<br>
 * I will update the code accordingly if I find the reason for the off-by-two error.
 *
 * @see <a href="https://adventofcode.com/2018/day/24">Day 24: Blizzard Basin</a>
 */
@NonNls
public final class Day24 implements Day {
    @Override
    public Object part1(final List<String> input) {
        final var map = parseMap(input);
        
        final var start = new Point(1, 0);
        final var end   = new Point(map.xMax() - 1, map.yMax());
        
        return doTrip(map, start, end, 0);
    }
    
    @Override
    public Object part2(final List<String> input) {
        final var map = parseMap(input);
        
        final var start = new Point(1, 0);
        final var end   = new Point(map.xMax() - 1, map.yMax());
        
        return doTrip(map, start, end, 2);
    }
    
    private static Map parseMap(final List<String> input) {
        final var leftBlizzards  = new ArrayList<Point>();
        final var rightBlizzards = new ArrayList<Point>();
        final var upBlizzards    = new ArrayList<Point>();
        final var downBlizzards  = new ArrayList<Point>();
        final var xMax           = input.get(0).length() - 1;
        final var yMax           = input.size() - 1;
        
        for (var y = 1; y < yMax; y++) {
            final var line = input.get(y);
            for (var x = 1; x < xMax; x++) {
                switch (line.charAt(x)) {
                    case '>' -> rightBlizzards.add(new Point(x, y));
                    case '<' -> leftBlizzards.add(new Point(x, y));
                    case '^' -> upBlizzards.add(new Point(x, y));
                    case 'v' -> downBlizzards.add(new Point(x, y));
                }
            }
        }
        
        return new Map(leftBlizzards, rightBlizzards, upBlizzards, downBlizzards, xMax, yMax);
    }
    
    private static int doTrip(final Map map, final Point start, final Point end, final int stage) {
        var currentMap = map;
        var positions  = Set.of(new Position(start, stage));
        var steps      = 0;
        
        while (true) {
            steps++;
            currentMap = doMove(currentMap);
            
            final var blizzards     = currentMap.getAllBlizzards();
            final var nextPositions = new HashSet<Position>();
            
            for (final var position : positions) {
                final var neighbors = new ArrayList<>(position.point().getNeighbours());
                neighbors.add(position.point());
                
                for (final var neighbor : neighbors) {
                    // Back to start and end is ok
                    if (neighbor.equals(start) || neighbor.equals(end)) {
                        nextPositions.add(new Position(neighbor, position.stage()));
                        continue;
                    }
                    // Grid limits and blizzards ignore
                    if (neighbor.getX() <= 0 ||
                        neighbor.getX() >= map.xMax() ||
                        neighbor.getY() <= 0 ||
                        neighbor.getY() >= map.yMax() ||
                        blizzards.contains(neighbor)) {
                        continue;
                    }
                    
                    nextPositions.add(new Position(neighbor, position.stage()));
                }
            }
            
            positions = new HashSet<>();
            
            for (final var position : nextPositions) {
                if (position.stage() == 2 && position.point().equals(end)) {
                    positions.clear();
                    positions.add(new Position(end, 1));
                    break;
                }
                if (position.stage() == 1 && position.point().equals(start)) {
                    positions.clear();
                    positions.add(new Position(start, 0));
                    break;
                }
                if (position.stage() == 0 && position.point().equals(end)) {
                    return steps;
                }
                positions.add(position);
            }
        }
    }
    
    private record Position(Point point, int stage) {
    }
    
    private static Map doMove(final Map map) {
        final var newLeftBlizzards  = new ArrayList<Point>();
        final var newRightBlizzards = new ArrayList<Point>();
        final var newUpBlizzards    = new ArrayList<Point>();
        final var newDownBlizzards  = new ArrayList<Point>();
        
        for (final var leftBlizzard : map.leftBlizzards()) {
            var next = leftBlizzard.add(Point.ARRAY_LEFT);
            if (next.getX() == 0) {
                next = new Point(map.xMax() - 1, leftBlizzard.getY());
            }
            newLeftBlizzards.add(next);
        }
        for (final var rightBlizzard : map.rightBlizzards()) {
            var next = rightBlizzard.add(Point.ARRAY_RIGHT);
            if (next.getX() == map.xMax()) {
                next = new Point(1, rightBlizzard.getY());
            }
            newRightBlizzards.add(next);
        }
        for (final var upBlizzard : map.upBlizzards()) {
            var next = upBlizzard.add(Point.ARRAY_UP);
            if (next.getY() == 0) {
                next = new Point(upBlizzard.getX(), map.yMax() - 1);
            }
            newUpBlizzards.add(next);
        }
        for (final var downBlizzard : map.downBlizzards()) {
            var next = downBlizzard.add(Point.ARRAY_DOWN);
            if (next.getY() == map.yMax()) {
                next = new Point(downBlizzard.getX(), 1);
            }
            newDownBlizzards.add(next);
        }
        
        return new Map(newLeftBlizzards, newRightBlizzards, newUpBlizzards, newDownBlizzards, map.xMax(), map.yMax());
    }
    
    private record Map(
            List<Point> leftBlizzards,
            List<Point> rightBlizzards,
            List<Point> upBlizzards,
            List<Point> downBlizzards,
            int xMax,
            int yMax
    ) {
        HashSet<Point> getAllBlizzards() {
            final var blizzards = new HashSet<Point>();
            
            blizzards.addAll(leftBlizzards);
            blizzards.addAll(rightBlizzards);
            blizzards.addAll(upBlizzards);
            blizzards.addAll(downBlizzards);
            
            return blizzards;
        }
    }
}

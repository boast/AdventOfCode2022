package aoc.days;

import aoc.common.Day;
import aoc.common.Point;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * <b>Day 14: Regolith Reservoir</b><br>
 * Today again the solution revolves around two problems: first in parsing the input (which I think is solved fine), and
 * then about simulating each grain of sand. The simulation itself is not that tricky and selecting the right bounds is
 * not that hard (I opted for max Y value + 1 in part 1). However part 2 uses some 25k+ sand grains, which is a bit much
 * for a HashMap. It runs fine and very fast, but a more efficient data structure like a simple boolean[][] to represent
 * occupied cells would be faster, but from my point of view harder to read. You would also need to keep track of the
 * count of sand during the simulation and not just count all fields in the end.<br>
 * Interesting enough, the hot path analysis in IntelliJ shows that the Point::hashCode method is the most expensive and
 * could be optimized. Instead of the built in Objects.hash(x, y) I changed it to a simple x * 31 + y and the increased
 * the performance a bit. The built in method has to deal with some arrays as it is a variadic function which can be
 * omitted in this case.<br>
 * After reading some comments in the AoC subreddit, I decided to also try a flood fill algorithm. More or less DFS
 * which I used similarly (BFS/DFS) on different days already and it really seems to be a silver bullet for so many AoC
 * problems.
 *
 * @see <a href="https://adventofcode.com/2022/day/14">Day 14: Regolith Reservoir</a>
 */
@NonNls
public final class Day14 implements Day {
    private static final Pattern ARROW = Pattern.compile(" -> ");
    private static final Pattern COMMA = Pattern.compile(",");
    
    @Override
    public Object part1(final List<String> input) {
        final var map  = getMap(input);
        final var yMax = map.keySet().stream().mapToInt(Point::getY).max().orElse(0);
        
        while (true) {
            var position = new Point(500, 0);
            
            while (true) {
                final var positionNext = position.add(Point.UP);
                
                if (positionNext.getY() > yMax) {
                    return getSandCount(map);
                }
                
                Point tmpPosition = null;
                
                for (final var p : new Point[]{
                        positionNext, positionNext.add(Point.LEFT), positionNext.add(Point.RIGHT)
                }) {
                    if (map.getOrDefault(p, Field.AIR) == Field.AIR) {
                        tmpPosition = p;
                        break;
                    }
                }
                
                if (tmpPosition != null) {
                    position = tmpPosition;
                    continue;
                }
                
                map.put(position, Field.SAND);
                break;
            }
        }
    }
    
    @Override
    public Object part2(final List<String> input) {
        final var map       = getMap(input);
        final var yMax      = map.keySet().stream().mapToInt(Point::getY).max().orElse(0) + 2;
        final var fillQueue = new ArrayDeque<Point>();
        
        fillQueue.add(new Point(500, 0));
        
        while (!fillQueue.isEmpty()) {
            final var point = fillQueue.poll();
            
            if (point.getY() >= yMax || map.getOrDefault(point, Field.AIR) != Field.AIR) {
                continue;
            }
            
            map.put(point, Field.SAND);
            
            fillQueue.add(point.add(Point.UP));
            fillQueue.add(point.add(Point.UP).add(Point.LEFT));
            fillQueue.add(point.add(Point.UP).add(Point.RIGHT));
        }
        
        return getSandCount(map);
    }
    
    private static long getSandCount(final Map<Point, Field> map) {
        return map.values().stream().filter(field -> field == Field.SAND).count();
    }
    
    @NotNull
    private static HashMap<Point, Field> getMap(final Iterable<String> input) {
        final var map = new HashMap<Point, Field>();
        
        for (final var line : input) {
            final var parts = ARROW.split(line);
            for (var i = 0; i < parts.length - 1; i++) {
                final var from = COMMA.split(parts[i]);
                final var to   = COMMA.split(parts[i + 1]);
                
                final var fromX = Integer.parseInt(from[0]);
                final var toX   = Integer.parseInt(to[0]);
                final var fromY = Integer.parseInt(from[1]);
                final var toY   = Integer.parseInt(to[1]);
                
                for (var x = Math.min(fromX, toX); x <= Math.max(fromX, toX); x++) {
                    for (var y = Math.min(fromY, toY); y <= Math.max(fromY, toY); y++) {
                        map.put(new Point(x, y), Field.ROCK);
                    }
                }
            }
        }
        
        return map;
    }
    
    private enum Field {
        AIR, ROCK, SAND,
    }
}

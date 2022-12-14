package aoc.days;

import aoc.common.Day;
import aoc.common.Point;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
 * omitted in this case.
 */
@NonNls
public final class Day14 implements Day {
    private static final Pattern ARROW = Pattern.compile(" -> ");
    private static final Pattern COMMA = Pattern.compile(",");
    
    @Override
    public Object part1(final List<String> input) {
        final var map = getMap(input);
        
        final var yMax      = map.keySet().stream().mapToInt(Point::getY).max().orElse(0) + 1;
        var       isFlowing = true;
        
        while (isFlowing) {
            var position = new Point(500, 0);
            
            while (true) {
                final var positionDown = position.add(Point.UP); // Sand flows in positive Y direction
                
                if (positionDown.getY() >= yMax) {
                    isFlowing = false;
                    break;
                }
                
                final var nextPosition = getNextPosition(map, positionDown);
                
                if (nextPosition != null) {
                    position = nextPosition;
                    continue;
                }
                
                map.put(position, Field.SAND);
                break;
            }
        }
        
        return getSandCount(map);
    }
    
    @Override
    public Object part2(final List<String> input) {
        final var map = getMap(input);
        
        final var bottom    = map.keySet().stream().mapToInt(Point::getY).max().orElse(0) + 2;
        var       isFlowing = true;
        
        while (isFlowing) {
            var position = new Point(500, 0);
            
            while (true) {
                final var positionDown = position.add(Point.UP); // Sand flows in positive Y direction
                
                if (positionDown.getY() >= bottom) {
                    map.put(position, Field.SAND);
                    break;
                }
                
                final var nextPosition = getNextPosition(map, positionDown);
                
                if (nextPosition != null) {
                    position = nextPosition;
                    continue;
                }
                
                map.put(position, Field.SAND);
                
                if (position.equals(new Point(500, 0))) {
                    isFlowing = false;
                }
                
                break;
            }
        }
        
        return getSandCount(map);
    }
    
    private static @Nullable Point getNextPosition(final Map<Point, ? super Field> map, final Point positionDown) {
        final var nextTileDown = map.getOrDefault(positionDown, Field.AIR);
        
        if (nextTileDown == Field.AIR) {
            return positionDown;
        }
        
        final var positionDownLeft = positionDown.add(Point.LEFT);
        final var nextTileDownLeft = map.getOrDefault(positionDownLeft, Field.AIR);
        
        if (nextTileDownLeft == Field.AIR) {
            return positionDownLeft;
        }
        
        final var positionDownRight = positionDown.add(Point.RIGHT);
        final var nextTileDownRight = map.getOrDefault(positionDownRight, Field.AIR);
        
        if (nextTileDownRight == Field.AIR) {
            return positionDownRight;
        }
        
        return null;
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

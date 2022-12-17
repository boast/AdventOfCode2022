package aoc.days;

import aoc.common.Day;
import aoc.common.Point;
import org.jetbrains.annotations.NonNls;

import java.math.BigInteger;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <b>Day 17: Pyroclastic Flow</b><br>
 * Another DP / Memoization problem.<br>
 * The first part can be done simply by following the rules exactly. I think to add a bottom row of rocks is a simple
 * solution to prevent dealing with that special case.<br>
 * The second part is obviously a too large loop count, so I first tried to see if we could find a "tetris" row which
 * would then create another floor, which would allow to find us repetitions. However, this was not working and in the
 * end I printed some output to see, that the pattern of rocks repeats.<br>
 * To find repetitions, we create a "signature" of the last 30 rows, shape & instruction.<br>
 * If we see such a signature, we can immediately repeat the "delta" between the current position and the occurrence of
 * the signature until we are close to the end.<br>
 * The 30 is there to prevent false signatures in edge cases (i.e. the shape would fall through on the side). But for my
 * input it already would work with one single line.<br>
 * The rest is just calculating the delta to the new top and the delta to the current loop. Add all up, done.
 *
 * @see <a href="https://adventofcode.com/2022/day/17">Day 17: Pyroclastic Flow</a>
 */
@NonNls
public final class Day17 implements Day {
    private static final Pattern EMPTY = Pattern.compile("");
    
    @Override
    public Object part1(final List<String> input) {
        return simulate(new BigInteger("2022"), getInstructions(input));
    }
    
    @Override
    public Object part2(final List<String> input) {
        return simulate(new BigInteger("1000000000000"), getInstructions(input));
    }
    
    private static BigInteger simulate(final BigInteger loops, final Point[] instructions) {
        var top       = BigInteger.ZERO;
        var topOffset = BigInteger.ZERO;
        
        final var rocks = new HashSet<Point>();
        final var xMin  = 0;
        final var xMax  = 6;
        
        // We add a floor at the bottom to avoid having to check for that special first case every time
        for (int i = xMin; i <= xMax; i++) {
            rocks.add(new Point(i, 0));
        }
        
        var instructionId = 0;
        
        final var seen = new HashMap<Signature, LoopYMaxPair>();
        
        for (var currentLoop = BigInteger.ZERO; currentLoop.compareTo(loops) < 0; currentLoop = currentLoop.add(
                BigInteger.ONE)) {
            final var shapeId      = currentLoop.mod(BigInteger.valueOf(shapes.length)).intValue();
            var       currentShape = shapes[shapeId];
            final var yMin         = rocks.stream().mapToInt(Point::getY).max().orElse(0);
            
            // Move to initial position
            currentShape = moveShape(currentShape, new Point(2, yMin + 4));
            
            while (true) {
                // Gas push left or right, depending on instruction
                final var currentShapeMovedByGas = moveShape(currentShape,
                                                             instructions[instructionId++ % instructions.length]
                );
                instructionId %= instructions.length;
                
                // Check if shape is still in bounds, if so, move it
                if (currentShapeMovedByGas.stream().noneMatch(p -> p.getX() < xMin || p.getX() > xMax) &&
                    currentShapeMovedByGas.stream().noneMatch(rocks::contains)) {
                    currentShape = currentShapeMovedByGas;
                }
                
                // Move down
                final var currentShapeMovedDown = moveShape(currentShape, Point.DOWN);
                
                // Check if shape collides with rocks, if so, add to rocks and move to next shape
                if (currentShapeMovedDown.stream().anyMatch(rocks::contains)) {
                    rocks.addAll(currentShape);
                    top = BigInteger.valueOf(currentShape.stream().mapToInt(Point::getY).max().orElse(0));
                    break;
                }
                
                // Move shape down
                currentShape = currentShapeMovedDown;
            }
            
            // Part 2
            final var signature = new Signature(getLast30Rows(rocks), shapeId, instructionId);
            
            if (seen.containsKey(signature)) {
                final var loopYMaxPair = seen.get(signature);
                final var deltaTop     = top.subtract(loopYMaxPair.top());
                final var deltaLoop    = currentLoop.subtract(loopYMaxPair.loop());
                final var repeats      = loops.subtract(currentLoop).divide(deltaLoop);
                
                topOffset = topOffset.add(deltaTop.multiply(repeats));
                currentLoop = currentLoop.add(deltaLoop.multiply(repeats));
            }
            
            seen.put(signature, new LoopYMaxPair(currentLoop, top));
        }
        
        return top.add(topOffset);
    }
    
    // Only used to generate a hash
    private record Signature(Set<Point> points, int shapeId, int instructionId) {
    }
    
    // Java, y u no have tuples?
    private record LoopYMaxPair(BigInteger loop, BigInteger top) {
    }
    
    private static Set<Point> getLast30Rows(final Collection<? extends Point> rocks) {
        final var top30Rows = rocks.stream().mapToInt(Point::getY).max().orElse(0) - 30;
        
        return rocks.stream()
                    .filter(p -> p.getY() >= top30Rows)
                    .map(p -> p.add(new Point(0, -top30Rows)))
                    .collect(Collectors.toSet());
    }
    
    private static Point[] getInstructions(final List<String> input) {
        return Arrays.stream(EMPTY.split(input.get(0)))
                     .map(s -> s.equals("<") ? Point.LEFT : Point.RIGHT)
                     .toArray(Point[]::new);
    }
    
    private static List<Point> moveShape(final Collection<? extends Point> shape, final Point direction) {
        return shape.stream().map(p -> p.add(direction)).toList();
    }
    
    private static final List<Point>[] shapes = new List[]{
            // Horizontal line
            new ArrayList<>() {{
                add(new Point(0, 0));
                add(new Point(1, 0));
                add(new Point(2, 0));
                add(new Point(3, 0));
            }},
            // + shape
            new ArrayList<>() {{
                add(new Point(1, 0));
                add(new Point(0, 1));
                add(new Point(1, 1));
                add(new Point(2, 1));
                add(new Point(1, 2));
            }},
            // Reverse L
            new ArrayList<>() {{
                add(new Point(0, 0));
                add(new Point(1, 0));
                add(new Point(2, 0));
                add(new Point(2, 1));
                add(new Point(2, 2));
            }},
            // Vertical line
            new ArrayList<>() {{
                add(new Point(0, 0));
                add(new Point(0, 1));
                add(new Point(0, 2));
                add(new Point(0, 3));
            }},
            // Square
            new ArrayList<>() {{
                add(new Point(0, 0));
                add(new Point(1, 0));
                add(new Point(0, 1));
                add(new Point(1, 1));
            }},
            };
}

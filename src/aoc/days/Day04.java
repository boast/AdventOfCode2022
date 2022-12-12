package aoc.days;

import aoc.common.Day;
import org.jetbrains.annotations.NonNls;

import java.util.Arrays;
import java.util.List;

/**
 * <b>Day 04: Camp Cleanup</b><br>
 * I learned a long time ago not to use regular expressions for parsing scenarios like this, just use split.<br>
 * Simpler to understand, less errors and usually faster because split is optimized for this left/right scenario.<br>
 * Part 1 is straight forward and you do a simple bounds check.<br>
 * Part 2 is easier if you realize that just the start or the end of one range must be contained in the other range.
 *
 * @see <a href="https://adventofcode.com/2022/day/4">Day 04: Camp Cleanup</a>
 */
@NonNls
public final class Day04 implements Day {
    @Override
    public Object part1(final List<String> input) {
        return input.stream().map(line -> {
            final var parts      = line.split(",");
            final var rangeLeft  = Arrays.stream(parts[0].split("-")).mapToInt(Integer::parseInt).toArray();
            final var rangeRight = Arrays.stream(parts[1].split("-")).mapToInt(Integer::parseInt).toArray();
            
            return isContained(rangeLeft, rangeRight) ? 1 : 0;
        }).reduce(0, Integer::sum);
    }
    
    @Override
    public Object part2(final List<String> input) {
        return input.stream().map(line -> {
            final var parts      = line.split(",");
            final var rangeLeft  = Arrays.stream(parts[0].split("-")).mapToInt(Integer::parseInt).toArray();
            final var rangeRight = Arrays.stream(parts[1].split("-")).mapToInt(Integer::parseInt).toArray();
            
            return isOverlapping(rangeLeft, rangeRight) ? 1 : 0;
        }).reduce(0, Integer::sum);
    }
    
    private static boolean isContained(final int[] rangeLeft, final int[] rangeRight) {
        return (rangeLeft[0] <= rangeRight[0] && rangeLeft[1] >= rangeRight[1]) || // right is contained in left
               (rangeLeft[0] >= rangeRight[0] && rangeLeft[1] <= rangeRight[1]);  // left is contained in right
    }
    
    private static boolean isOverlapping(final int[] rangeLeft, final int[] rangeRight) {
        return (rangeLeft[0] <= rangeRight[0] && rangeLeft[1] >= rangeRight[0]) || // right start is contained in left
               (rangeLeft[0] <= rangeRight[1] && rangeLeft[1] >= rangeRight[1]) || // right end is contained in left
               (rangeRight[0] <= rangeLeft[0] && rangeRight[1] >= rangeLeft[0]) || // left start is contained in right
               (rangeRight[0] <= rangeLeft[1] && rangeRight[1] >= rangeLeft[1]);  // left end is contained in right
    }
}

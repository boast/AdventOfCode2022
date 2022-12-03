package aoc.days;

import aoc.common.*;
import org.jetbrains.annotations.NonNls;

import java.util.List;

/**
 * <b>Day 03: Rucksack Reorganization</b><br>
 * Not to hard, always a bit fiddly to do char-int math, but one can use chars directly: {@code i - 'a'}.<br>
 * I also included the list-partition method in a common class, because I think it could be useful in the future.<br>
 * Finally, there are other methods how to find the intersection of two or more lists, but considering that the overlap
 * was guaranteed to be only one element, going by indexOf was simplest (i.e. using
 * {@link java.util.Collection#retainAll(java.util.Collection)}).
 */
@SuppressWarnings("HardCodedStringLiteral")
@NonNls
public final class Day03 implements Day {
    @Override
    public Object part1(final List<String> input) {
        return input.stream().map(line -> {
            final var left  = line.substring(0, line.length() / 2);
            final var right = line.substring(line.length() / 2);
            
            return left.chars().filter(i -> right.indexOf(i) >= 0).findFirst().orElseThrow();
        }).map(Day03::toRucksackValue).reduce(0, Integer::sum);
    }
    
    @Override
    public Object part2(final List<String> input) {
        final var partitions = CollectionUtil.partition(input, 3);
        
        return partitions.stream()
                         .map(lines -> lines.get(0)
                                            .chars()
                                            .filter(i -> lines.get(1).indexOf(i) >= 0 && lines.get(2).indexOf(i) >= 0)
                                            .findFirst()
                                            .orElseThrow())
                         .map(Day03::toRucksackValue)
                         .reduce(0, Integer::sum);
    }
    
    private static int toRucksackValue(final int i) {
        return 1 + (i >= 'a' && i <= 'z' ? i - 'a' : i - 'A' + 26);
    }
}

package aoc.days;

import aoc.common.Day;
import org.jetbrains.annotations.NonNls;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * <b>Day 1: Calorie Counting</b><br>
 * Straight forward, just sort the list and then calculate the score.
 *
 * @see <a href="https://adventofcode.com/2022/day/1">Day 1: Calorie Counting</a>
 */
@NonNls
public final class Day01 implements Day {
    @Override
    public Object part1(final List<String> input) {
        return getSums(input).stream().max(Comparator.comparing(Integer::intValue)).orElseThrow();
    }
    
    @Override
    public Object part2(final List<String> input) {
        return getSums(input).stream()
                .sorted(Comparator.comparing(Integer::intValue).reversed())
                .limit(3L)
                .reduce(0, Integer::sum);
    }
    
    private static List<Integer> getSums(final Iterable<String> input) {
        final var sums = new ArrayList<Integer>();
        
        var current = 0;
        
        for (final var line : input) {
            if (line.isBlank()) {
                sums.add(current);
                current = 0;
                continue;
            }
            
            current += Integer.parseInt(line);
        }
        
        sums.add(current);
        
        return sums;
    }
}

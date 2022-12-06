package aoc.days;

import aoc.common.Day;
import org.jetbrains.annotations.NonNls;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * <b>Day 06: Tuning Trouble</b><br>
 * Again a simple queue/stack problem with different queue sizes. The only tricky part to watch out for is to remove the
 * last element from the queue when the queue is full instead of the first (pop() vs. removeLast()).
 */
@NonNls
public final class Day06 implements Day {
    @Override
    public Object part1(final List<String> input) {
        return findSolution(input.get(0).toCharArray(), 4);
    }
    
    @Override
    public Object part2(final List<String> input) {
        return findSolution(input.get(0).toCharArray(), 14);
    }
    
    private static int findSolution(final char[] signal, final int markerSize) {
        final var signalBuffer = new LinkedList<Character>();
        
        var count = 0;
        
        for (final var c : signal) {
            signalBuffer.push(c);
            count++;
            
            if (signalBuffer.size() < markerSize) {
                continue;
            }
            if (signalBuffer.size() > markerSize) {
                signalBuffer.removeLast();
            }
            if (signalBuffer.stream().distinct().count() == markerSize) {
                return count;
            }
        }
        
        throw new IllegalStateException("No solution found");
    }
    
}

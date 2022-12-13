package aoc.days;

import aoc.common.CollectionUtil;
import aoc.common.Day;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NonNls;

import java.util.*;
import java.util.regex.Pattern;

/**
 * <b>Day 13: Distress Signal</b><br>
 * I love queues.<br>
 * Now here there are two challenges and if you do part 1 right, part 2 is actually trivial to solve.<br>
 * The first challenge is to parse the input into a very generic structure. After a first implementation using queues, I
 * got the idea that this is valid JSON and can be parsed into a generic structure using Gson.<br>
 * The second challenge is a classical compare operation (-1, 0, 1) which works recursively. The compare step is
 * separated according to instructions, first trying to compare integers, then optionally wrapping a single integer in
 * a new queue and then comparing the queues recursively. One thing to keep in mind is not to actually modify the values
 * and always work on clones / new items.<br>
 * Finally, the sorting operation can actually work with native Java methods which will use optimized sorting algorithms
 * under the hood.
 *
 * @see <a href="https://adventofcode.com/2022/day/13">Day 13: Distress Signal</a>
 */
@NonNls
public final class Day13 implements Day {
    private static final Pattern DIGIT = Pattern.compile("\\d");
    
    @Override
    public Object part1(final List<String> input) {
        final var g           = new Gson();
        var       packetSum   = 0;
        var       packetIndex = 1;
        
        for (final var packets : CollectionUtil.partition(input, 3)) {
            final var left  = g.fromJson(packets.get(0), Object.class);
            final var right = g.fromJson(packets.get(1), Object.class);
            
            if (comparePacket(left, right) == -1) {
                packetSum += packetIndex;
            }
            
            packetIndex++;
        }
        
        return packetSum;
    }
    
    @Override
    public Object part2(final List<String> input) {
        final var g       = new Gson();
        final var packets = new ArrayList<>();
        
        for (final var packet : input) {
            if (packet.isEmpty()) {
                continue;
            }
            packets.add(g.fromJson(packet, Object.class));
        }
        
        final var dividerPacket1 = g.fromJson("[[2]]", Object.class);
        final var dividerPacket2 = g.fromJson("[[6]]", Object.class);
        
        packets.add(dividerPacket1);
        packets.add(dividerPacket2);
        
        packets.sort(Day13::comparePacket);
        
        return (packets.indexOf(dividerPacket1) + 1) * (packets.indexOf(dividerPacket2) + 1);
    }
    
    private static int comparePacket(final Object left, final Object right) {
        // If both values are integers, the lower integer should come first. If the left integer is lower than the right
        // integer, the inputs are in the right order. If the left integer is higher than the right integer, the inputs
        // are not in the right order. Otherwise, the inputs are the same integer; continue checking the next part of
        // the input.
        
        if (left instanceof Double && right instanceof Double) { // Gson converts all numbers to Double
            return Double.compare((Double) left, (Double) right);
        }
        
        // If exactly one value is an integer, convert the integer to a list which contains that integer as its only
        // value, then retry the comparison.
        
        //noinspection all
        final var leftQueue = left instanceof ArrayList ? new ArrayDeque<>((ArrayList) left) : new ArrayDeque<>() {
            {
                add(left);
            }
        };
        //noinspection all
        final var rightQueue = right instanceof ArrayList ? new ArrayDeque<>((ArrayList) right) : new ArrayDeque<>() {
            {
                add(right);
            }
        };
        
        // If both values are lists, compare the first value of each list, then the second value, and so on. If the left
        // list runs out of items first, the inputs are in the right order. If the right list runs out of items first,
        // the inputs are not in the right order. If the lists are the same length and no comparison makes a decision
        // about the order, continue checking the next part of the input.
        
        while (true) {
            if (leftQueue.isEmpty() || rightQueue.isEmpty()) {
                return Integer.compare(leftQueue.size(), rightQueue.size());
            }
            
            final var leftValue  = leftQueue.poll();
            final var rightValue = rightQueue.poll();
            
            final var compare = comparePacket(leftValue, rightValue);
            
            if (compare != 0) {
                return compare;
            }
        }
    }
}

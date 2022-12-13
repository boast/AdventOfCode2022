package aoc.days;

import aoc.common.CollectionUtil;
import aoc.common.Day;
import org.jetbrains.annotations.NonNls;

import java.util.*;
import java.util.regex.Pattern;

/**
 * <b>Day 13: Distress Signal</b><br>
 * I love queues.<br>
 * Now here there are two challenges and if you do part 1 right, part 2 is actually trivial to solve.<br>
 * The first challenge is to parse the input into a very generic structure. I opted for a queue of objects, which inside
 * are really just more queues or actual integers. You absolutely have to disable inspections if you program that
 * generic or your IDE will scream at you.<br>
 * The parser works recursively through the input, which is also split to each character and consumed by the parsing
 * operations as a queue (stream).<br>
 * The second challenge is a classical compare operation (-1, 0, 1) which again works recursively. The compare step is
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
    
    private static final Pattern EMPTY = Pattern.compile("");
    private static final Pattern DIGIT = Pattern.compile("\\d");
    
    @Override
    public Object part1(final List<String> input) {
        var packetSum   = 0;
        var packetIndex = 1;
        
        for (final var packets : CollectionUtil.partition(input, 3)) {
            final var left  = parsePacket(new ArrayDeque<>(Arrays.asList(EMPTY.split(packets.get(0)))));
            final var right = parsePacket(new ArrayDeque<>(Arrays.asList(EMPTY.split(packets.get(1)))));
            
            if (comparePacket(left, right) == -1) {
                packetSum += packetIndex;
            }
            
            packetIndex++;
        }
        
        return packetSum;
    }
    
    @Override
    public Object part2(final List<String> input) {
        final var packets = new ArrayList<ArrayDeque<Object>>();
        
        for (final var packet : input) {
            if (packet.isEmpty()) {
                continue;
            }
            packets.add(parsePacket(new ArrayDeque<>(Arrays.asList(EMPTY.split(packet)))));
        }
        
        final var dividerPacket1 = parsePacket(new ArrayDeque<>(Arrays.asList(EMPTY.split("[[2]]"))));
        final var dividerPacket2 = parsePacket(new ArrayDeque<>(Arrays.asList(EMPTY.split("[[6]]"))));
        
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
        
        if (left instanceof Integer && right instanceof Integer) {
            return Integer.compare((Integer) left, (Integer) right);
        }
        
        // If exactly one value is an integer, convert the integer to a list which contains that integer as its only
        // value, then retry the comparison.
        
        //noinspection all
        final var leftQueue = left instanceof ArrayDeque ? new ArrayDeque<>((ArrayDeque) left) : new ArrayDeque<>() {
            {
                add(left);
            }
        };
        //noinspection all
        final var rightQueue = right instanceof ArrayDeque ? new ArrayDeque<>((ArrayDeque) right) : new ArrayDeque<>() {
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
    
    private static ArrayDeque<Object> parsePacket(final ArrayDeque<String> packetData) {
        final var result = new ArrayDeque<>();
        
        // A packet starts with a '[', with poll() we consume it
        if (!Objects.equals(packetData.poll(), "[")) {
            throw new IllegalArgumentException("Expected '['");
        }
        
        var digitBuffer = new StringBuilder(2);
        
        while (!Objects.equals(packetData.peek(), "]")) {
            // We have a sub-package, recurse
            if (Objects.equals(packetData.peek(), "[")) {
                result.add(parsePacket(packetData));
                // More follow, remove the comma
                if (Objects.equals(packetData.peek(), ",")) {
                    packetData.poll();
                }
                
                continue;
            }
            
            // Now the token is either a digit or a comma, the digits needs to be buffered and then added
            final var token = packetData.poll();
            assert token != null;
            
            if (DIGIT.matcher(token).matches()) {
                digitBuffer.append(token);
            } else if (token.equals(",")) {
                result.add(Integer.parseInt(digitBuffer.toString()));
                digitBuffer = new StringBuilder(2);
            }
        }
        // Check the digit buffer for the last digit
        if (!digitBuffer.isEmpty()) {
            result.add(Integer.parseInt(digitBuffer.toString()));
        }
        
        // A packet ends with a ']', with poll() we consume it
        if (!Objects.equals(packetData.poll(), "]")) {
            throw new IllegalArgumentException("Expected ']'");
        }
        
        return result;
    }
}

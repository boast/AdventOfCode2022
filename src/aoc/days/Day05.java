package aoc.days;

import aoc.common.Day;
import org.jetbrains.annotations.NonNls;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <b>Day 05: Supply Stacks</b><br>
 * The name already gives it away: this is a queue / stack problem. We use some helper methods (build*) to build our
 * instructions, queues and eventual moves as generic as possible, so we can use them for the test cases as well (they
 * are shorter in size and length).<br>
 * The first part is a simple dequeue / enqueue problem (also called push / pop). Java has the doubly-linked list and
 * this is perfect for this task ({@link LinkedList}).<br>
 * The second part uses another helper queue in-between the two queues, which in turn reverses the order of the elements
 * automatically (LIFO).
 */
@NonNls
public final class Day05 implements Day {
    
    @Override
    public Object part1(final List<String> input) {
        final var instructions = buildInstructions(input);
        final var queues       = buildQueues(instructions);
        final var moves        = buildMoves(input, (long) instructions.size() + 2);
        
        for (final var move : moves) {
            var       count          = Integer.parseInt(move[1]);
            final var fromQueueIndex = Integer.parseInt(move[3]) - 1;
            final var toQueueIndex   = Integer.parseInt(move[5]) - 1;
            
            while (count-- > 0) {
                queues.get(toQueueIndex).push(queues.get(fromQueueIndex).pop());
            }
        }
        
        return buildFinalQueueString(queues);
    }
    
    @Override
    public Object part2(final List<String> input) {
        final var instructions = buildInstructions(input);
        final var queues       = buildQueues(instructions);
        final var moves        = buildMoves(input, (long) instructions.size() + 2);
    
        for (final var move : moves) {
            var       count          = Integer.parseInt(move[1]);
            final var fromQueueIndex = Integer.parseInt(move[3]) - 1;
            final var toQueueIndex   = Integer.parseInt(move[5]) - 1;
            
            final var tmpList = new LinkedList<Character>();
        
            while (count-- > 0) {
                tmpList.push(queues.get(fromQueueIndex).pop());
            }
            
            while (!tmpList.isEmpty()) {
                queues.get(toQueueIndex).push(tmpList.pop());
            }
        }
    
        return buildFinalQueueString(queues);
    }
    
    private static List<String> buildInstructions(final Collection<String> input) {
        final var instructions = input.stream().takeWhile(line -> !line.startsWith(" 1 ")).collect(Collectors.toList());
        Collections.reverse(instructions);
        
        return instructions;
    }
    
    private static ArrayList<LinkedList<Character>> buildQueues(final Iterable<String> instructions) {
        final var queues = new ArrayList<LinkedList<Character>>();
        
        for (final var instruction : instructions) {
            final var length = instruction.length();
            final var chars  = instruction.toCharArray();
            var       queue  = 0;
            
            for (var i = 0; i <= length; i += 4) {
                if (chars[i] == '[') {
                    if (queues.size() <= queue) {
                        queues.add(new LinkedList<>());
                    }
                    queues.get(queue).push(chars[i + 1]);
                }
                queue++;
            }
        }
        
        return queues;
    }
    
    private static List<String[]> buildMoves(final Collection<String> input, final long skip) {
        return input.stream().skip(skip).map(line -> line.split(" ")).toList();
    }
    
    private static String buildFinalQueueString(final Collection<? extends LinkedList<Character>> queues) {
        final var sb = new StringBuilder(queues.size());
    
        for (final var queue : queues) {
            sb.append(queue.getFirst());
        }
    
        return sb.toString();
    }
}

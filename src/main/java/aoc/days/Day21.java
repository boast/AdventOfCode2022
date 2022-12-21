package aoc.days;

import aoc.common.Day;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <b>Day 21: Monkey Math</b><br>
 * Recursion and solving a problem from the end.<br>
 * Part 1 was parsing some input, creating some recursion and I already implemented some sort of shortcut, to prevent
 * possible recalculations if we hit a node we already calculated. Part 2 was then a bit tricky, I tried first to
 * simplify the tree with all know values (stopping when the tree did not shrink any more), but the solution space being
 * a {@link Long} made this not feasible.<br>
 * The eventual solution was again similar to day 12, where we come from the "back" and start with our unknown value,
 * calculate the possible values on each step. Part 1 comes in handy to pre-calculate all known nodes for each side,
 * with some slight modifications to the eval functions to handle the "null" cases, which flag the values where we
 * reverse the operation given.<br>
 * I used some good old pen and paper to define the reversed operations, as GitHub Copilot did it the wrong way
 * around...<br>
 * After that, it's a simple tree traversal to resolve either the left or right side of each equation until we hit our
 * final "humn" node.
 *
 * @see <a href="https://adventofcode.com/2020/day/21">Day 21: Monkey Math</a>
 */
@NonNls
public final class Day21 implements Day {
    private static final Pattern COMMA_AND_SPACE = Pattern.compile(": ");
    private static final Pattern SPACE           = Pattern.compile(" ");
    
    @Override
    public Object part1(final List<String> input) {
        final var monkeys = parseMonkeys(input);
        return evalMonkey("root", monkeys);
    }
    
    @Override
    public Object part2(final List<String> input) {
        monkeyStore.clear();
    
        //noinspection SpellCheckingInspection
        final var humn = "humn"; //NON-NLS
        final var monkeys = parseMonkeys(input);
        final var parts = SPACE.split(monkeys.get("root"));
        
        monkeys.put(humn, null);
        
        final var left  = parts[0];
        final var right = parts[2];
        
        // We can use part1 to recursively evaluate the left and right sides of the equation and solve one side already.
        evalMonkey(left, monkeys);
        evalMonkey(right, monkeys);
        
        // One of them ***must*** be resolved, because if not, this equation is not solvable.
        var monkey = monkeyStore.get(left) != null ? right : left;
        var value  = monkeyStore.get(left) != null ? monkeyStore.get(left) : monkeyStore.get(right);
        
        // Now we reduce the tree of operations until we get to our human node.
        while (!monkey.equals(humn)) {
            final var currentParts    = SPACE.split(monkeys.get(monkey));
            final var currentLeft     = currentParts[0];
            final var currentOperator = currentParts[1];
            final var currentRight    = currentParts[2];
            
            // Again, one side of the operation ***must*** already be solved, because again it would be not solvable.
            if (monkeyStore.get(currentLeft) == null) {
                monkey = currentLeft;
                value = reverseCalculateLeft(currentOperator, value, monkeyStore.get(currentRight));
            } else {
                monkey = currentRight;
                value = reverseCalculateRight(currentOperator, value, monkeyStore.get(currentLeft));
            }
        }
        
        return value;
    }
    
    private static Map<String, String> parseMonkeys(final Collection<String> input) {
        return input.stream()
                    .map(COMMA_AND_SPACE::split)
                    .collect(Collectors.toMap(split -> split[0], split -> split[1]));
    }
    
    private static final Map<String, Long> monkeyStore = new HashMap<>();
    
    private static @Nullable Long evalMonkey(final String monkey, final Map<String, String> monkeys) {
        if (monkeyStore.containsKey(monkey)) {
            return monkeyStore.get(monkey);
        }
        
        final var value = monkeys.get(monkey);
        
        if (value == null) {
            monkeyStore.put(monkey, null);
            return null;
        }
        
        var longValue = tryParse(value);
        
        if (longValue != null) {
            monkeyStore.put(monkey, longValue);
            return longValue;
        }
        
        final var parts    = SPACE.split(value);
        final var operator = parts[1];
        final var left     = evalMonkey(parts[0], monkeys);
        final var right    = evalMonkey(parts[2], monkeys);
        
        if (left == null || right == null) {
            monkeyStore.put(monkey, null);
            return null;
        }
        
        longValue = calculate(operator, left, right);
        monkeyStore.put(monkey, longValue);
        
        return longValue;
    }
    
    private static Long calculate(final String operator, final long left, final long right) {
        return switch (operator) {
            case "+" -> left + right;
            case "-" -> left - right;
            case "*" -> left * right;
            case "/" -> left / right;
            default -> throw new IllegalStateException("Unexpected value: %s".formatted(operator));
        };
    }
    
    // Reverse if the current operand was on the left side.
    private static Long reverseCalculateLeft(final String operator, final long value, final long operand) {
        return switch (operator) {
            case "+" -> value - operand;
            case "-" -> value + operand;
            case "*" -> value / operand;
            case "/" -> value * operand;
            default -> throw new IllegalStateException("Unexpected value: %s".formatted(operator));
        };
    }
    
    // Reverse if the current operand was on the right side.
    private static Long reverseCalculateRight(final String operator, final long value, final long operand) {
        return switch (operator) {
            case "+" -> value - operand;
            case "-" -> operand - value;
            case "*" -> value / operand;
            case "/" -> operand / value;
            default -> throw new IllegalStateException("Unexpected value: %s".formatted(operator));
        };
    }
    
    // Java, y u no help programmers with this?
    private static @Nullable Long tryParse(final String text) {
        try {
            return Long.parseLong(text);
        } catch (final NumberFormatException e) {
            return null;
        }
    }
}

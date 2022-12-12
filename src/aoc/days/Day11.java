package aoc.days;

import aoc.common.CollectionUtil;
import aoc.common.Day;
import org.jetbrains.annotations.NonNls;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <b>Day 11: Monkey in the Middle</b><br>
 * We are getting into a real AoC problem here. The first part is easy, but the main objective was to parse that input
 * in a more or less sane way.<br>
 * The second part needed some thinking and a realisation, that you can reduce those values in size by taking the LCM
 * of all values. A hint to this method is given as all the divisors are prime numbers, so the LCM is also the simple
 * product of all numbers.<br>
 *
 * @see <a href="https://adventofcode.com/2022/day/11">Day 11: Monkey in the Middle</a>
 */
@NonNls
public final class Day11 implements Day {
    @Override
    public Object part1(final List<String> input) {
        final var monkeys = parseMonkeys(input);
        var       rounds  = 20;
        
        while (rounds-- > 0) {
            monkeys.forEach(Monkey::inspectPart1);
        }
        
        final var sortedMonkeys = sortMonkeys(monkeys);
        return sortedMonkeys[0] * sortedMonkeys[1];
    }
    
    @Override
    public Object part2(final List<String> input) {
        final var monkeys = parseMonkeys(input);
        var       rounds  = 10_000;
        
        // Note: LCM is only the multiple of all numbers if they are prime, otherwise the algorithm is more complicated.
        final var lcm = monkeys.stream().map(Monkey::getTest).reduce(1L, (i1, i2) -> i1 * i2);
        
        while (rounds-- > 0) {
            monkeys.forEach(monkey -> monkey.inspectPart2(lcm));
        }
        
        final var sortedMonkeys = sortMonkeys(monkeys);
        return sortedMonkeys[0] * sortedMonkeys[1];
    }
    
    private static ArrayList<Monkey> parseMonkeys(final List<String> input) {
        final var monkeys = new ArrayList<Monkey>();
        
        for (final var lines : CollectionUtil.partition(input, 7)) {
            final var items = Arrays.stream(lines.get(1).trim().replace("Starting items: ", "").split(", ")) //NON-NLS
                                    .map(Long::parseLong).collect(Collectors.toCollection(ArrayList::new));
            final var operationString = lines.get(2).trim().replace("Operation: new = old ", ""); //NON-NLS
            
            final Function<Long, Long> operation;
            
            if (operationString.startsWith("* old")) { //NON-NLS
                operation = old -> old * old;
            } else if (operationString.startsWith("+")) { //NON-NLS
                operation = old -> old + Integer.parseInt(operationString.substring(2));
            } else {
                operation = old -> old * Integer.parseInt(operationString.substring(2));
            }
            
            final var test = Integer.parseInt(lines.get(3).trim().replace("Test: divisible by ", "")); //NON-NLS
            
            final var testTrue = Integer.parseInt(lines.get(4)
                                                       .trim()
                                                       .replace("If true: throw to monkey ", "")); //NON-NLS
            final var testFalse = Integer.parseInt(lines.get(5)
                                                        .trim()
                                                        .replace("If false: throw to monkey ", "")); //NON-NLS
            
            monkeys.add(new Monkey(monkeys, items, operation, test, testTrue, testFalse));
        }
        
        return monkeys;
    }
    
    private static Long[] sortMonkeys(final Collection<? extends Monkey> monkeys) {
        return monkeys.stream()
                      .map(Monkey::getInspectionCount)
                      .sorted(Comparator.comparing(Long::longValue).reversed())
                      .toArray(Long[]::new);
    }
    
    private static class Monkey {
        private final List<Monkey>                 monkeys;
        @SuppressWarnings("CollectionDeclaredAsConcreteClass")
        private final ArrayList<Long>              items;
        private final Function<? super Long, Long> operation;
        private final int                          test;
        private final int                          testTrue;
        private final int                          testFalse;
        
        private long inspectionCount = 0L;
        
        Monkey(final List<Monkey> monkeys, final ArrayList<Long> items, final Function<? super Long, Long> operation, final int test, final int testTrue, final int testFalse) {
            this.monkeys = monkeys;
            this.items = items;
            this.operation = operation;
            this.test = test;
            this.testTrue = testTrue;
            this.testFalse = testFalse;
        }
        
        final void inspectPart1() {
            for (var item : items) {
                item = operation.apply(item);
                item = item / 3;
                
                monkeys.get(item % test == 0 ? testTrue : testFalse).items.add(item);
                inspectionCount++;
            }
            
            items.clear();
        }
        
        final void inspectPart2(final long lcm) {
            for (var item : items) {
                item = operation.apply(item) % lcm;
                
                monkeys.get(item % test == 0 ? testTrue : testFalse).items.add(item);
                inspectionCount++;
            }
            
            items.clear();
        }
        
        final long getInspectionCount() {
            return inspectionCount;
        }
        
        final long getTest() {
            return test;
        }
    }
}

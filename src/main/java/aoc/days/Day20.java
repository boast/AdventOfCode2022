package aoc.days;

import aoc.common.Day;
import org.jetbrains.annotations.NonNls;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * <b>Day 20: Grove Positioning System</b><br>
 * Oh those darn duplicates.<br>
 * Super frustrating part 1, as I realized very fast, that you can calculate the index directly by adding the value to
 * the current index and simply take the mod of the size of the list (corrected by again the size of the list if the
 * resulting index should be negative - the same thing as {@link Math#floorMod} does in one go).<br>
 * Remember kids: know your languages Math library.<br>
 * That worked very well with the test data, but not with the real data as it contained <b>duplicate</b> values! This
 * took me a literal hour to find, and the solution is to create a simple record that holds the value and the index, so
 * all the collection hash methods and comparison methods work as expected.<br>
 * Note that a solution with {@link java.util.Collections#rotate} would may work for part 1, but not for part 2 as
 * it modifies the whole sublist from current index to target index, so it would be too slow for part 2.<br>
 * Part 2 was straight forward, as I already calculated the positions in one go, so doing everything with big numbers
 * and all of it 10 times did not really matter, except I had to adapt the datatype to {@link Long}.
 *
 * @see <a href="https://adventofcode.com/2020/day/20">Day 20: Grove Positioning System</a>
 */
@NonNls
public final class Day20 implements Day {
    @Override
    public Object part1(final List<String> input) {
        final var original = parseNumbers(input);
        final var numbers  = new ArrayList<>(original);
        
        mix(original, numbers);
        
        return getCoordinates(numbers);
    }
    
    @Override
    public Object part2(final List<String> input) {
        final var decryptionKey = 811589153L;
        final var original = parseNumbers(input).stream()
                                                .map(indexedNumber -> new IndexedNumber(indexedNumber.value() *
                                                                                        decryptionKey,
                                                                                        indexedNumber.index()
                                                ))
                                                .toList();
        final var numbers = new ArrayList<>(original);
        
        for (int i = 0; i < 10; i++) {
            mix(original, numbers);
        }
        
        return getCoordinates(numbers);
    }
    
    private static long getCoordinates(final List<IndexedNumber> numbers) {
        final int indexOfZero = numbers.indexOf(numbers.stream()
                                                       .filter(num -> num.value() == 0)
                                                       .findFirst()
                                                       .orElseThrow());
        
        return numbers.get((indexOfZero + 1000) % numbers.size()).value() +
               numbers.get((indexOfZero + 2000) % numbers.size()).value() +
               numbers.get((indexOfZero + 3000) % numbers.size()).value();
    }
    
    private static void mix(final Iterable<IndexedNumber> original, final List<? super IndexedNumber> numbers) {
        for (final var number : original) {
            final var currentIndex = numbers.indexOf(number);
            final var nextIndex    = number.value() + currentIndex;
            
            numbers.remove(currentIndex);
            
            /*
             * Math.floorMod() is equal to the following logic in our case (it does also some very handy stuff with a
             * negative modulus, but our modulus is always positive):
             * <code>
             * var modIndex = (int)(nextIndex % numbers.size());
             * if (modIndex <= 0) {
             *     modIndex += numbers.size();
             * }
             * numbers.add(modIndex, number);
             * </code>
             */
            numbers.add(Math.floorMod(nextIndex, numbers.size()), number); // List#add(index, element) inserts the value
        }
    }
    
    private static List<IndexedNumber> parseNumbers(final List<String> input) {
        // Cool, rider suggested to replace my for-loop with the IntStream.range() method.
        return IntStream.range(0, input.size())
                        .mapToObj(i -> new IndexedNumber(Long.parseLong(input.get(i)), i))
                        .toList();
    }
    
    // We need a way to differentiate between entries with the same value, so we add the index to the record which will
    // then be used in compares and hash values automatically.
    private record IndexedNumber(long value, int index) {
    }
}

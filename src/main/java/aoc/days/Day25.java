package aoc.days;

import aoc.common.Day;
import org.jetbrains.annotations.NonNls;

import java.util.List;

/**
 * <b>Day 25: Full of Hot Air</b><br>
 * Base 5 and a half.<br>
 * A nice little modulus problem to finish off. The {@link #fromSNAFU(String)} method is straight forward and I think
 * there is no other sane way to do. The {@link #toSNAFU(long)} method is a bit more interesting. If you ever wrote a
 * base conversion algorithm, you should have seen this coming (in our case, we do a special treatment for the -2 and -1
 * case, which normally would be 3 and 4 in base 5 and just be subtracted from the initial sum). Nice to see, that even
 * with negative values this algorithm works.<br>
 * What a strong year of AoC, some very very hard problem in the middle, but overall a lot of fun.<br>
 * See you in 2023!
 *
 * @see <a href="https://adventofcode.com/2015/day/25">Day 25: Full of Hot Air</a>
 */
@NonNls
public final class Day25 implements Day {
    @Override
    public Object part1(final List<String> input) {
        return toSNAFU(input.stream().mapToLong(Day25::fromSNAFU).sum());
    }
    
    @Override
    public Object part2(final List<String> input) {
        return "Merry Xmas!";
    }
    
    private static long fromSNAFU(final String s) {
        var sum = 0L;
        for (int i = 0; i < s.length(); i++) {
            //noinspection NumericCastThatLosesPrecision
            final var pow5 = (long) Math.pow(5, s.length() - i - 1);
            sum += pow5 * switch (s.charAt(i)) {
                case '=' -> -2;
                case '-' -> -1;
                case '0' -> 0;
                case '1' -> 1;
                case '2' -> 2;
                default -> throw new IllegalArgumentException("Invalid character: %s".formatted(s.charAt(i)));
            };
        }
        return sum;
    }
    
    private static String toSNAFU(final long l) {
        //noinspection NumericCastThatLosesPrecision
        final var sb  = new StringBuilder((int) Math.log10(l) * 2);
        var       sum = l;
        
        while (sum != 0) {
            final var mod = (int) (sum % 5);
            switch (mod) {
                case 0 -> sb.append('0'); // sum += 0;
                case 1 -> {
                    sb.append('1');
                    sum -= 1;
                }
                case 2 -> {
                    sb.append('2');
                    sum -= 2;
                }
                case 3 -> {
                    sb.append('=');
                    sum += 2;
                }
                case 4 -> {
                    sb.append('-');
                    sum += 1;
                }
                default -> throw new IllegalArgumentException("Invalid mod: %s".formatted(mod));
            }
            sum /= 5;
        }
        
        return sb.reverse().toString();
    }
}

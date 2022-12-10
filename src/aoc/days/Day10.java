package aoc.days;

import aoc.common.Day;
import org.jetbrains.annotations.NonNls;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <b>Day 10: Cathode-Ray Tube</b><br>
 * Not really a full CPU with an ALU like in other years, but it brought me one idea immediately which helped me to
 * solve the problem more granular: do each "cycle" really separately and only pass the CPU registers & the clock.<br>
 * Part 1 is now reduced to a small modulus calculation and is not that hard to understand.<br>
 * Part 2 is now in the same spirit, just with some string building in between and a nice little "sliding window"
 * evaluation with that CRT and sprite drawing position.<br>
 * I also learned that in Java passed functions do not "capture" primitive types, so I use {@link AtomicInteger} in part
 * 1, in part 2 we use a string builder anyway.<br>
 * Note that the extraction of the process method and the generalization to a CycleFunction was done in a small
 * refactoring step to reduce the code duplication for both parts.
 */
@NonNls
public final class Day10 implements Day {
    
    @Override
    public Object part1(final List<String> input) {
        final var signalStrength = new AtomicInteger();
        final var clockOffset    = 20;
        final var clockModulus   = 40;
        
        process(input, (clock, x)  -> {
            if ((clock + clockOffset) % clockModulus == 0) {
                signalStrength.addAndGet(x * clock);
            }
        });
        
        return signalStrength.get();
    }
    
    @Override
    public Object part2(final List<String> input) {
        final var sb       = new StringBuilder(6 * 41 + 1);
        final var crtWidth = 40;
        
        process(input, (clock, x) -> {
            final var crtPosition    = (clock - 1) % crtWidth;
            final var spritePosition = x % crtWidth;
            
            if (crtPosition == 0) {
                sb.append("\n");
            }
            
            sb.append(spritePosition >= crtPosition - 1 && spritePosition <= crtPosition + 1 ? "#" : ".");
        });
        
        sb.append("\n");
        
        return sb.toString();
    }
    
    @FunctionalInterface
    private interface ClockCycleFunction {
        void tick(final int clock, final int x);
    }
    
    private static void process(final Iterable<String> input, final ClockCycleFunction clockCycleFunction) {
        var clock = 0;
        var x = 1;
        
        for (final var line : input) {
            final var split = line.split(" ");
            
            clockCycleFunction.tick(++clock, x);
            
            if (split[0].equals("addx")) { //NON-NLS
                clockCycleFunction.tick(++clock, x);
                x += Integer.parseInt(split[1]);
            }
        }
    }
}

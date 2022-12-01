package aoc;

import aoc.common.Day;
import aoc.days.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Advent of Code 2022
 */
final class App {
    private static final Map<Integer, Day> DAYS;
    
    static {
        DAYS = new HashMap<>();
        DAYS.put(1, new Day01());
        DAYS.put(2, new Day02());
    }
    
    /**
     * Main entry point
     *
     * @param args Command line arguments
     * @throws IOException If an I/O error occurs
     */
    public static void main(final String[] args) throws IOException {
        final var day = args.length > 0 ? Integer.parseInt(args[0]) : 1;
        
        System.out.println(MessageFormat.format("AoC 2022 - Day {0}", day)); //NON-NLS
        
        final var paddedDay = String.format("%02d", day); //NON-NLS
        final var path      = Paths.get("resources", "day" + paddedDay + ".txt");
        final var input     = Files.readAllLines(path);
        
        System.out.println(MessageFormat.format("Part 1: {0}", DAYS.get(day).part1(input))); //NON-NLS
        System.out.println(MessageFormat.format("Part 2: {0}", DAYS.get(day).part2(input))); //NON-NLS
    }
}

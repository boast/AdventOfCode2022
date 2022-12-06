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
@SuppressWarnings("HardCodedStringLiteral")
final class App {
    private static final Map<Integer, Day> DAYS;
    
    static {
        DAYS = new HashMap<>();
        DAYS.put(1, new Day01());
        DAYS.put(2, new Day02());
        DAYS.put(3, new Day03());
        DAYS.put(4, new Day04());
        DAYS.put(5, new Day05());
        DAYS.put(6, new Day06());
    }
    
    /**
     * Main entry point
     *
     * @param args Command line arguments
     * @throws IOException If an I/O error occurs
     */
    public static void main(final String[] args) throws IOException {
        final var day = args.length > 0 ? Integer.parseInt(args[0]) : DAYS.size();
        
        System.out.println(MessageFormat.format("AoC 2022 - Day {0}", day));
        
        final var paddedDay = String.format("%02d", day);
        final var path      = Paths.get("resources", "day" + paddedDay + ".txt");
        final var input     = Files.readAllLines(path);
        
        System.out.printf("Part 1: %s%n", DAYS.get(day).part1(input));
        System.out.printf("Part 2: %s%n", DAYS.get(day).part2(input));
    }
}

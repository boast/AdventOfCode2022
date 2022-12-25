package aoc;

import aoc.common.Day;
import aoc.days.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        DAYS.put(7, new Day07());
        DAYS.put(8, new Day08());
        DAYS.put(9, new Day09());
        DAYS.put(10, new Day10());
        DAYS.put(11, new Day11());
        DAYS.put(12, new Day12());
        DAYS.put(13, new Day13());
        DAYS.put(14, new Day14());
        DAYS.put(15, new Day15());
        DAYS.put(16, new Day16());
        DAYS.put(17, new Day17());
        DAYS.put(18, new Day18());
        DAYS.put(19, new Day19());
        DAYS.put(20, new Day20());
        DAYS.put(21, new Day21());
        DAYS.put(22, new Day22());
        DAYS.put(23, new Day23());
        DAYS.put(24, new Day24());
        DAYS.put(25, new Day25());
    }
    
    /**
     * Main entry point
     *
     * @param args Command line arguments
     * @throws IOException If an I/O error occurs
     */
    public static void main(final String[] args) throws IOException {
        final var day = args.length > 0 ? Integer.parseInt(args[0]) : DAYS.size();
        
        System.out.printf("\033[1mAoC 2022 - Day %d\033[0m%n", day);
        
        final var paddedDay = "%02d".formatted(day);
        final var path      = Paths.get("resources", "day%s.txt".formatted(paddedDay));
        final var input     = Files.readAllLines(path);
        
        System.out.printf("Part 1: %s%n", DAYS.get(day).part1(input));
        System.out.printf("Part 2: %s%n", DAYS.get(day).part2(input));
    }
}

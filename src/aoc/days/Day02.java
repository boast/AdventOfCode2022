package aoc.days;

import aoc.common.Day;
import org.jetbrains.annotations.NonNls;

import java.util.List;

/**
 * <b>Day 2: Rock Paper Scissors</b><br>
 * Also straight forward, just two different switch statements. Creating string-constants helps with readability.<br>
 * The duplicated-code detection finds a nasty variable-based single solution for both parts, but lacks readability.
 *
 * @see <a href="https://adventofcode.com/2022/day/2">Day 2: Rock Paper Scissors</a>
 */
@NonNls
public final class Day02 implements Day {
    private static final String ROCK     = "A";
    private static final String PAPER    = "B";
    private static final String SCISSORS = "C";
    
    private static final String PLAYER_X = "X";
    private static final String PLAYER_Y = "Y";
    private static final String PLAYER_Z = "Z";
    
    private static final int WIN  = 6;
    private static final int DRAW = 3;
    private static final int LOSE = 0;
    
    private static final int PLAYED_ROCK     = 1;
    private static final int PLAYED_PAPER    = 2;
    private static final int PLAYED_SCISSORS = 3;
    
    @SuppressWarnings({"NestedConditionalExpression", "LongLine", "DuplicatedCode"})
    private static int getOutcomePart1(final String opponent, final @NonNls String player) {
        return switch (opponent) {
            case ROCK ->
                    player.equals(PLAYER_Y) ? WIN + PLAYED_PAPER : (player.equals(PLAYER_X) ? DRAW + PLAYED_ROCK : LOSE + PLAYED_SCISSORS);
            case PAPER ->
                    player.equals(PLAYER_Z) ? WIN + PLAYED_SCISSORS : (player.equals(PLAYER_Y) ? DRAW + PLAYED_PAPER : LOSE + PLAYED_ROCK);
            case SCISSORS ->
                    player.equals(PLAYER_X) ? WIN + PLAYED_ROCK : (player.equals(PLAYER_Z) ? DRAW + PLAYED_SCISSORS : LOSE + PLAYED_PAPER);
            default -> throw new IllegalStateException("Unexpected value: %s".formatted(opponent));
        };
    }
    
    @SuppressWarnings({"NestedConditionalExpression", "LongLine", "DuplicatedCode"})
    private static int getOutcomePart2(final String opponent, final @NonNls String outcome) {
        return switch (opponent) {
            case ROCK ->
                    outcome.equals(PLAYER_X) ? LOSE + PLAYED_SCISSORS : (outcome.equals(PLAYER_Y) ? DRAW + PLAYED_ROCK : WIN + PLAYED_PAPER);
            case PAPER ->
                    outcome.equals(PLAYER_X) ? LOSE + PLAYED_ROCK : (outcome.equals(PLAYER_Y) ? DRAW + PLAYED_PAPER : WIN + PLAYED_SCISSORS);
            case SCISSORS ->
                    outcome.equals(PLAYER_X) ? LOSE + PLAYED_PAPER : (outcome.equals(PLAYER_Y) ? DRAW + PLAYED_SCISSORS : WIN + PLAYED_ROCK);
            default -> throw new IllegalStateException("Unexpected value: %s".formatted(opponent));
        };
    }
    
    @Override
    public String part1(final List<String> input) {
        return Integer.toString(input.stream()
                                        .map(line -> line.split(" "))
                                        .mapToInt(parts -> getOutcomePart1(parts[0], parts[1]))
                                        .sum());
    }
    
    @Override
    public String part2(final List<String> input) {
        return Integer.toString(input.stream()
                                        .map(line -> line.split(" "))
                                        .mapToInt(parts -> getOutcomePart2(parts[0], parts[1]))
                                        .sum());
    }
}

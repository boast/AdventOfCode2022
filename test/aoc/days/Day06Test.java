package aoc.days;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class Day06Test extends AbstractDayTest {
    @Test
    public void testPart1() {
        final var data = getInput(6);
        final var day  = new Day06();

        assertEquals(day.part1(data), 7, "Part 1");
    }

    @Test
    public void testPart2() {
        final var data = getInput(6);
        final var day  = new Day06();

        assertEquals(day.part2(data), 19, "Part 2");
    }
}

package aoc.days;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class Day19Test extends AbstractDayTest {
    @Test
    public void testPart1() {
        final var data = getInput(19);
        final var day  = new Day19();

        assertEquals(day.part1(data), 33, "Part 1");
    }

    @Test
    public void testPart2() {
        final var data = getInput(19);
        final var day  = new Day19();

        assertEquals(day.part2(data), 56 * 62, "Part 2");
    }
}

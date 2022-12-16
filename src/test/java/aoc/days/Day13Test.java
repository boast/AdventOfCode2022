package aoc.days;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class Day13Test extends AbstractDayTest {
    @Test
    public void testPart1() {
        final var data = getInput(13);
        final var day  = new Day13();

        assertEquals(day.part1(data), 13, "Part 1");
    }

    @Test
    public void testPart2() {
        final var data = getInput(13);
        final var day  = new Day13();

        assertEquals(day.part2(data), 140, "Part 2");
    }
}

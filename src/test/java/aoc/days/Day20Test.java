package aoc.days;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class Day20Test extends AbstractDayTest {
    @Test
    public void testPart1() {
        final var data = getInput(20);
        final var day  = new Day20();

        assertEquals(day.part1(data), 3L, "Part 1");
    }

    @Test
    public void testPart2() {
        final var data = getInput(20);
        final var day  = new Day20();

        assertEquals(day.part2(data), 1623178306L, "Part 2");
    }
}

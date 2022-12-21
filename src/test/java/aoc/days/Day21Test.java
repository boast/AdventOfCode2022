package aoc.days;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class Day21Test extends AbstractDayTest {
    @Test
    public void testPart1() {
        final var data = getInput(21);
        final var day  = new Day21();

        assertEquals(day.part1(data), 152L, "Part 1");
    }

    @Test
    public void testPart2() {
        final var data = getInput(21);
        final var day  = new Day21();

        assertEquals(day.part2(data), 301L, "Part 2");
    }
}

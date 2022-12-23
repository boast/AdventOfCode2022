package aoc.days;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class Day23Test extends AbstractDayTest {
    @Test
    public void testPart1() {
        final var data = getInput(23);
        final var day  = new Day23();

        assertEquals(day.part1(data), 110, "Part 1");
    }

    @Test
    public void testPart2() {
        final var data = getInput(23);
        final var day  = new Day23();

        assertEquals(day.part2(data), 20, "Part 2");
    }
}

package aoc.days;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class Day08Test extends AbstractDayTest {
    @Test
    public void testPart1() {
        final var data = getInput(8);
        final var day  = new Day08();

        assertEquals(day.part1(data), 21, "Part 1");
    }

    @Test
    public void testPart2() {
        final var data = getInput(8);
        final var day  = new Day08();

        assertEquals(day.part2(data), 8, "Part 2");
    }
}

package aoc.days;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class Day12Test extends AbstractDayTest {
    @Test
    public void testPart1() {
        final var data = getInput(12);
        final var day  = new Day12();

        assertEquals(day.part1(data), 31, "Part 1");
    }

    @Test
    public void testPart2() {
        final var data = getInput(12);
        final var day  = new Day12();

        assertEquals(day.part2(data), 29, "Part 2");
    }
}

package aoc.days;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class Day16Test extends AbstractDayTest {
    @Test
    public void testPart1() {
        final var data = getInput(16);
        final var day  = new Day16();

        assertEquals(day.part1(data), 1651, "Part 1");
    }

    @Test
    public void testPart2() {
        final var data = getInput(16);
        final var day  = new Day16();

        assertEquals(day.part2(data), 1707, "Part 2");
    }
}

package aoc.days;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class Day04Test extends AbstractDayTest {
    @Test
    public void testPart1() {
        final var data = getInput(4);
        final var day  = new Day04();

        assertEquals(day.part1(data), 2, "Part 1");
    }

    @Test
    public void testPart2() {
        final var data = getInput(4);
        final var day  = new Day04();

        assertEquals(day.part2(data), 4, "Part 2");
    }
}

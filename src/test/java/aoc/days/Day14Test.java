package aoc.days;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class Day14Test extends AbstractDayTest {
    @Test
    public void testPart1() {
        final var data = getInput(14);
        final var day  = new Day14();

        assertEquals(day.part1(data), 24L, "Part 1");
    }

    @Test
    public void testPart2() {
        final var data = getInput(14);
        final var day  = new Day14();

        assertEquals(day.part2(data), 93L, "Part 2");
    }
}

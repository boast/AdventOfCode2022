package aoc.days;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class Day24Test extends AbstractDayTest {
    @Test
    public void testPart1() {
        final var data = getInput(24);
        final var day  = new Day24();

        assertEquals(day.part1(data), 18, "Part 1");
    }

    @Test
    public void testPart2() {
        final var data = getInput(24);
        final var day  = new Day24();

        assertEquals(day.part2(data), 54, "Part 2");
    }
}

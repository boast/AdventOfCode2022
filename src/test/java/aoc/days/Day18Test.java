package aoc.days;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class Day18Test extends AbstractDayTest {
    @Test
    public void testPart1() {
        final var data = getInput(18);
        final var day  = new Day18();

        assertEquals(day.part1(data), 64L, "Part 1");
    }

    @Test
    public void testPart2() {
        final var data = getInput(18);
        final var day  = new Day18();

        assertEquals(day.part2(data), 58L, "Part 2");
    }
}

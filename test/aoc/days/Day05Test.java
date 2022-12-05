package aoc.days;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class Day05Test extends AbstractDayTest {
    @Test
    public void testPart1() {
        final var data = getInput(5);
        final var day  = new Day05();

        assertEquals(day.part1(data), "CMZ", "Part 1");
    }

    @Test
    public void testPart2() {
        final var data = getInput(5);
        final var day  = new Day05();

        assertEquals(day.part2(data), "MCD", "Part 2");
    }
}

package aoc.days;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class Day09Test extends AbstractDayTest {
    @Test
    public void testPart1() {
        final var data = getInput(9);
        final var day  = new Day09();

        assertEquals(day.part1(data), 88, "Part 1");
    }

    @Test
    public void testPart2() {
        final var data = getInput(9);
        final var day  = new Day09();

        assertEquals(day.part2(data), 36, "Part 2");
    }
}

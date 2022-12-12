package aoc.days;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class Day11Test extends AbstractDayTest {
    @Test
    public void testPart1() {
        final var data = getInput(11);
        final var day  = new Day11();

        assertEquals(day.part1(data), 10605L, "Part 1");
    }

    @Test
    public void testPart2() {
        final var data = getInput(11);
        final var day  = new Day11();

        assertEquals(day.part2(data), 2713310158L, "Part 2");
    }
}

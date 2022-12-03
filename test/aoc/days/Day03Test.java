package aoc.days;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class Day03Test extends AbstractDayTest {
    @Test
    public void testPart1() {
        final var data = getInput(3);
        final var day  = new Day03();

        assertEquals(day.part1(data), 157, "Part 1");
    }

    @Test
    public void testPart2() {
        final var data = getInput(3);
        final var day  = new Day03();

        assertEquals(day.part2(data), 70, "Part 2");
    }
}

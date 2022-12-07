package aoc.days;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class Day07Test extends AbstractDayTest {
    @Test
    public void testPart1() {
        final var data = getInput(7);
        final var day  = new Day07();

        assertEquals(day.part1(data), 95437, "Part 1");
    }

    @Test
    public void testPart2() {
        final var data = getInput(7);
        final var day  = new Day07();

        assertEquals(day.part2(data), 24933642, "Part 2");
    }
}

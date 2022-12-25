package aoc.days;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class Day25Test extends AbstractDayTest {
    @Test
    public void testPart1() {
        final var data = getInput(25);
        final var day  = new Day25();

        assertEquals(day.part1(data), "2=-1=0", "Part 1");
    }

    @Test
    public void testPart2() {
        final var data = getInput(25);
        final var day  = new Day25();

        assertEquals(day.part2(data), "Merry Xmas!", "Part 2");
    }
}

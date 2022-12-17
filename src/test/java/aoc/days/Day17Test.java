package aoc.days;

import org.testng.annotations.Test;

import java.math.BigInteger;

import static org.testng.Assert.*;

public class Day17Test extends AbstractDayTest {
    @Test
    public void testPart1() {
        final var data = getInput(17);
        final var day  = new Day17();

        assertEquals(day.part1(data), new BigInteger("3068"), "Part 1");
    }

    @Test
    public void testPart2() {
        final var data = getInput(17);
        final var day  = new Day17();

        assertEquals(day.part2(data), new BigInteger("1514285714288"), "Part 2");
    }
}

package aoc.days;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class Day15Test extends AbstractDayTest {
    @Test
    public void testPart1() {
        final var data = getInput(15);

        Assert.assertEquals(Day15.part1WithParams(data, 10, 2000), 26L, "Part 1");
    }

    @Test
    public void testPart2() {
        final var data = getInput(15);
        
        assertEquals(Day15.part2WithParams(data, 20), 56000011L, "Part 2");
    }
}

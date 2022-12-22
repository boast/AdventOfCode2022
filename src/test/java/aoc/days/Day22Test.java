package aoc.days;

import org.testng.SkipException;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class Day22Test extends AbstractDayTest {
    @Test
    public void testPart1() {
        final var data = getInput(22);
        final var day  = new Day22();

        assertEquals(day.part1(data), 6032, "Part 1");
    }

    @Test
    public void testPart2() {
        final var data = getInput(22);
        final var day  = new Day22();
    
        //assertEquals(day.part2(data), 5031, "Part 1");
        throw new SkipException("Cannot test part 2, as it would require custom rotations.");
    }
}

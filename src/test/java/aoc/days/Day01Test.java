package aoc.days;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class Day01Test extends AbstractDayTest {
    @Test
    public void testPart1() {
        final var data = getInput(1);
        final var day  = new Day01();
        
        assertEquals(day.part1(data), 24000, "Part 1");
    }
    
    @Test
    public void testPart2() {
        final var data = getInput(1);
        final var day  = new Day01();
        
        assertEquals(day.part2(data), 45000, "Part 2");
    }
}
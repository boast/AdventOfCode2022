package aoc.days;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class Day02Test extends AbstractDayTest {
    @Test
    public void testPart1() {
        final var data = getInput(2);
        final var day  = new Day02();
        
        assertEquals(day.part1(data), 15, "Part 1");
    }
    
    @Test
    public void testPart2() {
        final var data = getInput(2);
        final var day  = new Day02();
        
        assertEquals(day.part2(data), 12, "Part 2");
    }
}
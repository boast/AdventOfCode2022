package aoc.days;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class Day10Test extends AbstractDayTest {
    @Test
    public void testPart1() {
        final var data = getInput(10);
        final var day  = new Day10();

        assertEquals(day.part1(data), 13140, "Part 1");
    }

    @Test
    public void testPart2() {
        final var data = getInput(10);
        final var day  = new Day10();
        
        final var crt = """
                
                ##..##..##..##..##..##..##..##..##..##..
                ###...###...###...###...###...###...###.
                ####....####....####....####....####....
                #####.....#####.....#####.....#####.....
                ######......######......######......####
                #######.......#######.......#######.....
                """;

        assertEquals(day.part2(data), crt, "Part 2");
    }
}

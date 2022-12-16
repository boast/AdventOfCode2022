package aoc.common;

import java.util.List;

/**
 * Interface for a generic day
 */
public interface Day {
    /**
     * Solve part 1
     *
     * @param input Input lines as list (same for both parts)
     * @return Solution
     */
    Object part1(final List<String> input);
    
    /**
     * Solve part 2
     *
     * @param input Input lines as list (same for both parts)
     * @return Solution
     */
    Object part2(final List<String> input);
}

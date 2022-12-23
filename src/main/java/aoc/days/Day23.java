package aoc.days;

import aoc.common.Day;
import aoc.common.Point;
import org.jetbrains.annotations.NonNls;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <b>Day 23: Unstable Diffusion</b><br>
 * Without a cellular automaton puzzle, it's not AoC.<br>
 * Finally, a game-of-life style puzzle (aka cellular automaton). This year we do not have to detect repeating patterns
 * or find blinking endless loops. No, this year the challenge was reading the instructions really carefully.<br>
 * Therefore, part 1 and part 2 are very similar, part 2 just runs longer until we detect no more changes.<br>
 * Part 2 runs in about 30-40 seconds on my machine and is another thing which I suspect other languages just do faster
 * than Java. Also not having 'yield-return' pattern as C# makes you implement a lot of boilerplate code when you want
 * fast exists on your iteration loops.<br>
 * The rest of the challenge was just carefully reading the instructions and implementing the rules, especially:
 * <ul>
 * <li>If an elf has no neighbors, it does nothing this round (but may does something at a later round).</li>
 * <li>Note that it is not forbidden for an elf not to find a move. In that case, it does also nothing.</li>
 * <li>Make sure, you have your N-NE-NW, S-SE-SW, W-NW-SW, E-NE-SE order correct. I used my helper functions so I would
 * not mess up.</li>
 * <li>Check all proposed moves with each other. I used {@link Collections#frequency(Collection, Object)} for this.</li>
 * </ul>
 * But after all, a simple nice puzzle and the end is fast approaching!
 *
 * @see <a href="https://adventofcode.com/2019/day/23">Day 23: Unstable Diffusion</a>
 */
@NonNls
public final class Day23 implements Day {
    @Override
    public Object part1(final List<String> input) {
        final var elves     = parseElves(input);
        final var maxRounds = 10;
        
        for (var round = 0; round < maxRounds; round++) {
            couldDoRound(round, elves);
        }
        
        final var xMin = elves.stream().mapToInt(Point::getX).min().orElseThrow();
        final var xMax = elves.stream().mapToInt(Point::getX).max().orElseThrow();
        final var yMin = elves.stream().mapToInt(Point::getY).min().orElseThrow();
        final var yMax = elves.stream().mapToInt(Point::getY).max().orElseThrow();
        
        var emptyCount = 0;
        
        for (var y = yMin; y <= yMax; y++) {
            for (var x = xMin; x <= xMax; x++) {
                if (!elves.contains(new Point(x, y))) {
                    emptyCount++;
                }
            }
        }
        
        return emptyCount;
    }
    
    @Override
    public Object part2(final List<String> input) {
        final var elves = parseElves(input);
        var       round = 0;
        
        while (couldDoRound(round++, elves)) {
            // loop
        }
        
        return round;
    }
    
    private static boolean couldDoRound(final int round, final Collection<Point> elves) {
        var didChange = false;
        
        final var proposedMoves = new ArrayList<ElfMove>();
        
        final var elvesWithNeighbors = elves.stream().filter(elf -> {
            for (final var neighbor : elf.getAdjacentIterable()) {
                if (elves.contains(neighbor)) {
                    return true;
                }
            }
            return false;
        }).toList();
        
        for (final var elf : elvesWithNeighbors) {
            for (var i = 0; i < directions.length; i++) {
                final var direction = directions[(i + round) % directions.length];
                
                if (!elves.contains(elf.add(direction[0])) &&
                    !elves.contains(elf.add(direction[1])) &&
                    !elves.contains(elf.add(direction[2]))) {
                    proposedMoves.add(new ElfMove(elf, elf.add(direction[0])));
                    break;
                }
            }
        }
        
        final var proposedMovesNextList = proposedMoves.stream().map(ElfMove::next).toList();
        
        for (final var proposedMove : proposedMoves) {
            if (Collections.frequency(proposedMovesNextList, proposedMove.next()) == 1) {
                elves.remove(proposedMove.elf());
                elves.add(proposedMove.next());
                didChange = true;
            }
        }
        
        return didChange;
    }
    
    private static Collection<Point> parseElves(final List<String> input) {
        final var elves = new ArrayList<Point>();
        
        for (var y = 0; y < input.size(); y++) {
            final var line = input.get(y);
            for (var x = 0; x < line.length(); x++) {
                if (line.charAt(x) == '#') {
                    elves.add(new Point(x, y));
                }
            }
        }
        return elves;
    }
    
    private static final Point[][] directions = {
            {Point.ARRAY_UP, Point.ARRAY_UP.add(Point.ARRAY_LEFT), Point.ARRAY_UP.add(Point.ARRAY_RIGHT)},
            {Point.ARRAY_DOWN, Point.ARRAY_DOWN.add(Point.ARRAY_LEFT), Point.ARRAY_DOWN.add(Point.ARRAY_RIGHT)},
            {Point.ARRAY_LEFT, Point.ARRAY_LEFT.add(Point.ARRAY_UP), Point.ARRAY_LEFT.add(Point.ARRAY_DOWN)},
            {Point.ARRAY_RIGHT, Point.ARRAY_RIGHT.add(Point.ARRAY_UP), Point.ARRAY_RIGHT.add(Point.ARRAY_DOWN)},
            };
    
    private record ElfMove(Point elf, Point next) {
    }
}

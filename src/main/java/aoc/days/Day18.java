package aoc.days;

import aoc.common.Day;
import org.jetbrains.annotations.NonNls;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <b>Day 18: Boiling Boulders</b><br>
 * A much simpler puzzle than yesterday's.<br>
 * I first solved part1 a bit different (creating an x, y and z hashmap with each having a list of points of the other
 * two coordinates) and then checked each list if for a given x and its point coordinate, if there exists an x+1 with
 * the same point coordinate. If so, reduce the total surface (total amount of points * 6) by 2. This wouldn't work at
 * all for part 2, as it was an obvious flood-fill algorithm required (I know of no other technique to search for
 * cavities in a 3D grid).<br>
 * So I reworked part 1 to calculate all possible sides (note: you cannot use a set here, as on an convex corner the
 * "side" appear multiple times) and then removed all sides which are cubes to get the total surface are from our
 * droplet.<br>
 * Part 2 then I used (again) DFS / flood-fill to find all droplets which are "outside", bound by the edges of our
 * droplet + 1. Now the total surface area can be calculated by checking again all the possible sides from part 1 and
 * only counting the sides which are part of the "outside" (note we already checked for actual cubes which can never
 * be "outside" and no extra step is required here).<br>
 *
 * @see <a href="https://adventofcode.com/2022/day/18">Day 18: Boiling Boulders</a>
 */
@NonNls
public final class Day18 implements Day {
    @Override
    public Object part1(final List<String> input) {
        final var cubes    = getCubes(input);
        final var allSides = getAllSides(cubes);
        
        return allSides.stream().filter(side -> !cubes.contains(side)).count();
    }
    
    @Override
    public Object part2(final List<String> input) {
        final var cubes    = getCubes(input);
        final var allSides = getAllSides(cubes);
        
        final var outside      = new HashSet<Cube>();
        final var cubeQueue = new ArrayDeque<Cube>();
        
        final var min = cubes.stream()
                             .mapToInt(cube -> Math.min(cube.x(), Math.min(cube.y(), cube.z())))
                             .min()
                             .orElseThrow() - 1;
        final var max = cubes.stream()
                             .mapToInt(cube -> Math.max(cube.x(), Math.max(cube.y(), cube.z())))
                             .max()
                             .orElseThrow() + 1;
        
        cubeQueue.add(new Cube(min, min, min));
        
        while (!cubeQueue.isEmpty()) {
            final var cube = cubeQueue.pop();
            outside.add(cube);
            
            getSides(cube).stream()
                          .filter(side -> !cubes.contains(side) && !outside.contains(side) && isInRange(side, min, max))
                          .forEach(cubeQueue::push);
        }
        
        return allSides.stream().filter(outside::contains).count();
    }
    
    private static boolean isInRange(final Cube cube, final int min, final int max) {
        return cube.x() >= min &&
               cube.x() <= max &&
               cube.y() >= min &&
               cube.y() <= max &&
               cube.z() >= min &&
               cube.z() <= max;
    }
    
    private static List<Cube> getAllSides(final Collection<Cube> cubes) {
        return cubes.stream().flatMap(cube -> getSides(cube).stream()).collect(Collectors.toList());
    }
    
    private static Set<Cube> getCubes(final Collection<String> input) {
        return input.stream().map(s -> {
            final var parts = s.split(",");
            return new Cube(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
        }).collect(Collectors.toSet());
    }
    
    private record Cube(int x, int y, int z) {
    }
    
    private static Set<Cube> getSides(final Cube cube) {
        return Set.of(
                new Cube(cube.x() + 1, cube.y(), cube.z()),
                new Cube(cube.x() - 1, cube.y(), cube.z()),
                new Cube(cube.x(), cube.y() + 1, cube.z()),
                new Cube(cube.x(), cube.y() - 1, cube.z()),
                new Cube(cube.x(), cube.y(), cube.z() + 1),
                new Cube(cube.x(), cube.y(), cube.z() - 1)
        );
    }
}

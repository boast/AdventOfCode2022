package aoc.days;

import aoc.common.Day;
import org.jetbrains.annotations.NonNls;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <b>Day 16:</b><br>
 */
@NonNls
public final class Day16 implements Day {
    private static final Pattern COMMA_AND_SPACE = Pattern.compile(", ");
    
    @Override
    public Object part1(final List<String> input) {
        final var valves  = new HashMap<String, Valve>();
        final var minutes = 30;
        final var start   = "AA";
        
        for (final var line : input) {
            final var parts = line.replace("Valve ", "") //NON-NLS
                                  .replace(" has flow rate=", ";") //NON-NLS
                                  .replace(" tunnels lead to valves ", "") //NON-NLS
                                  .replace(" tunnel leads to valve ", "") //NON-NLS
                                  .split(";");
            
            final var valve     = parts[0];
            final var flowRate  = Integer.parseInt(parts[1]);
            final var neighbors = Arrays.stream(COMMA_AND_SPACE.split(parts[2])).toList();
            
            valves.put(valve, new Valve(flowRate, neighbors));
        }
        
        final var interestingValves = valves.entrySet()
                                            .stream()
                                            .filter(v -> v.getValue().flowRate() > 0)
                                            .map(Map.Entry::getKey)
                                            .collect(Collectors.toList());
        interestingValves.add(start);
        
        final var distanceMap = calculateDistances(valves, interestingValves);
        
        return 0;
    }
    
    @Override
    public Object part2(final List<String> input) {
        return "Not implemented Part 2";
    }
    
    private static HashMap<String, HashMap<String, Integer>> calculateDistances(
            final Map<String, Valve> valves, final Iterable<String> interestingValves
    ) {
        final var distanceMap = new HashMap<String, HashMap<String, Integer>>();
        
        for (final var valve : interestingValves) {
            distanceMap.put(valve, distancesMap(valve, valves));
        }
        
        return distanceMap;
    }
    
    private static HashMap<String, Integer> distancesMap(final String from, final Map<String, Valve> valves) {
        final var distances = new HashMap<String, Integer>();
        final var queue     = new ArrayDeque<String>();
        
        queue.add(from);
        
        while (!queue.isEmpty()) {
            final var current = queue.remove();
            
            for (final var neighbor : valves.get(current).connections()) {
                if (!distances.containsKey(neighbor)) {
                    distances.put(neighbor, distances.getOrDefault(current, 0) + 1);
                    queue.add(neighbor);
                }
            }
        }
        
        return distances;
    }
    
    private record Valve(int flowRate, List<String> connections) {
    }
}

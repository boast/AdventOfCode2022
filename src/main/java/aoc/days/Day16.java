package aoc.days;

import aoc.common.Day;
import org.jetbrains.annotations.NonNls;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <b>Day 16: Proboscidea Volcanium</b><br>
 * Hot dang, what a hard problem - one of the hardest, if not the hardest problems of AoC ever.<br>
 * The problem is not the parsing, not the silly little tunnels vs tunnel singular plural problem, not even the
 * optimization to pre-calculate all the shortest paths between those valves.<br>
 * It's just a vast solution space and w/o resorting to primitive arrays and very optimized hashing algorithms, you wait
 * for your solution a few minutes - or do some bit operations, which probably would work as well.<br>
 * Especially as we need all the solutions for part 2, there is no other way than to actually calculate all the
 * solutions efficiently.<br>
 * However, I am actually quiet happy with part 2, as I was able to optimize the task a lot by first sorting the visited
 * valves (so different ordered solutions get merged) and then by further optimizing the search (break early) the result
 * is found quiet fast, as this is O(n^2) in the worst case.<br>
 * After some time on the subreddit I am even more confident, that this solution is more or less the best you can get
 * with Java.
 *
 * @see <a href="https://adventofcode.com/2022/day/16">Day 16: Proboscidea Volcanium</a>
 */
@NonNls
public final class Day16 implements Day {
    private static final Pattern COMMA_AND_SPACE = Pattern.compile(", ");
    private static final String  START           = "AA";
    
    private static final Map<String, Valve>                valves      = new HashMap<>();
    private static       Map<String, Map<String, Integer>> distanceMap = new HashMap<>();
    
    @Override
    public Object part1(final List<String> input) {
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
                                            .filter(v -> v.getValue().flowRate() > 0 || v.getKey().equals(START))
                                            .map(Map.Entry::getKey)
                                            .collect(Collectors.toList());
        
        distanceMap = calculateDistances(valves, interestingValves);
        
        return solve(30).values().stream().max(Integer::compareTo).orElseThrow();
    }
    
    @Override
    public Object part2(final List<String> input) {
        final var solutions = solve(26);
        var       best      = 0;
        
        final var bestVisited = new HashMap<List<? super String>, Integer>();
        
        // Sort the visited entries so we remove many duplicates
        for (final var solution : solutions.entrySet()) {
            final var visited = solution.getKey().visited().stream().sorted().toList();
            if (bestVisited.getOrDefault(visited, 0) < solution.getValue()) {
                bestVisited.put(visited, solution.getValue());
            }
        }
        
        // Sort by largest values first
        final var bestVisitedList = bestVisited.entrySet()
                                               .stream()
                                               .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                                               .toList();
        
        for (var i = 0; i < bestVisitedList.size() - 1; i++) {
            // We can stop if the two largest possible values are smaller than the current solution
            if (bestVisitedList.get(i).getValue() + bestVisitedList.get(i + 1).getValue() < best) {
                break;
            }
            for (var j = i + 1; j < bestVisitedList.size(); j++) {
                final var set1 = bestVisitedList.get(i);
                final var set2 = bestVisitedList.get(j);
                
                if (set1.getValue() + set2.getValue() <= best) {
                    continue;
                }
                
                if (set1.getKey().stream().anyMatch(set2.getKey()::contains)) {
                    continue;
                }
                
                best = Math.max(best, set1.getValue() + set2.getValue());
            }
        }
        
        return best;
    }
    
    private record SolveStep(String valve, int time, List<? super String> visited, int flow) {
        private SolveStep(final BestStep bestStep, final int flow) {
            this(bestStep.valve(), bestStep.time(), bestStep.visited(), flow);
        }
    }
    
    private record BestStep(String valve, int time, List<? super String> visited) {
        public int hashCode() {
            return 7 * valve().hashCode() + 11 * time() + 13 * visited().hashCode();
        }
    }
    
    private static Map<BestStep, Integer> solve(final int minutes) {
        final var bestStates = new HashMap<BestStep, Integer>();
        final var valveQueue = new ArrayDeque<SolveStep>();
        
        valveQueue.add(new SolveStep(START, minutes, new ArrayList<>(), 0));
        
        while (!valveQueue.isEmpty()) {
            final var currentStep = valveQueue.poll();
            
            if (currentStep.time() >= 1 && !currentStep.visited().contains(currentStep.valve())) {
                if (!currentStep.valve().equals(START)) {
                    currentStep.visited().add(currentStep.valve());
                }
                final var flow       = (currentStep.time() - 1) * valves.get(currentStep.valve()).flowRate();
                final var mayNewBest = new BestStep(currentStep.valve(), currentStep.time() - 1, currentStep.visited());
                
                if (bestStates.getOrDefault(mayNewBest, 0) < flow + currentStep.flow()) {
                    bestStates.put(mayNewBest, flow + currentStep.flow());
                    valveQueue.add(new SolveStep(mayNewBest, flow + currentStep.flow()));
                }
            }
            
            for (final var nextValve : distanceMap.entrySet()) {
                if (nextValve.getKey().equals(currentStep.valve()) ||
                    currentStep.visited().contains(nextValve.getKey())) {
                    continue;
                }
                
                final var distance = distanceMap.get(currentStep.valve()).get(nextValve.getKey());
                
                if (distance < currentStep.time()) {
                    final var mayNextBestStep = new BestStep(nextValve.getKey(),
                                                             currentStep.time() - distance,
                                                             new ArrayList<>(currentStep.visited())
                    );
                    
                    if (mayNextBestStep.time() > 0 &&
                        bestStates.getOrDefault(mayNextBestStep, -1) < currentStep.flow()) {
                        valveQueue.add(new SolveStep(mayNextBestStep, currentStep.flow()));
                    }
                }
            }
        }
        
        return bestStates;
    }
    
    private static Map<String, Map<String, Integer>> calculateDistances(
            final Map<String, Valve> valves, final Iterable<String> interestingValves
    ) {
        final var map = new HashMap<String, Map<String, Integer>>();
        
        for (final var valve : interestingValves) {
            map.put(valve, distancesMap(valve, valves));
        }
        
        return map;
    }
    
    private static Map<String, Integer> distancesMap(final String from, final Map<String, Valve> valves) {
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

package aoc.days;

import aoc.common.Day;
import org.jetbrains.annotations.NonNls;

import java.util.*;

/**
 * <b>Day 19: Not Enough Minerals</b><br>
 * Optimizing a DFS - again!<br>
 * This is really the year of DFS is seems. The problem starts with out with a small state-machine problem to organize
 * the resources and the robots. I quickly defined some helpers and extensively used records to make the code more
 * readable and already provide solid hashable objects to memoize the states.<br>
 * After that it was a long trial and error to find the right optimization strategy, where I could solve part 1 by
 * "flattening" the current state if the resources or the robots were above a required threshold (i.e. we have more ores
 * than we could use up if we would build the most expensive robot, and we have more non-geode robots which we could use
 * up their generated ores each turn).<br>
 * I expected something like the increased iteration time for part 2, and w/o further optimization it would probably be
 * fast enough already by using optimized small datastructures to represent the state and... well... in another language
 * than java.<br>
 * In the subreddit I found the eventual hint (after a lot of trial and error) to stop searching in the current state
 * if the current state could not reach the current best geodes count if (by magic) we would increase the geodes robot
 * count on each turn. This creates a familiar pattern (triangular number sequence: 1, 3, 6, 10, 15, 21, 28... which
 * represents current turn: 1 robot, 1 geode, next turn: 2 robots, 1+2=3 geodes, next turn: 3 robots, 1+2+3=6 geodes,
 * next turn: 4 robots, 1+2+3+4=10 geodes, ...). The formula is simple and can be calculated directly for a given n.<br>
 * The eventual runtime is not bad for java and I leave it at that.
 *
 * @see <a href="https://adventofcode.com/2022/day/19">Day 19: Not Enough Minerals</a>
 */
@NonNls
public final class Day19 implements Day {
    @Override
    public Object part1(final List<String> input) {
        final var blueprints    = parseBlueprints(input);
        var       qualityLevels = 0;
        
        for (final var bp : blueprints) {
            final var bestGeodes = applyBlueprint(bp, 24);
            
            qualityLevels += bestGeodes * bp.id();
        }
        
        return qualityLevels;
    }
    
    @Override
    public Object part2(final List<String> input) {
        final var blueprints = parseBlueprints(input);
        var       product    = 1;
        
        for (final var bp : blueprints.stream().takeWhile(bp -> bp.id() <= 3).toList()) {
            final var bestGeodes = applyBlueprint(bp, 32);
            
            product *= bestGeodes;
        }
        
        return product;
    }
    
    private static int applyBlueprint(final Blueprint bp, final int minutes) {
        final var stateQueue = new ArrayDeque<>(List.of(new State(minutes)));
        final var stateSeen  = new HashSet<State>();
        
        var bestGeodes = 0;
        
        while (!stateQueue.isEmpty()) {
            final var state = stateQueue.pop();
            
            bestGeodes = Math.max(bestGeodes, state.resources().geode());
            
            final var maxPossibleGeodes = state.resources().geode() +
                                          state.robots().geode() * state.minute() +
                                          triangular(state.minute());
            
            if (state.minute() == 0 || maxPossibleGeodes <= bestGeodes) {
                continue;
            }
            
            // Flatten (throw away) unneeded resources and robots (we never need more robots for a given resource
            // than the most expensive robot costs of that resource).
            
            final var oreRobots  = state.robots().ore() >= bp.maxOreReq() ? bp.maxOreReq() : state.robots().ore();
            final var clayRobots = state.robots().clay() >= bp.maxClayReq() ? bp.maxClayReq() : state.robots().clay();
            final var obsidianRobots = state.robots().obsidian() >=
                                       bp.maxObsidianReq() ? bp.maxObsidianReq() : state.robots().obsidian();
            
            // The maximum resource required is the maximum cost of any robot of this resource multiplied by the
            // time left, subtracted by the amount of that resource we will generate in the future.
            
            final var maxOreReq      = state.minute() * bp.maxOreReq() - oreRobots * (state.minute() - 1);
            final var maxClayReq     = state.minute() * bp.maxClayReq() - clayRobots * (state.minute() - 1);
            final var maxObsidianReq = state.minute() * bp.maxObsidianReq() - obsidianRobots * (state.minute() - 1);
            
            final var flattenState = new State(state.minute(),
                                               new Resources(state.resources().ore() >=
                                                             maxOreReq ? maxOreReq : state.resources().ore(),
                                                             state.resources().clay() >=
                                                             maxClayReq ? maxClayReq : state.resources().clay(),
                                                             state.resources().obsidian() >=
                                                             maxObsidianReq ? maxObsidianReq : state.resources()
                                                                                                    .obsidian(),
                                                             state.resources().geode()
                                               ),
                                               new Robots(oreRobots, clayRobots, obsidianRobots, state.robots().geode())
            );
            
            if (stateSeen.contains(flattenState)) {
                continue;
            }
            stateSeen.add(flattenState);
            
            final var nextState = flattenState.getNext();
            
            stateQueue.push(nextState);
            
            if (flattenState.resources().ore() >= bp.oreRobotOreCost()) {
                stateQueue.push(nextState.buyOreRobot(bp));
            }
            if (flattenState.resources().ore() >= bp.clayRobotOreCost()) {
                stateQueue.push(nextState.buyClayRobot(bp));
            }
            if (flattenState.resources().ore() >= bp.obsidianRobotOreCost() &&
                flattenState.resources().clay() >= bp.obsidianRobotClayCost()) {
                stateQueue.push(nextState.buyObsidianRobot(bp));
            }
            if (flattenState.resources().ore() >= bp.geodeRobotOreCost() &&
                flattenState.resources().obsidian() >= bp.geodeRobotObsidianCost()) {
                stateQueue.push(nextState.buyGeodeRobot(bp));
            }
        }
        
        return bestGeodes;
    }
    
    private static int triangular(final int n) {
        return n * (n - 1) / 2;
    }
    
    private static List<Blueprint> parseBlueprints(final Collection<String> input) {
        return input.stream().map(line -> {
            //noinspection HardCodedStringLiteral
            final var parts = line.replace("Blueprint ", "")
                                  .replace(": Each ore robot costs ", " ")
                                  .replace(" ore. Each clay robot costs ", " ")
                                  .replace(" ore. Each obsidian robot costs ", " ")
                                  .replace(" clay. Each geode robot costs ", " ")
                                  .replace(" ore and ", " ")
                                  .replace(" obsidian.", "")
                                  .split(" ");
            
            return new Blueprint(Integer.parseInt(parts[0]),
                                 Integer.parseInt(parts[1]),
                                 Integer.parseInt(parts[2]),
                                 Integer.parseInt(parts[3]),
                                 Integer.parseInt(parts[4]),
                                 Integer.parseInt(parts[5]),
                                 Integer.parseInt(parts[6])
            );
        }).toList();
    }
    
    private record Blueprint(
            int id,
            int oreRobotOreCost,
            int clayRobotOreCost,
            int obsidianRobotOreCost,
            int obsidianRobotClayCost,
            int geodeRobotOreCost,
            int geodeRobotObsidianCost
    ) {
        int maxOreReq() {
            return Math.max(oreRobotOreCost,
                            Math.max(clayRobotOreCost, Math.max(obsidianRobotOreCost, geodeRobotOreCost))
            );
        }
        
        int maxClayReq() {
            return obsidianRobotClayCost;
        }
        
        int maxObsidianReq() {
            return geodeRobotObsidianCost;
        }
    }
    
    private record Resources(int ore, int clay, int obsidian, int geode) {
        private Resources() {
            this(0, 0, 0, 0);
        }
    }
    
    private record Robots(int ore, int clay, int obsidian, int geode) {
        private Robots() {
            this(1, 0, 0, 0);
        }
        
        Robots addOreRobot() {
            return new Robots(ore + 1, clay, obsidian, geode);
        }
        
        Robots addClayRobot() {
            return new Robots(ore, clay + 1, obsidian, geode);
        }
        
        Robots addObsidianRobot() {
            return new Robots(ore, clay, obsidian + 1, geode);
        }
        
        Robots addGeodeRobot() {
            return new Robots(ore, clay, obsidian, geode + 1);
        }
    }
    
    private record State(int minute, Resources resources, Robots robots) {
        private State(final int minutes) {
            this(minutes, new Resources(), new Robots());
        }
        
        State getNext() {
            return new State(minute - 1, new Resources(resources.ore() + robots.ore(),
                                                       resources.clay() + robots.clay(),
                                                       resources.obsidian() + robots.obsidian(),
                                                       resources.geode() + robots.geode()
            ), robots);
        }
        
        State buyOreRobot(final Blueprint blueprint) {
            return new State(minute, new Resources(resources.ore() - blueprint.oreRobotOreCost(),
                                                   resources.clay(),
                                                   resources.obsidian(),
                                                   resources.geode()
            ), robots.addOreRobot());
        }
        
        State buyClayRobot(final Blueprint blueprint) {
            return new State(minute, new Resources(resources.ore() - blueprint.clayRobotOreCost(),
                                                   resources.clay(),
                                                   resources.obsidian(),
                                                   resources.geode()
            ), robots.addClayRobot());
        }
        
        State buyObsidianRobot(final Blueprint blueprint) {
            return new State(minute, new Resources(resources.ore() - blueprint.obsidianRobotOreCost(),
                                                   resources.clay() - blueprint.obsidianRobotClayCost(),
                                                   resources.obsidian(),
                                                   resources.geode()
            ), robots.addObsidianRobot());
        }
        
        State buyGeodeRobot(final Blueprint blueprint) {
            return new State(minute, new Resources(resources.ore() - blueprint.geodeRobotOreCost(),
                                                   resources.clay(),
                                                   resources.obsidian() - blueprint.geodeRobotObsidianCost(),
                                                   resources.geode()
            ), robots.addGeodeRobot());
        }
    }
}

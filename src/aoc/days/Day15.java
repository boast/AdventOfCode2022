package aoc.days;

import aoc.common.Day;
import aoc.common.Point;
import org.jetbrains.annotations.NonNls;

import java.io.IOException;
import java.sql.Array;
import java.util.*;

/**
 * <b>Day 15: Beacon Exclusion Zone</b><br>
 * Oh boy, this was hard.<br>
 * The parsing part was rather easy, you get a sensor and a beacon. Done.<br>
 * Now even on part 1 you start to realize, that the combination of sensor and beacon location creates massive "fields"
 * of ranges which cannot be done "naively" by simply mapping out each point.<br>
 * As an added hassle, the input and range of the testdata and the actual data differ vastly and the testdata also
 * included some nasty negative values which require an offset solution. This was in the end quiet handy, as the actual
 * data required also an offset so the ranges would not be negative.<br>
 * To reduce the problem size in part 1, I only simulated all points which are in the requested Y-row. This reduces the
 * problem size from roughly 10 million by 10 million array to a one dimensional (still 10 million elements) array.<br>
 * The part 2 was even trickier, but after some time and even printing out the given example data I realized, that those
 * sensor to beacon ranges can be represented with actual ranges! I still had to simulate one full axis, but due to the
 * limits given by the problem (4_000_000), this was again now 4 million rows which each 8 to 12 intervals.<br>
 * Note that the interval calculation involves the realization that when using the manhattan distance, that the edges of
 * a given sensor range in y coordinates plus the current x coordinate must be equal to the manhattan distance. So by
 * just moving the x coordinate you know the y coordinate by the delta to the manhattan distance.<br>
 * When you have all the ranges, all you need to do is a partial interval merge algorithm, where you first sort all the
 * ranges by their start value and then check if the next interval has the start of its value in the range of the
 * current interval, extend by the larger end value of both and repeat. Once there is a gap you have found the the
 * actual solution and can stop.<br>
 *
 * @see <a href="https://adventofcode.com/2022/day/15">Day 15: Beacon Exclusion Zone</a>
 */
@NonNls
public final class Day15 implements Day {
    @Override
    public Object part1(final List<String> input) {
        return part1WithParams(input, 2_000_000, 10_000_000);
    }
    
    static long part1WithParams(final Iterable<String> input, final int yRow, final int xMax) {
        final var sensorToBeaconMap = getSensorToBeaconMap(input, 1_000);
        final var map               = new Field[xMax];
        
        for (final var sensorBeaconEntry : sensorToBeaconMap.entrySet()) {
            final var sensor = sensorBeaconEntry.getKey();
            final var beacon = sensorBeaconEntry.getValue();
            
            if (sensor.getY() == yRow) {
                map[sensor.getX()] = Field.SENSOR;
            }
            if (beacon.getY() == yRow) {
                map[beacon.getX()] = Field.BEACON;
            }
            
            final var distance = sensor.manhattanDistance(beacon);
            
            for (int x = sensor.getX() - distance; x <= sensor.getX() + distance; x++) {
                final var distanceDelta = distance - Math.abs(x - sensor.getX());
                
                if (yRow >= sensor.getY() - distanceDelta && yRow <= sensor.getY() + distanceDelta) {
                    if (map[x] == null) {
                        map[x] = Field.EMPTY;
                    }
                }
            }
        }
        
        return Arrays.stream(map).filter(field -> field == Field.EMPTY || field == Field.SENSOR).count();
    }
    
    @Override
    public Object part2(final List<String> input) {
        return part2WithParams(input, 4_000_000);
    }
    
    static long part2WithParams(final Iterable<String> input, final int max) {
        final var                sensorToBeaconMap = getSensorToBeaconMap(input, 0);
        final ArrayList<Range>[] map               = new ArrayList[max + 1];
        
        for (int i = 0; i < max + 1; i++) {
            map[i] = new ArrayList<>(12); // Found by debugging to prevent resizing
        }
        
        for (final var sensorBeaconEntry : sensorToBeaconMap.entrySet()) {
            final var sensor   = sensorBeaconEntry.getKey();
            final var beacon   = sensorBeaconEntry.getValue();
            final var distance = sensor.manhattanDistance(beacon);
            
            for (int x = Math.max(0, sensor.getX() - distance); x <= Math.min(sensor.getX() + distance, max); x++) {
                final var distanceDelta = distance - Math.abs(x - sensor.getX());
                final var range = new Range(sensor.getY() - distanceDelta, sensor.getY() + distanceDelta);
    
                map[x].add(range);
            }
        }
        
        var x = 0L;
        
        for(final var ranges : map) {
            ranges.sort(Comparator.comparingInt(Range::getStart));
            final var current = ranges.get(0);
            
            for (int i = 1; i < ranges.size(); i++) {
                final var next = ranges.get(i);
                
                if (next.getStart() <= current.getEnd() + 1) {
                    current.setEnd(Math.max(current.getEnd(), next.getEnd()));
                } else {
                    return x * 4000000 + (current.getEnd() + 1);
                }
            }
            x++;
        }
        
        return -1;
    }
    
    private static Map<Point, Point> getSensorToBeaconMap(final Iterable<String> input, final int xOffset) {
        final var sensorToBeaconMap = new HashMap<Point, Point>();
        for (final var line : input) {
            final var parts = line.replace("Sensor at", "") //NON-NLS
                                  .replace("closest beacon is at", "") //NON-NLS
                                  .split(":");
            final var sensorParts = parts[0].split(",");
            final var sensor = new Point(
                    Integer.parseInt(sensorParts[0].replace("x=", "").trim()) + xOffset, //NON-NLS
                    Integer.parseInt(sensorParts[1].replace("y=", "").trim()) //NON-NLS
            );
            
            final var beaconParts = parts[1].split(",");
            final var beacon = new Point(
                    Integer.parseInt(beaconParts[0].replace("x=", "").trim()) + xOffset, //NON-NLS
                    Integer.parseInt(beaconParts[1].replace("y=", "").trim()) //NON-NLS
            );
            sensorToBeaconMap.put(sensor, beacon);
        }
        
        return sensorToBeaconMap;
    }
    
    private static final class Range {
        private int start;
        private int end;
    
        Range(final int start, final int end) {
            this.start = start;
            this.end = end;
        }
    
        int getStart() {
            return start;
        }
        
        void setStart(final int start) {
            this.start = start;
        }
        
        int getEnd() {
            return end;
        }
        
        void setEnd(final int end) {
            this.end = end;
        }
    }
    
    private enum Field {
        UNKNOWN, BEACON, SENSOR, EMPTY
    }
}

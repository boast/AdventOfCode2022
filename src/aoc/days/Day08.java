package aoc.days;

import aoc.common.Day;
import org.jetbrains.annotations.NonNls;

import java.util.Arrays;
import java.util.List;

/**
 * <b>Day 08: Treetop Tree House</b><br>
 * Oh dear, the first of many 2D problems. As everything is still row and column based (no neighbours), I opted for a
 * simple 2D array (rather than working with a Point structure).<br>
 * The parsing is straight forward, with a small char to int conversion trick (subtracting '0' from the char).<br>
 * The loops are a bit ugly but I think cannot be simplified more (or reduce the duplicated code). GitHub Co-Pilot
 * helped here with this repetitive task and detected the flow early on.<br>
 * In part 1 we have to loop from all outside directions (top, bottom, left, right) and note, that we never can break
 * the loop early, as there could always be a higher tree further down the line.<br>
 * In part 2 we do something similar but from the inside out, seen from the single trees perspective. Note that here we
 * can break early, as we only need to find the first tree that is higher or equal than the current one.<br>
 * For legibility I removed a small optimization which breaks the scoring algorithm on the first 0, but this can only
 * happen at the edges and the runtime barely changes (inside you see at least one tree in any direction, even if the
 * tree in front of you is large...). Also to finalize the generated maps in both parts, we use the nice flatMapToInt
 * stream function to reduce the 2D array to a single line of integers.
 */
@NonNls
public final class Day08 implements Day {
    @Override
    public Object part1(final List<String> input) {
        final var treeMap        = buildTreeMap(input);
        final var xMax           = treeMap.length;
        final var yMax           = treeMap[0].length;
        final var visibleTreeMap = new int[xMax][yMax];
        
        // X asc and desc
        //noinspection DuplicatedCode
        for (var y = 0; y < yMax; y++) {
            var currentMax = -1;
            for (var x = 0; x < xMax; x++) {
                if (treeMap[x][y] > currentMax) {
                    visibleTreeMap[x][y] = 1;
                    currentMax = treeMap[x][y];
                }
            }
            currentMax = -1;
            for (var x = xMax - 1; x >= 0; x--) {
                if (treeMap[x][y] > currentMax) {
                    visibleTreeMap[x][y] = 1;
                    currentMax = treeMap[x][y];
                }
            }
        }
        
        // Y asc and desc
        //noinspection DuplicatedCode
        for (var x = 0; x < xMax; x++) {
            var currentMax = -1;
            for (var y = 0; y < yMax; y++) {
                if (treeMap[x][y] > currentMax) {
                    visibleTreeMap[x][y] = 1;
                    currentMax = treeMap[x][y];
                }
            }
            currentMax = -1;
            for (var y = yMax - 1; y >= 0; y--) {
                if (treeMap[x][y] > currentMax) {
                    visibleTreeMap[x][y] = 1;
                    currentMax = treeMap[x][y];
                }
            }
        }
        
        return Arrays.stream(visibleTreeMap).flatMapToInt(Arrays::stream).sum();
    }
    
    @Override
    public Object part2(final List<String> input) {
        final var treeMap  = buildTreeMap(input);
        final var xMax     = treeMap.length;
        final var yMax     = treeMap[0].length;
        final var scoreMap = new int[xMax][yMax];
        
        for (var y = 0; y < yMax; y++) {
            for (var x = 0; x < xMax; x++) {
                scoreMap[x][y] = getScore(x, y, treeMap);
            }
        }
        
        return Arrays.stream(scoreMap).flatMapToInt(Arrays::stream).max().orElse(-1);
    }
    
    private static int getScore(final int x, final int y, final int[][] treeMap) {
        final var xMax = treeMap.length;
        final var yMax = treeMap[0].length;
        
        //noinspection DuplicatedCode
        var xAsc = 0;
        for (var iX = x + 1; iX < xMax; iX++) {
            xAsc++;
            if (treeMap[iX][y] >= treeMap[x][y]) {
                break;
            }
        }
        
        var xDesc = 0;
        for (var iX = x - 1; iX >= 0; iX--) {
            xDesc++;
            if (treeMap[iX][y] >= treeMap[x][y]) {
                break;
            }
        }
        
        //noinspection DuplicatedCode
        var yAsc = 0;
        for (var iY = y + 1; iY < yMax; iY++) {
            yAsc++;
            if (treeMap[x][iY] >= treeMap[x][y]) {
                break;
            }
        }
        
        var yDesc = 0;
        for (var iY = y - 1; iY >= 0; iY--) {
            yDesc++;
            if (treeMap[x][iY] >= treeMap[x][y]) {
                break;
            }
        }
        
        return xAsc * xDesc * yAsc * yDesc;
    }
    
    private static int[][] buildTreeMap(final List<String> input) {
        final var xMax    = input.get(0).length();
        final var yMax    = input.size();
        final var treeMap = new int[xMax][yMax];
        
        for (var y = 0; y < yMax; y++) {
            final var line = input.get(y);
            for (var x = 0; x < xMax; x++) {
                treeMap[x][y] = line.charAt(x) - '0';
            }
        }
        
        return treeMap;
    }
}

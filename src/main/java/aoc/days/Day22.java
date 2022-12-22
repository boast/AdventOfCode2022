package aoc.days;

import aoc.common.Day;
import aoc.common.Point;
import org.jetbrains.annotations.NonNls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;


/**
 * <b>Day 22: Monkey Map</b><br>
 * Have scissors and a piece of paper ready...<br>
 * Part 1 was pretty straight forward, just parse the input and I had the - eventual great - idea to wrap the map with a
 * border of {@link Tile#EMPTY} tiles. This made the bounds checks a lot easier and you would find logical errors very
 * fast, as only then your data arrays would go out of bounds. When you had to wrap in part 1, just iterate from the
 * other end of the board until you hit the first non-{@link Tile#EMPTY} tile. Which could be a {@link Tile#WALL} Tile,
 * so we have to do look ahead.<br>
 * Part 2 was a bloody mess and I eventually decided to skip the test case all together, as I understood the problem but
 * the test data would have a cube folded differently than the one of the input data, which would result in different
 * rotation rules. With the help of good old pen, paper and scissors I folded myself a cube according to the input data
 * and found the rotation rules that way. Note that all rotations are reversible, so I could easily check this in code.
 * Yes, I know, this is not nice Java code with a lot of duplication, but this day was a mess and I really wanted to get
 * it over with. It is probably possible to generalize the rotation rules depending on the input, as the rotation rules
 * repeat itself (all are some sort of matrix transpositions). But this is left as an exercise to the reader 😉.<br>
 * Oh, and `assert` is your friend if you make assumptions about the state of your code or input etc, use it!
 *
 * @see <a href="https://adventofcode.com/2020/day/22">Day 22: Monkey Map</a>
 */
@NonNls
public final class Day22 implements Day {
    
    private static final Pattern INSTRUCTIONS = Pattern.compile("(?<steps>\\d+)(?<turn>[L|R]?)");
    
    @Override
    public Object part1(final List<String> input) {
        // We create a board which is surrounded by a border of EMPTY tiles.
        final var xMax  = input.get(10).length() + 2; // By inspection this works for test and actual input.
        final var yMax  = input.size(); // We have 1 empty line and 1 instruction line, but add one at start & end each
        final var board = new Tile[yMax][xMax];
        
        fillBoard(board, input, yMax);
        final var instructions = getInstructions(input, yMax);
        
        var position  = new Point(input.get(0).indexOf('.') + 1, 1);
        var direction = Point.ARRAY_RIGHT;
        
        for (final var instruction : instructions) {
            for (int i = 0; i < instruction.steps(); i++) {
                var next = board[position.getY() + direction.getY()][position.getX() + direction.getX()];
                var wrap = new Point();
                
                // We have to wrap
                if (next == Tile.EMPTY) {
                    if (direction.equals(Point.ARRAY_RIGHT)) {
                        var x = 0;
                        while (board[position.getY()][x] == Tile.EMPTY) {
                            x++;
                        }
                        wrap = new Point(x, position.getY());
                    } else if (direction.equals(Point.ARRAY_LEFT)) {
                        var x = xMax - 1;
                        while (board[position.getY()][x] == Tile.EMPTY) {
                            x--;
                        }
                        wrap = new Point(x, position.getY());
                    } else if (direction.equals(Point.ARRAY_DOWN)) {
                        var y = 0;
                        while (board[y][position.getX()] == Tile.EMPTY) {
                            y++;
                        }
                        wrap = new Point(position.getX(), y);
                    } else if (direction.equals(Point.ARRAY_UP)) {
                        var y = yMax - 1;
                        while (board[y][position.getX()] == Tile.EMPTY) {
                            y--;
                        }
                        wrap = new Point(position.getX(), y);
                    }
                    next = board[wrap.getY()][wrap.getX()];
                }
                
                if (next == Tile.WALL) {
                    break;
                }
                
                assert next == Tile.OPEN;
                
                if (wrap.equals(Point.ORIGIN)) {
                    position = position.add(direction);
                } else {
                    position = wrap;
                }
            }
            
            direction = turn(direction, instruction.turn());
        }
        
        return getPassword(position, direction);
    }
    
    @Override
    public Object part2(final List<String> input) {
        // We create a board which is surrounded by a border of EMPTY tiles.
        final var xMax  = input.get(10).length() + 2; // By inspection this works for test and actual input.
        final var yMax  = input.size(); // We have 1 empty line and 1 instruction line, but add one at start & end each
        final var board = new Tile[yMax][xMax];
        
        fillBoard(board, input, yMax);
        final var instructions = getInstructions(input, yMax);
        
        var position  = new Point(input.get(0).indexOf('.') + 1, 1);
        var direction = Point.ARRAY_RIGHT;
        
        for (final var instruction : instructions) {
            for (int i = 0; i < instruction.steps(); i++) {
                var next = board[position.getY() + direction.getY()][position.getX() + direction.getX()];
                var wrap = new Orientation(new Point(), direction);
                
                // We have to wrap
                if (next == Tile.EMPTY) {
                    wrap = getWrap(new Orientation(position, direction));
                    next = board[wrap.position().getY()][wrap.position().getX()];
                }
                
                if (next == Tile.WALL) {
                    break;
                }
                
                assert next == Tile.OPEN;
                
                if (wrap.position().equals(Point.ORIGIN)) {
                    position = position.add(direction);
                } else {
                    position = wrap.position();
                    direction = wrap.direction();
                }
            }
            
            direction = turn(direction, instruction.turn());
        }
        
        return getPassword(position, direction);
    }
    
    private static Orientation getWrap(final Orientation orientation) {
        // My input cube looks like this (each face has length 50)
        //  12
        //  3
        // 45
        // 6
        // You can go right from 2, 3, 5, 6
        // You can go left from 1, 3, 4, 6
        // You can go up from 4, 1, 2
        // You can go down from 6, 5, 2
        // Yes, I built that cube with paper.
        final var x         = orientation.position().getX();
        final var y         = orientation.position().getY();
        final var direction = orientation.direction();
        
        if (direction == Point.ARRAY_RIGHT) {
            if (x == 150) {
                // 2 to 5 and rotate 180°
                return new Orientation(new Point(100, 151 - y), turn(direction, Turn.FULL));
            }
            if (x == 100) {
                if (y >= 51 && y <= 100) {
                    // 3 to 2 and rotate 90° left
                    return new Orientation(new Point(100 + (y - 50), 50), turn(direction, Turn.LEFT));
                }
                if (y >= 101 && y <= 150) {
                    // 5 to 2 and rotate 180°
                    return new Orientation(new Point(150, 51 - (y - 100)), turn(direction, Turn.FULL));
                }
            }
            if (x == 50) {
                // 6 to 5 and rotate 90° left
                return new Orientation(new Point(50 + (y - 150), 150), turn(direction, Turn.LEFT));
            }
        }
        if (direction == Point.ARRAY_LEFT) {
            if (x == 51) {
                if (y >= 1 && y <= 50) {
                    // 1 to 4 and rotate 180°
                    return new Orientation(new Point(1, 151 - y), turn(direction, Turn.FULL));
                }
                if (y >= 51 && y <= 100) {
                    // 3 to 4 and rotate 90° left
                    return new Orientation(new Point(y - 50, 101), turn(direction, Turn.LEFT));
                }
            }
            if (x == 1) {
                if (y >= 101 && y <= 150) {
                    // 4 to 1 and rotate 180°
                    return new Orientation(new Point(51, 1 + (150 - y)), turn(direction, Turn.FULL));
                }
                if (y >= 151 && y <= 200) {
                    // 6 to 1 and rotate 90° left
                    return new Orientation(new Point(50 + (y - 150), 1), turn(direction, Turn.LEFT));
                }
            }
        }
        if (direction == Point.ARRAY_DOWN) {
            if (y == 50) {
                // 2 to 3 and rotate 90° right
                return new Orientation(new Point(100, x - 50), turn(direction, Turn.RIGHT));
            }
            if (y == 150) {
                // 5 to 6 and rotate 90° right
                return new Orientation(new Point(50, x + 100), turn(direction, Turn.RIGHT));
            }
            if (y == 200) {
                // 6 to 2 and no rotation
                return new Orientation(new Point(x + 100, 1), turn(direction, Turn.NONE));
            }
        }
        if (direction == Point.ARRAY_UP) {
            if (y == 1) {
                if (x >= 51 && x <= 100) {
                    // 1 to 6 and rotate 90° right
                    return new Orientation(new Point(1, x + 100), turn(direction, Turn.RIGHT));
                }
                if (x >= 101 && x <= 150) {
                    // 2 to 6 and no rotation
                    return new Orientation(new Point(x - 100, 200), turn(direction, Turn.NONE));
                }
            }
            if (y == 101) {
                // 4 to 3 and rotate 90° right
                return new Orientation(new Point(51, x + 50), turn(direction, Turn.RIGHT));
            }
        }
        
        throw new IllegalArgumentException("Unknown wrap: %s".formatted(orientation));
    }
    
    private record Orientation(Point position, Point direction) {
    }
    
    private static List<Instruction> getInstructions(final List<String> input, final int yMax) {
        final var matcher      = INSTRUCTIONS.matcher(input.get(yMax - 1));
        final var instructions = new ArrayList<Instruction>();
        
        while (matcher.find()) {
            instructions.add(new Instruction(
                    Integer.parseInt(matcher.group("steps")),
                    Turn.fromChar(matcher.group("turn").isEmpty() ? 'N' : matcher.group("turn").charAt(0))
            ));
        }
        
        return instructions;
    }
    
    private static void fillBoard(final Tile[][] board, final List<String> input, final int yMax) {
        Arrays.fill(board[0], Tile.EMPTY);
        for (int y = 1; y < yMax - 1; y++) {
            Arrays.fill(board[y], Tile.EMPTY);
            
            for (int x = 0; x < input.get(y - 1).length(); x++) {
                board[y][x + 1] = Tile.fromChar(input.get(y - 1).charAt(x));
            }
        }
        Arrays.fill(board[yMax - 1], Tile.EMPTY);
    }
    
    private static int getPassword(final Point position, final Point direction) {
        int facing = 0; // Point.ARRAY_RIGHT
        
        if (direction.equals(Point.ARRAY_DOWN)) {
            facing = 1;
        } else if (direction.equals(Point.ARRAY_LEFT)) {
            facing = 2;
        } else if (direction.equals(Point.ARRAY_UP)) {
            facing = 3;
        }
        
        return 1000 * position.getY() + 4 * position.getX() + facing;
    }
    
    private record Instruction(int steps, Turn turn) {
    }
    
    private static Point turn(final Point direction, final Turn turn) {
        if (turn == Turn.NONE) {
            return direction;
        }
        if (direction.equals(Point.ARRAY_UP)) {
            if (turn == Turn.FULL) {
                return Point.ARRAY_DOWN;
            }
            return turn == Turn.LEFT ? Point.ARRAY_LEFT : Point.ARRAY_RIGHT;
        }
        if (direction.equals(Point.ARRAY_DOWN)) {
            if (turn == Turn.FULL) {
                return Point.ARRAY_UP;
            }
            return turn == Turn.LEFT ? Point.ARRAY_RIGHT : Point.ARRAY_LEFT;
        }
        if (direction.equals(Point.ARRAY_LEFT)) {
            if (turn == Turn.FULL) {
                return Point.ARRAY_RIGHT;
            }
            return turn == Turn.LEFT ? Point.ARRAY_DOWN : Point.ARRAY_UP;
        }
        if (direction.equals(Point.ARRAY_RIGHT)) {
            if (turn == Turn.FULL) {
                return Point.LEFT;
            }
            return turn == Turn.LEFT ? Point.ARRAY_UP : Point.ARRAY_DOWN;
        }
        throw new IllegalArgumentException("Unknown direction: %s".formatted(direction));
    }
    
    private enum Turn {
        LEFT, RIGHT, FULL, NONE;
        
        static Turn fromChar(final char charAt) {
            return switch (charAt) {
                case 'L' -> LEFT;
                case 'R' -> RIGHT;
                case 'F' -> FULL;
                case 'N' -> NONE;
                default -> throw new IllegalArgumentException("Unknown turn: %s".formatted(charAt));
            };
        }
    }
    
    private enum Tile {
        EMPTY, OPEN, WALL;
        
        @Override
        public String toString() {
            return switch (this) {
                case EMPTY -> " ";
                case OPEN -> ".";
                case WALL -> "#";
            };
        }
        
        static Tile fromChar(final char charAt) {
            return switch (charAt) {
                case '.' -> OPEN;
                case '#' -> WALL;
                default -> EMPTY;
            };
        }
    }
}

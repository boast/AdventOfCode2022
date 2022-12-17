$dayInt = $args[0]
$day = $dayInt.ToString().PadLeft(2, "0")

New-Item -Path "resources/day${day}.txt"
New-Item -Path "resources/day${day}.test.txt"

New-Item -Path "src/main/java/aoc/days/Day${day}.java"
$classContent = @"
package aoc.days;

import aoc.common.Day;
import org.jetbrains.annotations.NonNls;

import java.util.List;

/**
 * <b>Day ${day}:</b><br>
 */
 @NonNls
public final class Day${day} implements Day {
    @Override
    public Object part1(final List<String> input) {
        return "Not implemented Part 1";
    }

    @Override
    public Object part2(final List<String> input) {
        return "Not implemented Part 2";
    }
}
"@
Add-Content -Path "src/main/java/aoc/days/Day${day}.java" -Value $classContent

New-Item -Path "src/test/java/aoc/days/Day${day}Test.java"
$testClassContent = @"
package aoc.days;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class Day${day}Test extends AbstractDayTest {
    @Test
    public void testPart1() {
        final var data = getInput(${dayInt});
        final var day  = new Day${day}();

        assertEquals(day.part1(data), "Not implemented Part 1", "Part 1");
    }

    @Test
    public void testPart2() {
        final var data = getInput(${dayInt});
        final var day  = new Day${day}();

        assertEquals(day.part2(data), "Not implemented Part 2", "Part 2");
    }
}
"@
Add-Content -Path "src/test/java/aoc/days/Day${day}Test.java" -Value $testClassContent

$testngContent = @"
    <test name="aoc.test.Day${day}Test">
        <classes>
            <class name="aoc.days.Day${day}Test"/>
        </classes>
    </test>
</suite>
"@

((Get-Content -Path "src/test/java/testng.xml" -Raw) -replace "</suite>", $testngContent).Trim() `
    | Set-Content -Path "src/test/java//testng.xml"

((Get-Content -Path "src/main/java/aoc/App.java" -Raw) `
    -replace "\(\)\)\;\r\n    }", "());`r`n        DAYS.put(${dayInt}, new Day${day}());`r`n    }").Trim() `
    | Set-Content -Path "src/main/java/aoc/App.java"
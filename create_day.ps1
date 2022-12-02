$dayInt = $args[0]
$day = $dayInt.ToString().PadLeft(2, "0")

New-Item -Path "resources/day${day}.txt"
New-Item -Path "resources/day${day}.test.txt"

New-Item -Path "src/aoc/days/Day${day}.java"
$classContent = @"
package aoc.days;

import aoc.common.Day;
import org.jetbrains.annotations.NonNls;

import java.util.List;

/**
 * Day ${day}:
 */
 @NonNls
public final class Day${day} implements Day {
    @Override
    public String part1(final List<String> input) {
        return "Not implemented Part 1"; //NON-NLS
    }

    @Override
    public String part2(final List<String> input) {
        return "Not implemented Part 2"; //NON-NLS
    }
}
"@
Add-Content -Path "src/aoc/days/Day${day}.java" -Value $classContent

New-Item -Path "test/aoc/days/Day${day}Test.java"
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
Add-Content -Path "test/aoc/days/Day${day}Test.java" -Value $testClassContent

$testngContent = @"
    <test name="aoc.test.Day${day}Test">
        <classes>
            <class name="aoc.days.Day${day}Test"/>
        </classes>
    </test>
</suite>
"@

((Get-Content -Path "test/testng.xml" -Raw) -replace "</suite>", $testngContent).Trim() | Set-Content -Path "test/testng.xml"

((Get-Content -Path "src/aoc/App.java" -Raw) `
    -replace "\(\)\)\;\r\n    }", "());`r`n        DAYS.put(${dayInt}, new Day${day}());`r`n    }").Trim() `
    | Set-Content -Path "src/aoc/App.java"
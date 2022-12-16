package aoc.days;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

abstract class AbstractDayTest {
    static List<String> getInput(final int day) {
        final var paddedDay = String.format("%02d", day); //NON-NLS
        final var path      = Paths.get("resources", "day" + paddedDay + ".test.txt");
        
        try {
            return Files.readAllLines(path);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

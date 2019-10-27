package console;

import org.anized.umf.app.InputOutput;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConsoleSpec {

    @Test
    @DisplayName("An I/O console will read a line from it's input source")
    void inputReadsLine() {
        final InputOutput subject =  new InputOutput() {
            @Override
            public Optional<String> nextLine() {
                return Optional.of("q");
            }
            @Override
            public void print(String line, Object... args) {
            }
            @Override
            public void println(String line, Object... args) {
            }
        };
        assertEquals("q", subject.nextLine().get());
    }

    @Test
    @DisplayName("An I/O console will output text")
    void outputFormatsText() {
        final StringBuffer buffer = new StringBuffer();
        final InputOutput subject = new InputOutput() {
            @Override
            public Optional<String> nextLine() { return Optional.of(""); }

            @Override
            public void print(String line, Object... args) { }

            public void println(String line, Object... args) {
                buffer.append(String.format(line, args));
                buffer.append("\n");
            }
        };
        subject.errorLine("Test #%03d", 1);
        assertEquals(InputOutput.Red+"Test #001"+InputOutput.Reset+"\n", buffer.toString());
    }
}

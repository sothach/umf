package org.anized.umf.app;

import java.io.PrintStream;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleInOut implements InputOutput {
    private Scanner input;
    private final PrintStream output;

    public ConsoleInOut(final Scanner scanner, final PrintStream output) {
        this.input = scanner;
        this.output = output;
    }

    @Override
    public Optional<String> nextLine() {
        try {
            return Optional.of(input.nextLine());
        } catch(final Exception e) {
            return Optional.empty();
        }
    }

    public void print(final String line, Object... args) {
        output.printf(line, args);
    }

    public void println(final String line, Object... args) {
        output.printf(line, args);
        output.println();
    }

}

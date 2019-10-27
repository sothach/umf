package org.anized.umf.app;

import java.util.Optional;

public interface InputOutput {
    String Red = "\u001b[31m";
    String Reset ="\u001b[0m";
    Optional<String> nextLine();
    void print(String line, Object... args);
    void println(String line, Object... args);
    default void errorLine(String line, Object... args) {
        println(Red+line+Reset, args);
    }
}

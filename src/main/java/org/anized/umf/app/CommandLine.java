package org.anized.umf.app;

import org.anized.umf.commands.Command;
import org.anized.umf.commands.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CommandLine {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandLine.class);
    private final InputOutput console;
    private final Processor commandProcessor;

    @Autowired
    public CommandLine(final InputOutput console,
                       final Processor commandProcessor) {
        this.console = console;
        this.commandProcessor = commandProcessor;
    }

    public void repl() {
        Optional<Command> command;
        do {
            console.print("Enter command ('?' for help): ");
            command = commandProcessor.find(console.nextLine());
            command.ifPresent( comm -> LOGGER.debug("processing: {}", comm.getName()));
        } while(command.isPresent() && command.get().execute());
    }
}

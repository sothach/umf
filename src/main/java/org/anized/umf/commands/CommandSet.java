package org.anized.umf.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

class CommandSet {
    private final Map<String,Command> commands = new HashMap<>();
    private final Function<String,String> normalize = (String comm) -> {
        assert(comm != null && !comm.isEmpty());
        return comm.trim().toLowerCase().substring(0, 1);
    };

    CommandSet add(final Command command) {
        commands.put(normalize.apply(command.getName()), command);
        return this;
    }

    List<String> list() {
        return commands.entrySet().stream()
                .map(commandPrinter)
                .collect(Collectors.toList());
    }

    private final Function<Map.Entry<String, Command>,String> commandPrinter =
            entry -> entry.getValue().toString();

    Optional<Command> lookup(final String name) {
        return Optional.ofNullable(commands.get(normalize.apply(name)));
    }
}

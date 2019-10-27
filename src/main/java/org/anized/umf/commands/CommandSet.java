package org.anized.umf.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

class CommandSet {
    private Map<String,Command> commands = new HashMap<>();
    private Function<String,String> normalize = (String comm) -> {
        if (comm == null || comm.trim().isEmpty()) {
            return "";
        } else {
            return comm.trim().toLowerCase().substring(0, 1);
        }
    };

    CommandSet add(final Command command) {
        final String normal = normalize.apply(command.getName());
        commands.put(normal, command);
        return this;
    }

    List<String> list() {
        return commands.entrySet().stream()
                .map(commandPrinter)
                .collect(Collectors.toList());
    }

    private Function<Map.Entry<String, Command>,String> commandPrinter =
            entry -> entry.getValue().toString();

    Optional<Command> lookup(final String name) {
        final String normal = normalize.apply(name);
        if (commands.containsKey(normal)) {
            return Optional.of(commands.get(normal));
        } else {
            return Optional.empty();
        }
    }
}

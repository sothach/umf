package org.anized.umf.commands;

public class Command {
    private final String name;
    private final String description;
    private final Executor executor;

    public Command(final String name, final String description,
                   final Executor executor) {
        this.name = name;
        this.description = description;
        this.executor = executor;
    }

    @Override
    public String toString() {
        return String.format("%-6.6s %s", name, description);
    }

    public String getName() { return name; }

    public boolean execute() {
        return executor.execute();
    }
}

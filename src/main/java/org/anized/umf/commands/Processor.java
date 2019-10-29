package org.anized.umf.commands;

import org.anized.common.MessageBundle;
import org.anized.umf.app.InputOutput;
import org.anized.umf.services.ManifestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Processor {
    private final CommandSet commands = new CommandSet();
    private final Command help;

    @Autowired
    public Processor(final ConfigurableApplicationContext context,
                     final ManifestService manifestService,
                     final InputOutput ioChannel,
                     final MessageBundle messages) {
        assert(context != null);
        final Executors executors = new Executors(manifestService,ioChannel,messages);
        help = new Command(messages.text("umf.console.menu.help.key"), messages.text("umf.console.menu.help.text"),
                () -> {
                    commands.list().forEach(ioChannel::println);
                    return true;
                });
        commands.add(help)
                .add(new Command(messages.text("umf.console.menu.add.key"),
                        messages.text("umf.console.menu.add.text"), executors.add()))
                .add(new Command(messages.text("umf.console.menu.edit.key"),
                        messages.text("umf.console.menu.edit.text"), executors.edit()))
                .add(new Command(messages.text("umf.console.menu.delete.key"),
                        messages.text("umf.console.menu.delete.text"), executors.delete()))
                .add(new Command(messages.text("umf.console.menu.count.key"),
                        messages.text("umf.console.menu.count.text"), executors.count()))
                .add(new Command(messages.text("umf.console.menu.list.key"),
                        messages.text("umf.console.menu.list.text"), executors.list()))
                .add(new Command(messages.text("umf.console.menu.upload.key"),
                        messages.text("umf.console.menu.upload.text"), executors.load()))
                .add(new Command(messages.text("umf.console.menu.quit.key"),
                        messages.text("umf.console.menu.quit.text"), () -> {context.close(); return false;}));
    }

    public Optional<Command> find(final Optional<String> input) {
        return input.map(key -> commands.lookup(key).orElse(help));
    }
}

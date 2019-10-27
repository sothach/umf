package org.anized.umf.commands;

import org.anized.common.Try;
import org.anized.umf.app.InputOutput;
import org.anized.umf.model.User;
import org.anized.umf.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

@Component
public class Processor {
    private final CommandSet commands = new CommandSet();
    private UserService userService;
    private InputOutput ioChannel;

    @Autowired
    public Processor(final ConfigurableApplicationContext context,
                     final UserService userService,
                     final InputOutput ioChannel) {
        this.userService = userService;
        this.ioChannel = ioChannel;
        commands.add(help)
                .add(new Command("add",    "add a user", add))           // 1. Add Person (id, firstName, surname)
                .add(new Command("edit",   "edit a user", edit))         // 2. Edit Person (firstName, surname)
                .add(new Command("delete", "delete a user", delete))     // 3. Delete Person (id)
                .add(new Command("count",  "count stored users", count)) // 4. Count Number of Persons
                .add(new Command("list",   "list stored user", list))    // 5. List Persons
                .add(new Command("upload", "upload user(s) from XML", load)) // 6. Load user from xml file
                .add(new Command( "quit",  "terminate program", () -> {context.close(); return false;}));
    }
    private Command help =
                     new Command("help",   "display command help",  () -> {
                         commands.list().forEach(ioChannel::println);
                         return true;
                     });

    public Optional<Command> find(final Optional<String> input) {
        return input.map(key -> commands.lookup(key).orElse(help));
    }

    private Executor add = () -> {
        ioChannel.print("Enter User (id, surname, firstname): ");
        ioChannel.nextLine().ifPresent( user -> {
            final Optional<User> maybeNewUser = User.apply(user);
            if (!maybeNewUser.isPresent()) {
                ioChannel.errorLine("invalid user '%s', please enter: id, surname, firstname", user);
            } else {
                final User newUser = maybeNewUser.get();
                final Optional<User> existingUser = userService.read(newUser);
                if (existingUser.isPresent()) {
                    ioChannel.errorLine("user '%s' already exists in manifest", existingUser.get());
                } else {
                    userService.create(newUser).onSuccess(ok ->
                            ioChannel.println("user '%s' successfully added", newUser));
                }
            }
        });
        return true;
    };

    private Executor list = () -> {
        userService.findAll().forEach(user -> ioChannel.println(user.toString()));
        return true;
    };

    private Executor count = () -> {
        ioChannel.println("# User records=%d", userService.count());
        return true;
    };

    private Executor delete = () -> {
        ioChannel.print("Enter User Id: ");
        ioChannel.nextLine().ifPresent(id -> {
            try {
                final BigInteger userId = new BigInteger(id.trim());
                final Try<User> user = userService.delete(userId);
                if (user.isSuccess()) {
                    ioChannel.println("deleted user %d", userId);
                } else {
                    ioChannel.errorLine("failed to delete user #" + userId);
                }
            } catch (final NumberFormatException nfe) {
                ioChannel.errorLine("invalid user id: %s", id);
            }
        });
        return true;
    };

    private Function<Optional<String>,Optional<String>> cleanInput = input -> {
        return input.map(String::trim).filter(value -> !value.isEmpty());
    };

    private Executor edit = () -> {
        ioChannel.print("Enter Id of user: ");
        ioChannel.nextLine().ifPresent(userId -> {
            final Optional<User> maybeUser = findUser(userId);
            if (!maybeUser.isPresent()) {
                ioChannel.errorLine("user %s does not exist", userId);
            } else {
                final User user = maybeUser.get();
                final User.Builder builder = User.builder(user);
                ioChannel.print("Enter new firstName (return to use %s): ", user.getFirstName());
                final Optional<String> firstName = ioChannel.nextLine();
                if (cleanInput.apply(firstName).isPresent()) {
                    builder.setFirstName(firstName.get());
                }
                ioChannel.print("Enter new surname (return to use %s): ", user.getSurname());
                final Optional<String> surname = ioChannel.nextLine();
                if (cleanInput.apply(surname).isPresent()) {
                    builder.setSurname(surname.get());
                }
                userService.update(builder.build())
                    .onSuccess(updated -> {
                        ioChannel.println("updated user: %s", updated);
                    }).onFailure(error ->
                        ioChannel.errorLine("failed to update: %s", user, error.getMessage()));
            }
        });
        return true;
    };

    private Executor load = () -> {
        ioChannel.print("Enter path to user XML file: ");
        ioChannel.nextLine().ifPresent(path ->
        DataLoader.loadFromXmlFile(path)
            .onSuccess(userList -> {
                Stream<Try<Boolean>> createdUsers = userList.stream().map(user -> userService.create(user));
                long success = createdUsers.filter(Try::isSuccess).count();
                ioChannel.println("loaded %d user records of %d",success,userList.size());
            }).onFailure(error ->
                ioChannel.errorLine("failed to load user records from '%s': %s", path, error.getMessage())));
        return true;
    };

    private Optional<User> findUser(final String id) {
        try {
            final BigInteger userId = new BigInteger(id.trim());
            return userService.findUserById(userId);
        } catch(final NumberFormatException nfe) {
            ioChannel.errorLine("invalid user id: %s", id);
            return Optional.empty();
        }
    }

}

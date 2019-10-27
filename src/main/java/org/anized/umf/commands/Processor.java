package org.anized.umf.commands;

import org.anized.common.Try;
import org.anized.umf.app.InputOutput;
import org.anized.umf.model.Person;
import org.anized.umf.services.ManifestService;
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
    private ManifestService manifestService;
    private InputOutput ioChannel;

    @Autowired
    public Processor(final ConfigurableApplicationContext context,
                     final ManifestService manifestService,
                     final InputOutput ioChannel) {
        this.manifestService = manifestService;
        this.ioChannel = ioChannel;
        commands.add(help)
                .add(new Command("add",    "Add Person (id, firstName, surname)", add)) 
                .add(new Command("edit",   "Edit Person (firstName, surname)", edit))
                .add(new Command("delete", "Delete Person (id)", delete)) 
                .add(new Command("count",  "Count Number of Persons", count))
                .add(new Command("list",   "List Persons", list)) 
                .add(new Command("upload", "Load Person manifest from xml file", load))
                .add(new Command("quit",   "Terminate program", () -> {context.close(); return false;}));
    }
    private Command help =
                     new Command("help",   "Display command help",  () -> {
                         commands.list().forEach(ioChannel::println);
                         return true;
                     });

    public Optional<Command> find(final Optional<String> input) {
        return input.map(key -> commands.lookup(key).orElse(help));
    }

    private Executor add = () -> {
        ioChannel.print("Enter Person (id, firstname, surname): ");
        ioChannel.nextLine().ifPresent( person -> {
            final Optional<Person> maybeNewPerson = Person.apply(person);
            if (!maybeNewPerson.isPresent()) {
                ioChannel.errorLine("invalid Person '%s', please enter: id, firstname, surname", person);
            } else {
                final Person newPerson = maybeNewPerson.get();
                final Optional<Person> existingPerson = manifestService.read(newPerson);
                if (existingPerson.isPresent()) {
                    final Person u = existingPerson.get();
                    ioChannel.errorLine("Person '%s, %s' already exists in manifest with id=%s",
                            u.getFirstName(), u.getSurname(), u.getId());
                } else {
                    manifestService.create(newPerson).onSuccess(ok ->
                            ioChannel.println("Person '%s' successfully added", newPerson));
                }
            }
        });
        return true;
    };

    private Executor list = () -> {
        manifestService.findAll().forEach(Person -> ioChannel.println(Person.toString()));
        return true;
    };

    private Executor count = () -> {
        ioChannel.println("# Person records=%d", manifestService.count());
        return true;
    };

    private Executor delete = () -> {
        ioChannel.print("Enter Person Id: ");
        ioChannel.nextLine().ifPresent(id -> {
            try {
                final BigInteger PersonId = new BigInteger(id.trim());
                final Try<Person> Person = manifestService.delete(PersonId);
                if (Person.isSuccess()) {
                    ioChannel.println("deleted Person %d", PersonId);
                } else {
                    ioChannel.errorLine("failed to delete Person #" + PersonId);
                }
            } catch (final NumberFormatException nfe) {
                ioChannel.errorLine("invalid Person id: %s", id);
            }
        });
        return true;
    };

    private Function<Optional<String>,Optional<String>> cleanInput = input -> {
        return input.map(String::trim).filter(value -> !value.isEmpty());
    };

    private Executor edit = () -> {
        ioChannel.print("Enter Person (firstname, surname): ");
        ioChannel.nextLine().ifPresent(Personname -> {
           final Person editPerson = Person.builder().parse(Personname).build();
           final Optional<Person> maybePerson = manifestService.read(editPerson);
            if (!maybePerson.isPresent()) {
                ioChannel.errorLine("Person '%s' does not exist", Personname);
            } else {
                final Person person = maybePerson.get();
                final Person.Builder builder = Person.builder(person);
                ioChannel.print("Enter new firstName (return to use %s): ", person.getFirstName());
                final Optional<String> firstName = ioChannel.nextLine();
                if (cleanInput.apply(firstName).isPresent()) {
                    builder.setFirstName(firstName.get());
                }
                ioChannel.print("Enter new surname (return to use %s): ", person.getSurname());
                final Optional<String> surname = ioChannel.nextLine();
                if (cleanInput.apply(surname).isPresent()) {
                    builder.setSurname(surname.get());
                }
                manifestService.update(builder.build())
                    .onSuccess(updated -> {
                        ioChannel.println("updated Person: %s", updated);
                    }).onFailure(error ->
                        ioChannel.errorLine("failed to update: %s", person, error.getMessage()));
            }
        });
        return true;
    };

    private Executor load = () -> {
        ioChannel.print("Enter path to Person XML file: ");
        ioChannel.nextLine().ifPresent(path ->
        DataLoader.loadFromXmlFile(path)
            .onSuccess(PersonList -> {
                Stream<Try<Boolean>> createdPersons = PersonList.stream().map(Person -> manifestService.create(Person));
                long success = createdPersons.filter(Try::isSuccess).count();
                ioChannel.println("loaded %d Person records of %d",success,PersonList.size());
            }).onFailure(error ->
                ioChannel.errorLine("failed to load Person records from '%s': %s", path, error.getMessage())));
        return true;
    };

}

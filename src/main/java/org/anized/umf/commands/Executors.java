package org.anized.umf.commands;

import org.anized.common.MessageBundle;
import org.anized.common.Try;
import org.anized.umf.app.InputOutput;
import org.anized.umf.model.Person;
import org.anized.umf.services.ManifestService;

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

class Executors {
    private final ManifestService manifestService;
    private final InputOutput ioChannel;
    private final MessageBundle messages;

    Executors(final ManifestService manifestService,
              final InputOutput ioChannel,
              final MessageBundle messages) {
        assert(manifestService != null);
        assert(ioChannel != null);
        this.manifestService = manifestService;
        this.ioChannel = ioChannel;
        this.messages = messages;
    }

    final Executor add() {
        return () -> {
            ioChannel.print(messages.text("umf.console.input.person.full"));
            ioChannel.nextLine().ifPresent(person -> {
                final Optional<Person> maybeNewPerson = Person.apply(person);
                if (!maybeNewPerson.isPresent()) {
                    ioChannel.errorLine(messages.text("umf.console.failed.invalid.person", person));
                } else {
                    final Person newPerson = maybeNewPerson.get();
                    final Optional<Person> existingPerson = manifestService.read(newPerson);
                    if (existingPerson.isPresent()) {
                        final Person u = existingPerson.get();
                        ioChannel.errorLine(
                                messages.text("umf.console.failed.person.exists", u.getFirstName(), u.getSurname(), u.getId().toString()));
                    } else {
                        manifestService.create(newPerson).onSuccess(ok ->
                                ioChannel.println(messages.text("umf.console.success.person.added", newPerson)));
                    }
                }
            });
            return true;
        };
    }

    final Executor list() {
        return () -> {
            manifestService.findAll().forEach(Person -> ioChannel.println(Person.toString()));
            return true;
        };
    }

    final Executor count() {
        return () -> {
            ioChannel.println(messages.text("umf.console.success.person.records", manifestService.count()));
            return true;
        };
    }

    final Executor delete() {
        return () -> {
            ioChannel.print(messages.text("umf.console.input.person.id"));
            ioChannel.nextLine().ifPresent(id -> {
                try {
                    final BigInteger personId = new BigInteger(id.trim());
                    manifestService.delete(personId)
                            .onSuccess(person -> ioChannel.println(messages.text("umf.console.success.person.deleted", personId.toString())))
                            .onFailure(e -> ioChannel.errorLine(messages.text("umf.console.failed.person.deleted", personId.toString())));
                } catch (final NumberFormatException nfe) {
                    ioChannel.errorLine(messages.text("umf.console.failed.person.invalid", id));
                }
            });
            return true;
        };
    }

    private final Function<Optional<String>,Optional<String>> cleanInput =
            input -> input.map(String::trim).filter(value -> !value.isEmpty());

    final Executor edit() {
        return () -> {
            ioChannel.print(messages.text("umf.console.input.person.name"));
            ioChannel.nextLine().ifPresent(personname -> {
                final Person editPerson = Person.builder().parse(personname).build();
                final Optional<Person> maybePerson = manifestService.read(editPerson);
                if (!maybePerson.isPresent()) {
                    ioChannel.errorLine(messages.text("umf.console.failed.person.notExists", personname));
                } else {
                    final Person person = maybePerson.get();
                    final Person.Builder builder = Person.builder(person);
                    ioChannel.print(messages.text("umf.console.input.person.firstname", person.getFirstName()));
                    final Optional<String> firstName = ioChannel.nextLine();
                    cleanInput.apply(firstName).ifPresent(builder::setFirstName);
                    ioChannel.print(messages.text("umf.console.input.person.surname", person.getSurname()));
                    final Optional<String> surname = ioChannel.nextLine();
                    cleanInput.apply(surname).ifPresent(builder::setSurname);
                    manifestService.update(builder.build())
                            .onSuccess(updated ->
                                    ioChannel.println(messages.text("umf.console.success.person.updated", updated)))
                            .onFailure(error ->
                                    ioChannel.errorLine(messages.text("umf.console.failed.person.update", person, error.getMessage())));
                }
            });
            return true;
        };
    }

    final Executor load() {
        return () -> {
            ioChannel.print(messages.text("umf.console.input.xml.filepath"));
            ioChannel.nextLine().ifPresent(path ->
                    DataLoader.loadFromXmlFile(path)
                            .onSuccess(personList -> {
                                final Stream<Try<Boolean>> createdPersons = personList.stream().map(manifestService::create);
                                final long success = createdPersons.filter(Try::isSuccess).count();
                                ioChannel.println(messages.text("umf.console.success.loaded.count", success, personList.size()));
                            }).onFailure(error ->
                            ioChannel.errorLine(messages.text("umf.console.failed.load.xml", path, error.getMessage()))));
            return true;
        };
    }
}

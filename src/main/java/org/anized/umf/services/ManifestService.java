package org.anized.umf.services;

import org.anized.common.Try;
import org.anized.umf.model.Person;
import org.anized.umf.persistence.ManifestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
public class ManifestService {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(ManifestService.class);
    private final ManifestRepository storage;

    @Autowired
    public ManifestService(final ManifestRepository storage) {
       this.storage = storage;
    }

    public Try<Boolean> create(final Person person) {
        final Person canon = Person.builder(person)
                .setFirstName(person.getFirstName().trim().toLowerCase())
                .setSurname(person.getSurname().trim().toLowerCase())
                .build();
        return Try.apply(() -> {
            if (read(canon).isPresent()) {
                LOGGER.warn("failed to create Person: {} already exists", person);
                throw new IllegalStateException("Person already exists: " + canon);
            } else {
                storage.save(canon);
                return true;
            }
        });
    }

    public Optional<Person> read(final Person person) {
        return findByName(person.getSurname(), person.getFirstName());
    }

    public Try<Person> update(final Person person) {
        return Try.apply(() -> {
            if (findPersonById(person.getId()).isPresent()) {
                storage.save(person);
                return person;
            } else {
                LOGGER.warn("failed to update Person: {} does not exist", person);
                throw new IllegalStateException("Person does not exist: " + person);
            }
        });
    }

    public Try<Person> delete(final BigInteger personId) {
        final Optional<Person> maybePerson = storage.findById(personId);
        LOGGER.debug("deleting Person id={}: {}", personId,
                maybePerson.map(Person::toString).orElse("<not found>"));
        return Try.apply(() -> {
            if (!maybePerson.isPresent()) {
                LOGGER.warn("failed to delete Person id={} does not exist", personId);
                throw new IllegalStateException("Person does not exist for id=" + personId);
            } else {
                final Person person = maybePerson.get();
                storage.delete(person);
                return person;
            }
        });
    }

    public List<Person> findAll() {
        return storage.findAll();
    }

    public long count() {
        return storage.count();
    }

    private Optional<Person> findByName(final String surname, final String forename) {
        return storage.findBySurnameAndFirstName(
                surname.trim().toLowerCase(), forename.trim().toLowerCase());
    }

    private Optional<Person> findPersonById(final BigInteger personId) {
        return storage.findById(personId);
    }

}

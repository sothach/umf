package services;

import org.anized.common.Try;
import org.anized.umf.model.Person;
import org.anized.umf.services.ManifestService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.math.BigInteger;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = { ManifestService.class})
@AutoConfigureDataMongo
@EnableAutoConfiguration
@EnableMongoRepositories("org.anized.umf.persistence")
class PersonServiceIntegrationTest {

    @Autowired
    private ManifestService manifestService;

    private final Person person = Person.builder()
            .setId(BigInteger.TEN)
            .setFirstName("Fred")
            .setSurname("Feuerstein")
            .build();

    @Test
    @DisplayName("A person record can be persisted and updated via the service")
    void testCreate() {
        manifestService.create(person);
        final Optional<Person> found = manifestService.read(person);
        assertTrue(found.isPresent());
        found.ifPresent(u -> assertEquals(u, person));
        manifestService.update(person).onFailure(e -> fail(e.getMessage()));
    }

    @Test
    @DisplayName("An attempt to create a duplicate person should fail")
    void testCreateDuplicates() {
        final Person testPerson = Person.builder(person)
                .setFirstName("Pebbles").build();
        manifestService.create(testPerson);
        final Optional<Person> found = manifestService.read(testPerson);
        assertTrue(found.isPresent());
        found.ifPresent(u -> assertEquals(u, testPerson));

        assertFalse(manifestService.create(testPerson).isSuccess());
    }

    @Test
    @DisplayName("An attempt to update a non-existent person should fail")
    void testUpdateNonExistent() {
        final Person testPerson = Person.builder(person)
                .setId(new BigInteger("17123"))
                .setFirstName("Dino").build();
        manifestService.update(testPerson).onSuccess(p -> fail());
    }

    @Test
    @DisplayName("A person record can be deleted via the service")
    void testDelete() {
        final Person testPerson = Person.builder(person)
                .setFirstName("Wilma").build();
        manifestService.create(testPerson);
        final Optional<Person> found = manifestService.read(testPerson);
        assertTrue(found.isPresent());
        found.ifPresent(u -> {
            final Try<Person> deleted = manifestService.delete(u.getId());
            assertTrue(deleted.isSuccess());
        });
        final Optional<Person> refind = manifestService.read(testPerson);
        assertFalse(refind.isPresent());
    }

    @Test
    @DisplayName("An attempt to delete a non-existent person should fail")
    void testDeletedNonExistent() {
        manifestService.delete(new BigInteger("17123")).onSuccess(p -> fail());
    }

    @Test
    @DisplayName("All user records can be retrieved")
    void testListAll() {
        manifestService.findAll().forEach (user ->
                System.out.println("User: "+ user));
    }
}
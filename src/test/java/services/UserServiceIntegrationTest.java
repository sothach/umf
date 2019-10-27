package services;

import org.anized.common.Try;
import org.anized.umf.model.User;
import org.anized.umf.services.UserService;
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

@SpringBootTest(classes = { UserService.class})
@AutoConfigureDataMongo
@EnableAutoConfiguration
@EnableMongoRepositories("org.anized.umf.persistence")
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    private final User user = User.builder()
            .setId(BigInteger.TEN)
            .setFirstName("Fred")
            .setSurname("Feuerstein")
            .build();

    @Test
    @DisplayName("A user record can be persisted via the service")
    void testCreate() {
        userService.create(user);
        final Optional<User> found = userService.read(user);
        assertTrue(found.isPresent());
        found.ifPresent(u -> assertEquals(u, user));
    }

    @Test
    @DisplayName("Attempts to create a duplicate user will fail")
    void testCreateDuplicates() {
        final User testUser = User.builder(user)
                .setFirstName("Pebbles").build();
        userService.create(testUser);
        final Optional<User> found = userService.read(testUser);
        assertTrue(found.isPresent());
        found.ifPresent(u -> assertEquals(u, testUser));

        assertFalse(userService.create(testUser).isSuccess());
    }

    @Test
    @DisplayName("A user record can be deleted via the service")
    void testDelete() {
        final User testUser = User.builder(user)
                .setFirstName("Wilma").build();
        userService.create(testUser);
        final Optional<User> found = userService.read(testUser);
        assertTrue(found.isPresent());
        found.ifPresent(u -> {
            final Try<User> deleted = userService.delete(u.getId());
            assertTrue(deleted.isSuccess());
        });
        final Optional<User> refind = userService.read(testUser);
        assertFalse(refind.isPresent());
    }

    @Test
    @DisplayName("All user records can be retrieved")
    void testListAll() {
        userService.findAll().forEach ( user ->
                System.out.println("User: "+ user));
    }
}
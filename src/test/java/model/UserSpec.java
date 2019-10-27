package model;

import org.anized.umf.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserSpec {

    @Test
    @DisplayName("The User builder constructs a valid object")
    void userObjectBuilt() {
        final User subject = User.builder(expected)
                .setFirstName("Fred")
                .setSurname("Flintstone")
                .setId(BigInteger.ONE)
                .build();
        assertEquals("Flintstone, Fred (ID=1)", subject.toString());
        assertEquals(expected.getSurname(), subject.getSurname());
        assertEquals(expected.getFirstName(), subject.getFirstName());
        assertEquals(expected, subject);
    }

    @Test
    @DisplayName("The User creator constructs a valid object")
    void userCreated() {
        final User subject = User.createUser("1", "Fred","Flintstone");
        assertEquals("Flintstone, Fred (ID=1)", subject.toString());
        assertEquals(expected.getSurname(), subject.getSurname());
        assertEquals(expected.getFirstName(), subject.getFirstName());
        assertEquals(expected, subject);
        assertFalse(expected.hashCode() != subject.hashCode());
    }

    @Test
    @DisplayName("An invalid user string is detected")
    void invalidUserString() {
        final Optional<User> subject = User.apply("Flintstone. Fred");
        assertFalse(subject.isPresent());
    }

    private User expected = User.apply("1, Flintstone, Fred").get();
}

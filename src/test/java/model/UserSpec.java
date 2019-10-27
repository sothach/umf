package model;

import org.anized.umf.model.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserSpec {

    @Test
    @DisplayName("The User builder constructs a valid object")
    void userObjectBuilt() {
        final Person subject = Person.builder(expected)
                .setFirstName("Fred")
                .setSurname("Flintstone")
                .setId(BigInteger.ONE)
                .build();
        assertEquals("Fred, Flintstone (ID=1)", subject.toString());
        assertTrue(expected.getSurname().equalsIgnoreCase(subject.getSurname()));
        assertTrue(expected.getFirstName().equalsIgnoreCase(subject.getFirstName()));
        assertEquals(expected, subject);
    }

    @Test
    @DisplayName("The User creator constructs a valid object")
    void userCreated() {
        final Person subject = Person.createPerson("1", "Fred","Flintstone");
        assertEquals("Fred, Flintstone (ID=1)", subject.toString());
        assertTrue(expected.getSurname().equalsIgnoreCase(subject.getSurname()));
        assertTrue(expected.getFirstName().equalsIgnoreCase(subject.getFirstName()));
        assertEquals(expected, subject);
        assertFalse(expected.hashCode() != subject.hashCode());
    }

    @Test
    @DisplayName("An invalid user string is detected")
    void invalidUserString() {
        final Optional<Person> subject = Person.apply("Fred, Flintstone");
        assertFalse(subject.isPresent());
    }

    private Person expected = Person.apply("1, Fred, Flintstone").get();
}

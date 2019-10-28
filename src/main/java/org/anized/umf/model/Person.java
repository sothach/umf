package org.anized.umf.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;

public class Person {
    @Id
    private final BigInteger id;
    private final String firstName;
    private final String surname;

    private Person(final BigInteger id, final String firstName, final String surname) {
        this.firstName = firstName;
        this.surname = surname;
        this.id = id;
    }

    public BigInteger getId() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getSurname() {
        return surname;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Person person = (Person) o;
        return id.equals(person.id) && firstName.equalsIgnoreCase(person.firstName) &&
                surname.equalsIgnoreCase(person.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, surname);
    }

    @Override
    public String toString() {
        final String sname = Character.toUpperCase(surname.charAt(0)) + surname.substring(1);
        final String fname = Character.toUpperCase(firstName.charAt(0)) + firstName.substring(1);
        return fname + ", " + sname + " (ID=" + id + ")";
    }

    public static Optional<Person> apply(final String fullName) {
        final String[] parts = fullName.split(",");
        if(parts.length != 3) {
            return Optional.empty();
        }
        final Person.Builder builder = Person.builder();
        return Optional.of(builder
                .setId(new BigInteger(parts[0].trim()))
                .setFirstName(parts[1].trim().toLowerCase())
                .setSurname(parts[2].trim().toLowerCase())
                .build());
    }

    @JsonCreator
    public static Person createPerson(
            @JsonProperty("id") String id,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("surname") String surname) {
        return builder()
                .setId(new BigInteger(id.trim()))
                .setFirstName(firstName.toLowerCase())
                .setSurname(surname.toLowerCase())
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(final Person prototype) {
        return (new Builder()).clone(prototype);
    }

    public static class Builder {
        private BigInteger id = BigInteger.ZERO;
        private String firstName = "  ";
        private String surname = "  ";

        public Builder parse(final String fullName) {
            final String[] parts = fullName.split(",");
            if(parts.length > 0) {
                setFirstName(parts[0].trim());
            }
            if(parts.length > 1) {
                setSurname(parts[1].trim());
            }
            return this;
        }

        public Builder setId(final BigInteger id) {
            this.id = id;
            return this;
        }

        public Builder setFirstName(final String firstName) {
            assert(firstName != null && !firstName.isEmpty());
            this.firstName = firstName;
            return this;
        }

        public Builder setSurname(final String surname) {
            assert(surname != null && !surname.isEmpty());
            this.surname = surname;
            return this;
        }

        Builder clone(final Person other) {
            this.id = other.id;
            this.firstName = other.firstName;
            this.surname = other.surname;
            return this;
        }

        public Person build() {
            return new Person(id, firstName, surname);
        }
    }
}

package org.anized.umf.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;

public class User {
    @Id
    private BigInteger id;
    private String firstName;
    private String surname;

    private User(final BigInteger id, final String firstName, final String surname) {
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
        final User user = (User) o;
        return id.equals(user.id) && firstName.equalsIgnoreCase(user.firstName) &&
                surname.equalsIgnoreCase(user.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, surname);
    }

    @Override
    public String toString() {
        final String sname = Character.toUpperCase(surname.charAt(0)) + surname.substring(1);
        final String fname = Character.toUpperCase(firstName.charAt(0)) + firstName.substring(1);
        return sname + ", " + fname + " (ID=" + id + ")";
    }

    public static Optional<User> apply(final String userText) {
        final String[] parts = userText.split(",");
        if(parts.length != 3) {
            return Optional.empty();
        }
        final User.Builder builder = User.builder();
        return Optional.of(builder
                .setId(new BigInteger(parts[0].trim()))
                .setFirstName(parts[2].trim())
                .setSurname(parts[1].trim())
                .build());
    }

    @JsonCreator
    public static User createUser(
            @JsonProperty("id") String id,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("surname") String surname) {
        return builder()
                .setId(new BigInteger(id.trim()))
                .setFirstName(firstName)
                .setSurname(surname)
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(final User prototype) {
        return (new Builder()).clone(prototype);
    }

    public static class Builder {
        private BigInteger id;
        private String firstName;
        private String surname;

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

        public Builder clone(final User other) {
            this.id = other.id;
            this.firstName = other.firstName;
            this.surname = other.surname;
            return this;
        }

        public User build() {
            return new User(id, firstName, surname);
        }
    }
}
